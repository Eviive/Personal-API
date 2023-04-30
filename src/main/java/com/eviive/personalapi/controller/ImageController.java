package com.eviive.personalapi.controller;

import com.eviive.personalapi.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
@RequestMapping("image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    // GET

    @GetMapping(path = "{id}/download")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable Long id) {
        Pair<StreamingResponseBody, MediaType> responseBodyAndMediaType = imageService.download(id);

        return ResponseEntity.ok()
                             .header(CONTENT_TYPE, responseBodyAndMediaType.getSecond().toString())
                             .body(responseBodyAndMediaType.getFirst());
    }

    // DELETE

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
