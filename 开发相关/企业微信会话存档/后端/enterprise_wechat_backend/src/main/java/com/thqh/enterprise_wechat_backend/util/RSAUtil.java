package com.thqh.enterprise_wechat_backend.util;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.ClassPathResource;
import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * @ClassName: RSAUtil
 * @Description:
 * @Author liubin
 * @Date 2025/3/3 15:14
 * @Version V1.0
 */
public class RSAUtil {

    static {
        // 添加 BC Provider（如果尚未添加）
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 生成 RSA 密钥对，密钥长度为 2048 位
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    /**
     * 将 PrivateKey 转换为 PKCS#1 格式的 PEM 字符串
     */
    public static String getPEMFromPrivateKey(PrivateKey privateKey) throws Exception {
        RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey) privateKey;
        // 使用 BouncyCastle 构造 PKCS#1 格式的 RSAPrivateKey
        RSAPrivateKey bcPrivateKey = new RSAPrivateKey(
                rsaPrivateCrtKey.getModulus(),
                rsaPrivateCrtKey.getPublicExponent(),
                rsaPrivateCrtKey.getPrivateExponent(),
                rsaPrivateCrtKey.getPrimeP(),
                rsaPrivateCrtKey.getPrimeQ(),
                rsaPrivateCrtKey.getPrimeExponentP(),
                rsaPrivateCrtKey.getPrimeExponentQ(),
                rsaPrivateCrtKey.getCrtCoefficient()
        );
        byte[] encoded = bcPrivateKey.getEncoded();
        String pem = "-----BEGIN RSA PRIVATE KEY-----\n" +
                Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(encoded) +
                "\n-----END RSA PRIVATE KEY-----";
        return pem;
    }

    /**
     * 将 PublicKey 转换为 PKCS#1 格式的 PEM 字符串
     */
    public static String getPEMFromPublicKey(PublicKey publicKey) throws Exception {
        // 将公钥转换为 java.security.interfaces.RSAPublicKey
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
        // 使用 BouncyCastle 构造 PKCS#1 格式的 RSAPublicKey，注意使用全限定名避免冲突
        org.bouncycastle.asn1.pkcs.RSAPublicKey bcPublicKey = new org.bouncycastle.asn1.pkcs.RSAPublicKey(
                rsaPublicKey.getModulus(),
                rsaPublicKey.getPublicExponent()
        );
        byte[] encoded = bcPublicKey.getEncoded();
        String pem = "-----BEGIN RSA PUBLIC KEY-----\n" +
                Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(encoded) +
                "\n-----END RSA PUBLIC KEY-----";
        return pem;
    }

    /**
     * 使用公钥进行 RSA 加密，返回加密后的字节数组
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 使用私钥进行 RSA 解密，返回解密后的字节数组
     */
    public static byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }

    /**
     * 工具方法：对字符串加密后返回 Base64 编码的密文
     */
    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        byte[] encryptedBytes = encrypt(plainText.getBytes(StandardCharsets.UTF_8), publicKey);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 工具方法：对 Base64 编码的密文解密后返回明文字符串
     */
    public static String decrypt(String base64Encrypted, PrivateKey privateKey) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(base64Encrypted);
        byte[] decryptedBytes = decrypt(encryptedBytes, privateKey);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 将 PublicKey 转换为 X.509 格式的 PEM 字符串
     */
    public static String getX509PEMFromPublicKey(PublicKey publicKey) {
        byte[] encoded = publicKey.getEncoded();
        String pem = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(encoded) +
                "\n-----END PUBLIC KEY-----";
        return pem;
    }

    /**
     * 将 PublicKey 转换为 X.509 格式的 PEM 字符串
     */
    public static String getX509PEMFromprivateKey(PrivateKey privateKey) {
        byte[] encoded = privateKey.getEncoded();
        String pem = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(encoded) +
                "\n-----END PRIVATE KEY-----";
        return pem;
    }


    /**
     * 从 PEM 文件中读取公钥（X.509 格式），并转换为 PublicKey 对象
     * 文件内容应包含类似 "-----BEGIN PUBLIC KEY-----" 的头部
     */
    /**
     * 从字符串形式的 PEM 公钥中解析出 PublicKey 对象
     * 输入的 keyString 应该包含 PEM 格式的头部和尾部，如 "-----BEGIN PUBLIC KEY-----"
     */
    public static PublicKey readPublicKeyFromString(String keyString) throws Exception {
        try (Reader reader = new StringReader(keyString);
             PEMParser pemParser = new PEMParser(reader)) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            if (object instanceof SubjectPublicKeyInfo) {
                return converter.getPublicKey((SubjectPublicKeyInfo) object);
            } else if (object instanceof X509CertificateHolder) {
                X509CertificateHolder certHolder = (X509CertificateHolder) object;
                return converter.getPublicKey(certHolder.getSubjectPublicKeyInfo());
            } else {
                throw new IllegalArgumentException("Unsupported public key format: " + object.getClass().getName());
            }
        }
    }

    /**
     * 从字符串形式的 PEM 私钥中解析出 PrivateKey 对象
     * 输入的 keyString 应该包含 PEM 格式的头部和尾部，如 "-----BEGIN PRIVATE KEY-----"
     */
    public static PrivateKey readPrivateKeyFromString(String keyString) throws Exception {
        try (Reader reader = new StringReader(keyString);
             PEMParser pemParser = new PEMParser(reader)) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            if (object instanceof PrivateKeyInfo) {
                return converter.getPrivateKey((PrivateKeyInfo) object);
            } else {
                throw new IllegalArgumentException("Unsupported private key format: " + object.getClass().getName());
            }
        }
    }


    /**
     * 从w文件形式的 PEM 私钥中解析出 PrivateKey 对象
     * 输入的 keyString 应该包含 PEM 格式的头部和尾部，如 "-----BEGIN PRIVATE KEY-----"
     */
    public static PrivateKey readPrivateKeyFromFile(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String privateKey = reader.lines().collect(Collectors.joining("\n"));
            return readPrivateKeyFromString(privateKey);
        }


    }

    public static PublicKey readPublicKeyFromFile(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String privateKey = reader.lines().collect(Collectors.joining("\n"));
            return readPublicKeyFromString(privateKey);
        }


    }

}
