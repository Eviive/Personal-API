package com.eviive.personalapi.util;

import com.eviive.personalapi.dto.ErrorResponseDTO;
import com.eviive.personalapi.exception.PersonalApiErrorsEnum;
import com.eviive.personalapi.exception.PersonalApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
public final class ErrorUtils {

    public <E> ErrorResponseDTO<E> buildError(final HttpStatus httpStatus, final E message) {
        return new ErrorResponseDTO<>(httpStatus.value(), httpStatus.getReasonPhrase(), message);
    }

    public ErrorResponseDTO<String> buildError(
        final PersonalApiErrorsEnum personalApiErrorsEnum,
        final Object... args
    ) {
        return this.buildError(
            personalApiErrorsEnum.getHttpStatus(),
            personalApiErrorsEnum.getMessage().formatted(args)
        );
    }

    public ErrorResponseDTO<String> buildError(final PersonalApiException personalApiException) {
        return this.buildError(
            personalApiException.getHttpStatus(),
            personalApiException.getMessage()
        );
    }

    public ErrorResponseDTO<String> buildError(
        final HttpStatusCode httpStatusCode,
        final ProblemDetail problemDetail
    ) {
        return this.buildError(
            HttpStatus.valueOf(httpStatusCode.value()),
            problemDetail.getDetail()
        );
    }

}
