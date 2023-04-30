package com.eviive.personalapi.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.eviive.personalapi.dto.ImageDTO;
import com.eviive.personalapi.entity.Image;
import com.eviive.personalapi.exception.PersonalApiException;
import com.eviive.personalapi.mapper.ImageMapper;
import com.eviive.personalapi.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.net.URI;
import java.util.UUID;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.*;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    private final BlobServiceClient blobServiceClient;

    public ImageDTO upload(String containerName, MultipartFile file, String alt) {
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
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String fileName = String.format("%s.%s", uuid, extension);

        BlobClient blobClient = containerClient.getBlobClient(fileName);

        try {
            blobClient.upload(file.getInputStream(), file.getSize());

            BlobHttpHeaders headers = new BlobHttpHeaders();
            headers.setContentType(file.getContentType());

            blobClient.setHttpHeaders(headers);
        } catch (Exception e) {
            throw PersonalApiException.format(API500_UPLOAD_ERROR, e.getMessage());
        }

        URI uri = URI.create(String.format("%s/%s", containerName, fileName));

        Image image = new Image();
        image.setUri(uri.toString());
        image.setAlt(alt);

        return imageMapper.toDTO(imageRepository.save(image));
    }

    public void delete(Long id) {
        Image image = imageRepository.findById(id)
                                     .orElseThrow(() -> PersonalApiException.format(API404_IMAGE_NOT_FOUND, id));

        getBlobClient(image).delete();

        image.setUri(null);
    }

    public Pair<StreamingResponseBody, MediaType> download(Long id) {
        Image image = imageRepository.findById(id)
                                     .orElseThrow(() -> PersonalApiException.format(API404_IMAGE_NOT_FOUND, id));

        BlobClient blobClient = getBlobClient(image);

        StreamingResponseBody responseBody = outputStream -> {
            blobClient.downloadStream(outputStream);
            outputStream.flush();
        };

        MediaType mediaType = MediaType.parseMediaType(blobClient.getProperties().getContentType());

        return Pair.of(responseBody, mediaType);
    }

    private BlobClient getBlobClient(Image image) {
        String containerName = image.getUri().substring(0, image.getUri().indexOf("/"));
        String fileName = image.getUri().substring(image.getUri().indexOf("/") + 1);

        return blobServiceClient.getBlobContainerClient(containerName).getBlobClient(fileName);
    }

}
