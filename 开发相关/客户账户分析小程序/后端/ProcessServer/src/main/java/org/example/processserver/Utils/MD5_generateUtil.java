package org.example.processserver.Utils;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author PY.Lu
 * @date 2024/10/25
 * @Description
 */
public class MD5_generateUtil {
    public static String generateMD5(String filePath) {
        String md5 = computeMD5(new File(filePath));
//        System.out.println(md5);
        return md5;
    }

    private static String computeMD5(File file) {
        DigestInputStream din = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //第一个参数是一个输入流
            din = new DigestInputStream(new BufferedInputStream(new FileInputStream(file)), md5);

            byte[] b = new byte[1024];
            while (din.read(b) != -1);

            byte[] digest = md5.digest();

            StringBuilder result = new StringBuilder(DatatypeConverter.printHexBinary(digest));
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (din != null) {
                    din.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
