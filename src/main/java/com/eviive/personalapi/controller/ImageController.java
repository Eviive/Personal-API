package com.eviive.personalapi.controller;

import com.eviive.personalapi.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.UUID;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
@RequestMapping("image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    // GET

    @GetMapping(path = "{uuid}")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable UUID uuid) {
        Pair<StreamingResponseBody, MediaType> bodyAndMediaType = imageService.download(uuid);

        return ResponseEntity.ok()
                             .header(CONTENT_TYPE, bodyAndMediaType.getSecond().toString())
                             .cacheControl(CacheControl.maxAge(30, DAYS).cachePublic().immutable())
                             .body(bodyAndMediaType.getFirst());
    }

    // DELETE

    @DeleteMapping(path = "{uuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID uuid) {
        imageService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

}
