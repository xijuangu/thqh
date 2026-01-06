package com.thqh.wechat_miniprogram_backend.util;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 临时工具类，用于生成 RSA 密钥对并打印为 application.properties 格式。
 * 生成后，请将密钥安全地存储，并删除此类或移至非生产环境。
 * <p>
 * 运行方式:
 * 1. 确保 RsaUtil.java 已编译并可用。
 * 2. 运行此类的 main 方法。
 * 3. 控制台将输出可用于 application.properties 的配置项。
 */
public class KeyGenerator {

    // 推荐的密钥大小
    private static final int KEY_SIZE = 2048; // 或 4096

    public static void main(String[] args) {
        try {
            System.out.println("正在生成 " + KEY_SIZE + " 位 RSA 密钥对...");

            // 1. 生成密钥对
            KeyPair keyPair = RsaUtil.generateKeyPair(KEY_SIZE);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 2. 使用 RsaUtil 的辅助方法编码为 Base64
            String base64PublicKey = RsaUtil.encodePublicKeyToBase64(publicKey);
            String base64PrivateKey = RsaUtil.encodePrivateKeyToBase64(privateKey);

            // 3. 输出到控制台，格式化以便直接复制到 application.properties
            System.out.println("\n--- 生成的 RSA 密钥对 (请复制到 application.properties) ---");
            // 注意：这里使用了 myapp 作为前缀，您应该根据实际情况修改为您自己的前缀
            System.out.println("# RSA 公钥 (用于前端加密)");
            System.out.println("myapp.rsa.public-key=" + base64PublicKey);
            System.out.println(); // 空行分隔
            System.out.println("# RSA 私钥 (用于后端解密) - *** 极其重要，请妥善保管!!! ***");
            System.out.println("myapp.rsa.private-key=" + base64PrivateKey);
            System.out.println("--- 密钥生成完成 ---\n");

            // 4. （可选）打印密钥长度信息
            System.out.println("公钥长度 (字节): " + publicKey.getEncoded().length);
            System.out.println("私钥长度 (字节): " + privateKey.getEncoded().length);

        } catch (Exception e) {
            System.err.println("生成密钥对时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}