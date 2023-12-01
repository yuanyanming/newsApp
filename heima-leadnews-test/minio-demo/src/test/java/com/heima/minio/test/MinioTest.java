package com.heima.minio.test;

import com.heima.file.service.FileStorageService;
import com.heima.minio.MinIOApplication;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

//@SpringBootTest(classes = MinIOApplication.class)
//@RunWith(SpringRunner.class)
public class MinioTest {

//    @Autowired
//    private FileStorageService fileStorageService;
//    @Test
//    public void test() throws FileNotFoundException {
//        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\84799\\Documents\\list.html");
//        String path = fileStorageService.uploadHtmlFile("", "list.html", fileInputStream);
//        System.out.println(path);
//    }

    public static void main(String[] args) {

        try {
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream("C:\\Users\\84799\\Documents\\tmp\\js\\index.js");

            //1.创建minio链接客户端
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minio", "minio123")
                    .endpoint("http://192.168.14.129:9000")
                    .build();
            //2.上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("plugins/js/index.js")//文件名
                    .contentType("text/js")//文件类型
                    .bucket("leadnews")//桶名词  与minio创建的名词一致
                    .stream(fileInputStream, fileInputStream.available(), -1)
                    .build();

            minioClient.putObject(putObjectArgs);

//            System.out.println("http://192.168.14.129:9000/leadnews/list.html");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
