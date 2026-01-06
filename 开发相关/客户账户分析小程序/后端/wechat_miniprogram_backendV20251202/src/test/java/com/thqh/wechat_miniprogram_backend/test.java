package com.thqh.wechat_miniprogram_backend;
import com.thqh.wechat_miniprogram_backend.util.RsaUtil;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @author PY.Lu
 * @date 2025/12/5
 * @Description
 */
public class test {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException {
        RsaUtil rsaUtil = new RsaUtil();
        KeyPair keyPair = rsaUtil.generateKeyPair(2048);
        System.out.println("私钥: " + rsaUtil.encodePrivateKeyToBase64(keyPair.getPrivate()));
        System.out.println("公钥: " + rsaUtil.encodePublicKeyToBase64(keyPair.getPublic()));
    }
}
