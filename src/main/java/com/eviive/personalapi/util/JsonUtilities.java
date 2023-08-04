package com.eviive.personalapi.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public final class JsonUtilities {

    public Map<String, Object> generateErrorBody(HttpStatusCode httpStatusCode, Object message) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", httpStatusCode.value());
        if (httpStatusCode instanceof HttpStatus httpStatus) {
            body.put("error", httpStatus.getReasonPhrase());
        }
        body.put("message", message);

        return body;
    }

}
