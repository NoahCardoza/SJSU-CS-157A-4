package com.example.demo;

import jakarta.servlet.http.Part;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Paths;

public class S3 {
    public static String uploadFile(String bucketName, String key, RequestBody body) {
        S3Client s3 = S3Client.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.US_EAST_2)
                .build();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3.putObject(objectRequest, body);

        return s3.utilities().getUrl(builder -> builder.bucket(bucketName).key(key).build()).toString();
    }

    public static String uploadFile(String bucketName, String key, String filePath) {
        return uploadFile(bucketName, key, RequestBody.fromFile(Paths.get(filePath)));
    }
    public static String uploadFile(String bucketName, String key, Part part) throws IOException {
        return uploadFile(bucketName, key, RequestBody.fromInputStream(part.getInputStream(), part.getSize()));
    }
}
