package com.thqh.wechat_miniprogram_backend.util;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: EncryptionUtil
 * @Description:
 * @Author liubin
 * @Date 2025/1/6 17:36
 * @Version V1.0
 */


@Component
public class EncryptionUtil {

    @Value("${encryption.aes.key}")
    private String aesKey;

    @Value("${encryption.aes.iv}")
    private String aesIv;

    // RSA 配置
    // Base64 编码的公钥
    @Value("${encryption.rsa.publicKey}")
    private String rsaPublicKey;
    // Base64 编码的私钥
    @Value("${encryption.rsa.privateKey}")
    private String rsaPrivateKey;


    // SM4 配置(16 字节的SM4密钥)
    @Value("${encryption.sm4.key}")
    private String sm4Key;
    // 16 字节的SM4初始化向量
    @Value("${encryption.sm4.iv}")
    private String sm4Iv;

    /**
     * 使用AES-256-CBC加密
     *
     * @param data 需要加密的数据
     * @return Base64编码的加密数据
     */
    public String encryptAes256Cbc(String data) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(aesIv.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * 使用AES-256-CBC解密
     *
     * @param encryptedData Base64编码的加密数据
     * @return 解密后的数据
     */
    public String decryptAes256Cbc(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(aesIv.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    /**
     * 使用 RSA 私钥加密
     *
     * @param data 需要加密的数据
     * @return Base64 编码的加密数据
     */
    public  String encryptRSAPrivate(String data) {
        try {
            RSAPrivateKey privateKey = getRSAPrivateKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encryptedBytes = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(StandardCharsets.UTF_8), privateKey.getModulus().bitLength());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("使用私钥加密失败", e);
        }
    }

    /**
     * 使用 RSA 公钥解密
     *
     * @param data 需要解密的数据（Base64 编码）
     * @return 解密后的数据
     */
    public String decryptRSAPublic(String data) {
        try {
            RSAPublicKey publicKey = getRSAPublicKey();
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] decryptedBytes = rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data), publicKey.getModulus().bitLength());
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("使用公钥解密失败", e);
        }
    }

    /**
     * RSA 分段加密/解密
     *
     * @param cipher   Cipher 实例
     * @param opMode   操作模式（加密或解密）
     * @param data     需要处理的数据
     * @param keySize  RSA 密钥大小
     * @return 处理后的数据
     */
    private byte[] rsaSplitCodec(Cipher cipher, int opMode, byte[] data, int keySize) {
        int blockSize = (opMode == Cipher.DECRYPT_MODE) ? keySize / 8 : keySize / 8 - 11;
        int offset = 0;
        List<byte[]> resultList = new ArrayList<>();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            while (data.length > offset) {
                int length = Math.min(data.length - offset, blockSize);
                byte[] encryptedBlock = cipher.doFinal(data, offset, length);
                resultList.add(encryptedBlock);
                offset += blockSize;
            }

            for (byte[] block : resultList) {
                outputStream.write(block);
            }

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("RSA分段处理失败", e);
        }
    }

    /**
     * 将字符串转换为十六进制字符串
     *
     * @param s 原始字符串
     * @return 十六进制表示的字符串
     */
    public String toHexString(String s) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            int ch = s.charAt(i);
            str.append(Integer.toHexString(ch));
        }
        return str.toString();
    }

    /**
     * 加载 RSA 私钥
     *
     * @return RSAPrivateKey 实例
     */
    private RSAPrivateKey getRSAPrivateKey() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(rsaPrivateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) factory.generatePrivate(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("加载RSA私钥失败", e);
        }
    }

    /**
     * 加载 RSA 公钥
     *
     * @return RSAPublicKey 实例
     */
    private RSAPublicKey getRSAPublicKey() {
        try {
            byte[] keyBytes =Base64.getDecoder().decode(rsaPublicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) factory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("加载RSA公钥失败", e);
        }
    }


    // SM4 密钥长度为 16 字节（128 位）
    private static final int SM4_KEY_SIZE = 16;
    // SM4 初始向量（IV）长度为 16 字节
    private static final int SM4_IV_SIZE = 16;

    /**
     * 将密钥或 IV 截取为前 16 字节
     */
    public static byte[] trimToSM4Size(byte[] input, int size) {
        return Arrays.copyOf(input, size);
    }

    /**
     * SM4 加密，CBC 模式
     *
     * @param plainText 明文字符串
     * @return 返回加密后的密文，使用 Hex 编码
     * @throws Exception 如果出现异常
     */
    public  String encryptSM4CBC(String plainText) throws Exception {
        // 密钥和 IV 截取为前 16 字节
        byte[] sm4KeyByte = trimToSM4Size(sm4Key.getBytes(StandardCharsets.UTF_8), SM4_KEY_SIZE);
        byte[] sm4IVByte = trimToSM4Size(sm4Iv.getBytes(StandardCharsets.UTF_8), SM4_IV_SIZE);

        // 创建 SM4 引擎并设置 CBC 模式
        SM4Engine sm4Engine = new SM4Engine();
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new org.bouncycastle.crypto.modes.CBCBlockCipher(sm4Engine));

        // 设置加密参数（密钥和 IV）
        CipherParameters params = new ParametersWithIV(new KeyParameter(sm4KeyByte), sm4IVByte);
        // true 表示加密模式
        cipher.init(true, params);

        // 将明文转换为字节
        byte[] input = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] output = new byte[cipher.getOutputSize(input.length)];

        // 执行加密
        int len = cipher.processBytes(input, 0, input.length, output, 0);
        len += cipher.doFinal(output, len);

        // 返回 Hex 编码的密文
        return Hex.toHexString(Arrays.copyOf(output, len));
    }

    /**
     * SM4 解密，CBC 模式
     *
     * @param cipherText 密文字符串（Hex 编码）
     * @return 返回解密后的明文
     * @throws Exception 如果出现异常
     */
    public  String decryptSM4CBC(String cipherText) throws Exception {
        // 密钥和 IV 截取为前 16 字节
        byte[] sm4KeyByte = trimToSM4Size(sm4Key.getBytes(StandardCharsets.UTF_8), SM4_KEY_SIZE);
        byte[] sm4IVByte = trimToSM4Size(sm4Iv.getBytes(StandardCharsets.UTF_8), SM4_IV_SIZE);

        // 创建 SM4 引擎并设置 CBC 模式
        SM4Engine sm4Engine = new SM4Engine();
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new org.bouncycastle.crypto.modes.CBCBlockCipher(sm4Engine));

        // 设置解密参数（密钥和 IV）
        CipherParameters params = new ParametersWithIV(new KeyParameter(sm4KeyByte), sm4IVByte);
        // false 表示解密模式
        cipher.init(false, params);

        // 将 Hex 编码的密文转换为字节
        byte[] input = Hex.decode(cipherText);
        byte[] output = new byte[cipher.getOutputSize(input.length)];

        // 执行解密
        int len = cipher.processBytes(input, 0, input.length, output, 0);
        len += cipher.doFinal(output, len);

        // 返回 UTF-8 编码的明文
        return new String(Arrays.copyOf(output, len), StandardCharsets.UTF_8);
    }


}
