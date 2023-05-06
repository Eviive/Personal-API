package com.eviive.personalapi.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.eviive.personalapi.entity.Image;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.UUID;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.*;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final BlobServiceClient blobServiceClient;

    public Image upload(Image image, String containerName, MultipartFile file) {
        if (file.isEmpty()) {
            throw new PersonalApiException(API400_FILE_EMPTY);
        }

        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
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
            throw PersonalApiException.format(API500_UPLOAD_ERROR, e.getMessage());
        }

        if (image.getUuid() != null) {
            delete(image);
        }

        image.setUuid(uuid);

        return image;
    }

    public Pair<StreamingResponseBody, MediaType> download(UUID uuid) {
        Image image = imageRepository.findByUuid(uuid)
                                     .orElseThrow(() -> PersonalApiException.format(API404_IMAGE_NOT_FOUND, uuid));

        BlobClient blobClient = getBlobClient(image);

        StreamingResponseBody responseBody = outputStream -> {
            blobClient.downloadStream(outputStream);
            outputStream.flush();
        };

        MediaType mediaType = MediaType.parseMediaType(blobClient.getProperties().getContentType());

        return Pair.of(responseBody, mediaType);
    }

    public void delete(UUID uuid) {
        Image image = imageRepository.findByUuid(uuid)
                                     .orElseThrow(() -> PersonalApiException.format(API404_IMAGE_NOT_FOUND, uuid));

        delete(image);

        imageRepository.save(image);
    }

    private void delete(Image image) {
        getBlobClient(image).delete();

        image.setUuid(null);
    }

    private BlobClient getBlobClient(Image image) {
        String containerName;
        if (image.getProject() != null) {
            containerName = "project";
        } else if (image.getSkill() != null) {
            containerName = "skill";
        } else {
            throw PersonalApiException.format(API500_IMAGE_NO_PARENT, image.getUuid().toString());
        }
        String fileName = image.getUuid().toString();

        return blobServiceClient.getBlobContainerClient(containerName + "-images").getBlobClient(fileName);
    }

}
