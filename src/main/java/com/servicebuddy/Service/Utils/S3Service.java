package com.servicebuddy.Service.Utils;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import com.servicebuddy.Exception.ImageProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        assert fileName != null;
        fileName = uuid.toString() + fileName.substring(fileName.lastIndexOf("."));

        // Upload file to S3
        try (InputStream fileInputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fileInputStream, file.getSize()));

            return getFileUrl(fileName);

        } catch (IOException e) {
            throw new ImageProcessingException("Error in uploading file to AWS: " + e.getMessage());
        }
    }

    public String getFileUrl(String fileName) {
        // Generate a presigned URL or object URL
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
    }

    public void deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}
