package org.example.processserver.Utils;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SM4Util {

    private static final int SM4_KEY_SIZE = 16;  // SM4 密钥长度为 16 字节（128 位）
    private static final int SM4_IV_SIZE = 16;   // SM4 初始向量（IV）长度为 16 字节

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
     * @param key       密钥（32 字节，将截取为前 16 字节）
     * @param iv        初始向量（IV，32 字节，将截取为前 16 字节）
     * @return 返回加密后的密文，使用 Hex 编码
     * @throws Exception 如果出现异常
     */
    public static String encryptCBC(String plainText, byte[] key, byte[] iv) throws Exception {
        // 密钥和 IV 截取为前 16 字节
        byte[] sm4Key = trimToSM4Size(key, SM4_KEY_SIZE);
        byte[] sm4IV = trimToSM4Size(iv, SM4_IV_SIZE);

        // 创建 SM4 引擎并设置 CBC 模式
        SM4Engine sm4Engine = new SM4Engine();
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new org.bouncycastle.crypto.modes.CBCBlockCipher(sm4Engine));

        // 设置加密参数（密钥和 IV）
        CipherParameters params = new ParametersWithIV(new KeyParameter(sm4Key), sm4IV);
        cipher.init(true, params);  // true 表示加密模式

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
     * @param key        密钥（32 字节，将截取为前 16 字节）
     * @param iv         初始向量（IV，32 字节，将截取为前 16 字节）
     * @return 返回解密后的明文
     * @throws Exception 如果出现异常
     */
    public static String decryptCBC(String cipherText, byte[] key, byte[] iv) throws Exception {
        // 密钥和 IV 截取为前 16 字节
        byte[] sm4Key = trimToSM4Size(key, SM4_KEY_SIZE);
        byte[] sm4IV = trimToSM4Size(iv, SM4_IV_SIZE);

        // 创建 SM4 引擎并设置 CBC 模式
        SM4Engine sm4Engine = new SM4Engine();
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new org.bouncycastle.crypto.modes.CBCBlockCipher(sm4Engine));

        // 设置解密参数（密钥和 IV）
        CipherParameters params = new ParametersWithIV(new KeyParameter(sm4Key), sm4IV);
        cipher.init(false, params);  // false 表示解密模式

        // 将 Hex 编码的密文转换为字节
        byte[] input = Hex.decode(cipherText);
        byte[] output = new byte[cipher.getOutputSize(input.length)];

        // 执行解密
        int len = cipher.processBytes(input, 0, input.length, output, 0);
        len += cipher.doFinal(output, len);

        // 返回 UTF-8 编码的明文
        return new String(Arrays.copyOf(output, len), StandardCharsets.UTF_8);
    }

    //加密方法
    public static String cipherTextCBC(String plainText) throws Exception {
        byte[] key = "5N62l5rzh5GCf9YA".getBytes(StandardCharsets.UTF_8);  // 32 字节密钥
        byte[] iv = "JhXMkLNeQ66Yw1wn".getBytes(StandardCharsets.UTF_8);    // 32 字节 IV

        String cipherTextCBC = encryptCBC(plainText, key, iv);

        return cipherTextCBC;

    }

    //解密方法
    public static String decryptTextCBC(String encryptText) throws Exception {
        byte[] key = "5N62l5rzh5GCf9YA".getBytes(StandardCharsets.UTF_8);  // 32 字节密钥
        byte[] iv = "JhXMkLNeQ66Yw1wn".getBytes(StandardCharsets.UTF_8);    // 32 字节 IV

        //测试环境
//        byte[] key = "Cx7NucwUD7sSU2wg".getBytes(StandardCharsets.UTF_8);  // 32 字节密钥
//        byte[] iv = "E1bW0fpK35VLSOsJ".getBytes(StandardCharsets.UTF_8);    // 32 字节 IV

        String decryptTextCBC = decryptCBC(encryptText, key, iv);

        return decryptTextCBC;

    }
}
