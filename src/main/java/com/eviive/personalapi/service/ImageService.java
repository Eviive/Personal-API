package com.eviive.personalapi.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.eviive.personalapi.entity.Image;
import com.eviive.personalapi.entity.Project;
import com.eviive.personalapi.entity.Skill;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.repository.ImageRepository;
import com.eviive.personalapi.util.StreamUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.UUID;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final BlobServiceClient blobServiceClient;

    private final StreamUtils streamUtils;

    public void upload(Image image, String containerName, MultipartFile file) {
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

        BlobContainerClient containerClient = blobServiceClient.createBlobContainerIfNotExists(containerName);

        UUID uuid = UUID.randomUUID();

        BlobClient blobClient = containerClient.getBlobClient(uuid.toString());

        try {
            blobClient.upload(file.getInputStream(), file.getSize());

            BlobHttpHeaders headers = new BlobHttpHeaders();
            headers.setContentType(file.getContentType());

            blobClient.setHttpHeaders(headers);
        } catch (Exception e) {
            throw PersonalApiException.format(e, API500_UPLOAD_ERROR, e.getMessage());
        }

        if (image.getUuid() != null) {
            delete(image, containerName);
        }

        image.setUuid(uuid);
    }

    public Pair<StreamingResponseBody, MediaType> download(UUID uuid) {
        Image image = imageRepository.findByUuid(uuid)
                                     .orElseThrow(() -> PersonalApiException.format(API404_IMAGE_NOT_FOUND, uuid));

        BlobClient blobClient = getBlobClient(image);

        StreamingResponseBody streamingResponseBody = outputStream -> {
            try {
                streamUtils.transferTo(blobClient.openInputStream(), outputStream);
            } catch (IOException e) {
                throw PersonalApiException.format(e, API500_DOWNLOAD_ERROR, e.getMessage());
            }
        };

        MediaType mediaType = MediaType.parseMediaType(blobClient.getProperties().getContentType());

        return Pair.of(streamingResponseBody, mediaType);
    }

    public void delete(UUID uuid) {
        Image image = imageRepository.findByUuid(uuid)
                                     .orElseThrow(() -> PersonalApiException.format(API404_IMAGE_NOT_FOUND, uuid));

        delete(image);

        imageRepository.save(image);
    }

    private void delete(Image image) {
        getBlobClient(image).deleteIfExists();

        image.setUuid(null);
    }

    public void delete(Image image, String containerName) {
        getBlobClient(image, containerName).deleteIfExists();

        image.setUuid(null);
    }

    private BlobClient getBlobClient(Image image) {
        String containerName;
        if (image.getProject() != null) {
            containerName = Project.AZURE_CONTAINER_NAME;
        } else if (image.getSkill() != null) {
            containerName = Skill.AZURE_CONTAINER_NAME;
        } else {
            throw PersonalApiException.format(API500_IMAGE_NO_PARENT, image.getUuid().toString());
        }

        return getBlobClient(image, containerName);
    }

    private BlobClient getBlobClient(Image image, String containerName) {
        String fileName = image.getUuid().toString();

        return blobServiceClient.getBlobContainerClient(containerName).getBlobClient(fileName);
    }

}
