package com.eviive.personalapi.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public final class UriUtils {

    public URI buildLocation(Long id, String pathToRemove) {
        String path = ServletUriComponentsBuilder.fromCurrentRequest()
                                                 .path("/{id}")
                                                 .buildAndExpand(id)
                                                 .getPath();

        if (path != null && pathToRemove != null) {
            pathToRemove = pathToRemove.startsWith("/") ? pathToRemove : "/%s".formatted(pathToRemove);
            path = path.replace(pathToRemove, "");
        }

        return URI.create(path != null ? path : "");
    }

    public URI buildLocation(Long id) {
        return buildLocation(id, null);
    }

}
