package com.thqh.enterprise_wechat_backend.util;

import io.minio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: MinioUtil
 * @Description:
 * @Author liubin
 * @Date 2025/3/11 11:16
 * @Version V1.0
 */
@Component
public class MinioUtil {

    @Value("${minio.bucket}")
    private String bucketName;

    private final MinioClient minioClient;

    private static final Logger logger = LoggerFactory.getLogger(MinioUtil.class);

    public MinioUtil(MinioClient minioClient) {
        this.minioClient = minioClient;
    }


    /**
     * 上传文件到minio
     * @param localFilePath
     * @return minio保存路径
     * @throws Exception
     */
    public String uploadFile(String localFilePath) throws Exception {
         /*
         如果bucket不存在，可以在此检查/创建
         boolean found =
             minioClient.bucketExists(BucketExistsArgs.builder().bucket("test").build());
         if (!found) {
             minioClient.makeBucket(MakeBucketArgs.builder().bucket("test").build());
         }
          */
        // 原始文件名
        String originalFilename = new File(localFilePath).getName();
        // 加个日期目录
        String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        // 合并为: 2025/03/10/uuid_originalFilename
        String objectName = datePath + "/" + originalFilename;

        FileInputStream fis = new FileInputStream(localFilePath);
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(fis, fis.available(), -1)
                        .contentType("application/octet-stream")
                        .build());
        // 返回可访问URL或对象名
        return  "/" + bucketName + "/" + objectName;
    }


    /**
     * 下载 MinIO 文件为字节数组
     * @param objectPath 对象路径（包含桶名，形如 /thqh-enterprise-wechat/2025/03/11/6e358ba0d3f848189930d0e9e8fd7e9e_1741679583404.amr）
     * @return 文件数据
     */
    public byte[] downloadFile(String objectPath) throws Exception {
        String prefix = "/" + bucketName + "/";
        if (objectPath.startsWith(prefix)) {
            objectPath = objectPath.substring(prefix.length());
        }
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectPath)
                        .build()
        );
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            byte[] data = new byte[1024];
            int nRead;
            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        }
    }
}
