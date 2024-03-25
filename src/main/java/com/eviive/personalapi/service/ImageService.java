package com.eviive.personalapi.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.eviive.personalapi.entity.Image;
import com.eviive.personalapi.exception.PersonalApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final BlobServiceClient blobServiceClient;

    @Value("${spring.cloud.azure.storage.blob.container-name.project}")
    private String projectContainer;

    @Value("${spring.cloud.azure.storage.blob.container-name.skill}")
    private String skillContainer;

    public void upload(Image image, UUID oldUuid, MultipartFile file) {
        if (file.isEmpty()) {
            throw new PersonalApiException(API400_FILE_EMPTY);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw PersonalApiException.format(API415_FILE_NOT_IMAGE, file.getContentType());
        }

        if (file.getOriginalFilename() == null) {
            throw new PersonalApiException(API400_IMAGE_NO_NAME);
        }

        BlobClient blobClient = getBlobClient(image);

        try {
            blobClient.upload(file.getInputStream(), file.getSize());

            BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(file.getContentType());

            blobClient.setHttpHeaders(headers);
        } catch (Exception e) {
            throw PersonalApiException.format(e, API500_UPLOAD_ERROR, e.getMessage());
        }

        if (oldUuid != null) {
            delete(image, oldUuid);
        }
    }

    public void delete(Image image) {
        getBlobClient(image).deleteIfExists();

        image.setUuid(null);
    }

    private void delete(Image image, UUID oldUuid) {
        getBlobClient(image, oldUuid).deleteIfExists();
    }

    private BlobContainerClient getBlobContainerClient(Image image) {
        String containerName;
        if (image.getProject() != null) {
            containerName = projectContainer;
        } else if (image.getSkill() != null) {
            containerName = skillContainer;
        } else {
            throw PersonalApiException.format(API500_UNKNOWN_CONTAINER, image.getUuid().toString());
        }

        return blobServiceClient.createBlobContainerIfNotExists(containerName);
    }

    private BlobClient getBlobClient(Image image) {
        return getBlobClient(image, image.getUuid());
    }

    private BlobClient getBlobClient(Image image, UUID uuid) {
        return getBlobContainerClient(image).getBlobClient(uuid.toString());
    }

}
