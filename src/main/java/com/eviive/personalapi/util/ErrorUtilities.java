package com.eviive.personalapi.util;

import com.eviive.personalapi.dto.ErrorResponseDTO;
import com.eviive.personalapi.exception.PersonalApiErrorsEnum;
import com.eviive.personalapi.exception.PersonalApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public final class ErrorUtilities {

    public ErrorResponseDTO buildError(HttpStatusCode httpStatusCode, Object message) {
        ErrorResponseDTO responseBody = new ErrorResponseDTO();

        responseBody.setStatus(httpStatusCode.value());

        if (httpStatusCode instanceof HttpStatus httpStatus) {
            responseBody.setError(httpStatus.getReasonPhrase());
        }

        responseBody.setMessage(message);
        return responseBody;
    }

    public ErrorResponseDTO buildError(PersonalApiErrorsEnum personalApiErrorsEnum, Object... args) {
        return this.buildError(personalApiErrorsEnum.getHttpStatusCode(), personalApiErrorsEnum.getMessage().formatted(args));
    }

    public ErrorResponseDTO buildError(PersonalApiException personalApiException) {
        return this.buildError(personalApiException.getHttpStatusCode(), personalApiException.getMessage());
    }

}
