package com.eviive.personalapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class PersonalApiException extends RuntimeException {

    private final HttpStatusCode httpStatusCode;

    private PersonalApiException(String message, Throwable cause, HttpStatusCode httpStatusCode) {
        super(message, cause);
        this.httpStatusCode = httpStatusCode;
    }

    private PersonalApiException(String message, HttpStatusCode httpStatusCode) {
        this(message, null, httpStatusCode);
    }

    public PersonalApiException(PersonalApiErrorsEnum personalApiErrorsEnum) {
        this(personalApiErrorsEnum.getMessage(), personalApiErrorsEnum.getHttpStatusCode());
    }

    public static PersonalApiException format(PersonalApiErrorsEnum personalApiErrorsEnum, Throwable cause, Object... args) {
        return new PersonalApiException(String.format(personalApiErrorsEnum.getMessage(), args), cause, personalApiErrorsEnum.getHttpStatusCode());
    }

    public static PersonalApiException format(PersonalApiErrorsEnum personalApiErrorsEnum, Object... args) {
        return format(personalApiErrorsEnum, null, args);
    }

}
