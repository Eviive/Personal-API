package com.eviive.personalapi.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public final class JsonUtilities {

    public Map<String, Object> generateErrorBody(HttpStatusCode status, Object message) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", status.value());
        if (status instanceof HttpStatus) {
            body.put("error", ((HttpStatus) status).getReasonPhrase());
        }
        body.put("message", message);

        return body;
    }

}
