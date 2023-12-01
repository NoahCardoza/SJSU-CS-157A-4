package com.example.demo;

import jakarta.servlet.http.Part;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class S3 {

    private static class S3ObjectProps {
        private String bucketName;
        private String key;

        static public S3ObjectProps fromUrl(String url) {
            String[] parts = url.substring(url.indexOf(':') + 3).split("/");
            String bucketName = parts[0].split("\\.", 2)[0];
            String key = parts[1];

            return new S3ObjectProps(bucketName, key);
        }

        public S3ObjectProps(String bucketName, String key) {
            this.bucketName = bucketName;
            this.key = key;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "S3ObjectProps{" +
                    "bucketName='" + bucketName + '\'' +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

    static S3Client s3 = S3Client.builder()
            .credentialsProvider(DefaultCredentialsProvider.create())
            .region(Region.US_EAST_2)
            .build();

    public static String uploadFile(String bucketName, String key, RequestBody body) {
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

    public static void deleteFiles(List<String> urls) {
        urls.stream()
            .map(S3ObjectProps::fromUrl)
            .collect(Collectors.groupingBy(S3ObjectProps::getBucketName))
            .forEach((bucketName, props) -> {
                s3.deleteObjects(builder -> builder.bucket(bucketName).delete(builder2 -> builder2.objects(
                        props.stream()
                                .map(S3ObjectProps::getKey)
                                .map(key -> ObjectIdentifier.builder().key(key).build())
                                .toList()
                )
                        .build()
                ).build());
            });
    }
}
