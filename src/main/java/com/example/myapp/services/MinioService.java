package com.example.myapp.services;

import io.minio.*;
import com.example.myapp.configs.MinioProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties props;

    public String uploadAvatar(MultipartFile file) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File rỗng");
        }

        if (file.getContentType() == null ||
                !file.getContentType().startsWith("image/")) {
            throw new RuntimeException("File phải là ảnh");
        }

        String objectName =
                "avatars/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        ensureBucketExists();

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(props.getBucket())
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return props.getUrl() + "/" + props.getBucket() + "/" + objectName;
    }

    private void ensureBucketExists() throws Exception {
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(props.getBucket())
                        .build()
        );

        if (!exists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(props.getBucket())
                            .build()
            );
        }
    }

}