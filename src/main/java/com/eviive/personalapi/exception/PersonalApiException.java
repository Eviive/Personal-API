package com.eviive.personalapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class PersonalApiException extends RuntimeException {

    private final HttpStatusCode httpStatusCode;

    public PersonalApiException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public static PersonalApiException format(PersonalApiErrorsEnum personalApiErrorsEnum, Object... args) {
        return new PersonalApiException(String.format(personalApiErrorsEnum.getMessage(), args), personalApiErrorsEnum.getHttpStatusCode());
    }

}
