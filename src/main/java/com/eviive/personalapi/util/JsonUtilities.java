package com.eviive.personalapi.util;

import com.eviive.personalapi.dto.ErrorResponseDTO;
import com.eviive.personalapi.exception.PersonalApiErrorsEnum;
import com.eviive.personalapi.exception.PersonalApiException;
import org.springframework.core.NestedRuntimeException;
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

    public ErrorResponseDTO buildError(PersonalApiErrorsEnum personalApiErrorsEnum) {
        return this.buildError(personalApiErrorsEnum.getHttpStatusCode(), personalApiErrorsEnum.getMessage());
    }

    public ErrorResponseDTO buildError(PersonalApiErrorsEnum personalApiErrorsEnum, NestedRuntimeException nestedRuntimeException) {
        return this.buildError(
                PersonalApiException.format(
                        nestedRuntimeException,
                        personalApiErrorsEnum,
                        nestedRuntimeException.getMostSpecificCause().getMessage()
                )
        );
    }

    public ErrorResponseDTO buildError(PersonalApiException personalApiException) {
        return this.buildError(personalApiException.getHttpStatusCode(), personalApiException.getMessage());
    }

}
