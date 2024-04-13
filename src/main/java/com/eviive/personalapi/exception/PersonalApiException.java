package com.eviive.personalapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PersonalApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    private PersonalApiException(final Throwable cause, final String message, final HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    private PersonalApiException(final String message, final HttpStatus httpStatus) {
        this(null, message, httpStatus);
    }

    public PersonalApiException(final PersonalApiErrorsEnum personalApiErrorsEnum) {
        this(personalApiErrorsEnum.getMessage(), personalApiErrorsEnum.getHttpStatus());
    }

    // Formatted messages

    public static PersonalApiException format(
        final Throwable cause,
        final PersonalApiErrorsEnum personalApiErrorsEnum,
        final Object... args
    ) {
        return new PersonalApiException(
            cause,
            personalApiErrorsEnum.getMessage().formatted(args),
            personalApiErrorsEnum.getHttpStatus()
        );
    }

    public static PersonalApiException format(
        final PersonalApiErrorsEnum personalApiErrorsEnum,
        final Object... args
    ) {
        return format(null, personalApiErrorsEnum, args);
    }

}
