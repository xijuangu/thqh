package com.thqh.wechat_miniprogram_backend.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets; // JDK 1.7+ 推荐使用 StandardCharsets
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
/**
 * @ClassName: RsaUtil
 * @Description:
 * @Author liubin
 * @Date 2025/12/2 16:28
 * @Version V1.0
 */
public class RsaUtil {

    private static final Logger logger = LoggerFactory.getLogger(RsaUtil.class);

    /**
     * 加密算法
     */
    private static final String ALGORITHM = "RSA";
    /**
     * 默认转换模式 (算法/模式/填充)
     */
    private static final String DEFAULT_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    /**
     * 安全提供者名称
     */
    private static final String PROVIDER_NAME = BouncyCastleProvider.PROVIDER_NAME;

    // 静态初始化，确保 Bouncy Castle 提供者已注册
    static {
        if (Security.getProvider(PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
            logger.info("Bouncy Castle provider registered successfully.");
        } else {
            logger.debug("Bouncy Castle provider was already registered.");
        }
    }

    /**
     * 使用 Base64 编码的 X.509 公钥字符串加密明文。
     *
     * @param plainText       待加密的原始字符串 (UTF-8)。
     * @param base64PublicKey Base64 编码的 X.509 公钥字符串。
     * @return Base64 编码的加密后密文。
     * @throws IllegalArgumentException 如果输入参数无效。
     * @throws Exception                如果加密过程中发生任何错误 (如密钥格式错误、加密失败)。
     */
    public static String encrypt(String plainText, String base64PublicKey) throws Exception {
        // 1. 输入验证
        if (plainText == null) {
            throw new IllegalArgumentException("Plain text to encrypt cannot be null.");
        }
        if (base64PublicKey == null || base64PublicKey.isEmpty()) {
            throw new IllegalArgumentException("Public key for encryption cannot be null or empty.");
        }

        try {
            // 2. 解码并加载公钥
            byte[] publicKeyBytes = Base64.getDecoder().decode(base64PublicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER_NAME);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // 3. 初始化 Cipher 实例
            Cipher cipher = Cipher.getInstance(DEFAULT_TRANSFORMATION, PROVIDER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // 4. 执行加密并 Base64 编码结果
            byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedBytes = cipher.doFinal(plainBytes);
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (InvalidKeySpecException e) {
            logger.error("Failed to parse provided public key. It might not be in valid X.509 Base64 format.", e);
            throw new Exception("Invalid public key format provided for encryption.", e);
        } catch (InvalidKeyException e) {
            logger.error("The provided public key is invalid or incompatible with the cipher.", e);
            throw new Exception("Invalid public key used for encryption.", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) { // 理论上加密模式下不太可能发生 BadPadding，但以防万一
            logger.error("Encryption operation failed at the cryptographic level.", e);
            throw new Exception("Encryption failed due to an internal cryptographic error.", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during the encryption process.", e);
            throw new Exception("An unexpected error occurred while encrypting the data.", e);
        }
    }

    /**
     * 使用 Base64 编码的 PKCS#8 私钥字符串解密 Base64 编码的密文。
     *
     * @param base64EncryptedData Base64 编码的待解密密文。
     * @param base64PrivateKey    Base64 编码的 PKCS#8 私钥字符串。
     * @return 解密后的原始字符串 (UTF-8)。
     * @throws IllegalArgumentException 如果输入参数无效。
     * @throws Exception                如果解密过程中发生任何错误 (如密钥格式错误、解密失败)。
     */
    public static String decrypt(String base64EncryptedData, String base64PrivateKey) throws Exception {
        // 1. 输入验证
        if (base64EncryptedData == null || base64EncryptedData.isEmpty()) {
            throw new IllegalArgumentException("Encrypted data to decrypt cannot be null or empty.");
        }
        if (base64PrivateKey == null || base64PrivateKey.isEmpty()) {
            throw new IllegalArgumentException("Private key for decryption cannot be null or empty.");
        }

        try {
            // 2. 解码并加载私钥
            byte[] privateKeyBytes = Base64.getDecoder().decode(base64PrivateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER_NAME);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // 3. 初始化 Cipher 实例
            Cipher cipher = Cipher.getInstance(DEFAULT_TRANSFORMATION, PROVIDER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // 4. 解码密文并执行解密
            byte[] encryptedBytes = Base64.getDecoder().decode(base64EncryptedData);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // 5. 返回 UTF-8 字符串
            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (InvalidKeySpecException e) {
            logger.error("Failed to parse provided private key. It might not be in valid PKCS#8 Base64 format.", e);
            throw new Exception("Invalid private key format provided for decryption.", e);
        } catch (InvalidKeyException e) {
            logger.error("The provided private key is invalid or incompatible with the cipher.", e);
            throw new Exception("Invalid private key used for decryption.", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            logger.error("Decryption failed. The data might be corrupted or the key is incorrect.", e);
            throw new Exception("Decryption failed: Invalid ciphertext or incorrect key.", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during the decryption process.", e);
            throw new Exception("An unexpected error occurred while decrypting the data.", e);
        }
    }

    // --- 可选辅助方法：密钥生成与编码 (主要用于测试或初始设置) ---

    /**
     * 生成一个新的 RSA 密钥对 (主要用于测试或生成初始密钥)。
     * 注意：生产环境中应使用专业的密钥管理工具生成和存储密钥。
     *
     * @param keySize 密钥长度，推荐 2048 或更高 (如 4096)。
     * @return 生成的 KeyPair 对象。
     * @throws NoSuchAlgorithmException 如果找不到 RSA 算法。
     * @throws NoSuchProviderException  如果找不到 Bouncy Castle 提供者。
     */
    public static KeyPair generateKeyPair(int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER_NAME);
        keyGen.initialize(keySize, new SecureRandom());
        return keyGen.generateKeyPair();
    }

    /**
     * 将私钥编码为 Base64 字符串 (PKCS#8 格式)。
     *
     * @param privateKey PrivateKey 对象。
     * @return Base64 编码的 PKCS#8 私钥字符串。
     */
    public static String encodePrivateKeyToBase64(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 将公钥编码为 Base64 字符串 (X.509 格式)。
     *
     * @param publicKey PublicKey 对象。
     * @return Base64 编码的 X.509 公钥字符串。
     */
    public static String encodePublicKeyToBase64(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
}