package com.eviive.personalapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class PersonalApiException extends RuntimeException {

    private final HttpStatusCode httpStatusCode;

    private PersonalApiException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public PersonalApiException(PersonalApiErrorsEnum personalApiErrorsEnum) {
        super(personalApiErrorsEnum.getMessage());
        this.httpStatusCode = personalApiErrorsEnum.getHttpStatusCode();
    }

    public static PersonalApiException format(PersonalApiErrorsEnum personalApiErrorsEnum, Object... args) {
        return new PersonalApiException(String.format(personalApiErrorsEnum.getMessage(), args), personalApiErrorsEnum.getHttpStatusCode());
    }

}
