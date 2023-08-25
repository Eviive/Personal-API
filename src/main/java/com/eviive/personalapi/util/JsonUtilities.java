package com.eviive.personalapi.util;

import com.eviive.personalapi.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public final class JsonUtilities {

    public ErrorResponseDTO buildError(HttpStatusCode httpStatusCode, Object message) {
        ErrorResponseDTO responseBody = new ErrorResponseDTO();

        responseBody.setStatus(httpStatusCode.value());

        if (httpStatusCode instanceof HttpStatus httpStatus) {
            responseBody.setError(httpStatus.getReasonPhrase());
        }

        responseBody.setMessage(message);
        return responseBody;
    }

}
