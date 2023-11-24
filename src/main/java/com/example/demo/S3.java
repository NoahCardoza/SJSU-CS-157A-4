package com.example.demo;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;

public class S3 {
    public static void main(String[] args) {
        uploadFile("little-hidden-gems", "test.txt", "/Users/Vulcan/Downloads/locations.jpg");
    }

    public static String uploadFile(String bucketName, String key, String filePath) {
        S3Client s3 = S3Client.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.US_EAST_2)
                .build();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3.putObject(objectRequest, RequestBody.fromFile(Paths.get(filePath)));

        return s3.utilities().getUrl(builder -> builder.bucket(bucketName).key(key).build()).toString();
    }
}
