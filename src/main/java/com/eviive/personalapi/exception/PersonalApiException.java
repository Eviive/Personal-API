package com.eviive.personalapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class PersonalApiException extends RuntimeException {

    private final HttpStatusCode httpStatusCode;

    private PersonalApiException(Throwable cause, String message, HttpStatusCode httpStatusCode) {
        super(message, cause);
        this.httpStatusCode = httpStatusCode;
    }

    private PersonalApiException(String message, HttpStatusCode httpStatusCode) {
        this(null, message, httpStatusCode);
    }

    public PersonalApiException(PersonalApiErrorsEnum personalApiErrorsEnum) {
        this(personalApiErrorsEnum.getMessage(), personalApiErrorsEnum.getHttpStatusCode());
    }

    public static PersonalApiException format(Throwable cause, PersonalApiErrorsEnum personalApiErrorsEnum, Object... args) {
        return new PersonalApiException(cause, personalApiErrorsEnum.getMessage().formatted(args), personalApiErrorsEnum.getHttpStatusCode());
    }

    public static PersonalApiException format(PersonalApiErrorsEnum personalApiErrorsEnum, Object... args) {
        return format(null, personalApiErrorsEnum, args);
    }

}
