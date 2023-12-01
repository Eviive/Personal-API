package com.eviive.personalapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PersonalApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    private PersonalApiException(Throwable cause, String message, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    private PersonalApiException(String message, HttpStatus httpStatus) {
        this(null, message, httpStatus);
    }

    public PersonalApiException(PersonalApiErrorsEnum personalApiErrorsEnum) {
        this(personalApiErrorsEnum.getMessage(), personalApiErrorsEnum.getHttpStatus());
    }

    // Formatted messages

    public static PersonalApiException format(Throwable cause, PersonalApiErrorsEnum personalApiErrorsEnum, Object... args) {
        return new PersonalApiException(cause, personalApiErrorsEnum.getMessage().formatted(args), personalApiErrorsEnum.getHttpStatus());
    }

    public static PersonalApiException format(PersonalApiErrorsEnum personalApiErrorsEnum, Object... args) {
        return format(null, personalApiErrorsEnum, args);
    }

}
