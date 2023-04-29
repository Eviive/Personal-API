package com.eviive.personalapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum PersonalApiErrorsEnum {

    // 400 Bad Request

    API400_PAGE_NUMBER_NEGATIVE("The page number must be positive (currently %d).", BAD_REQUEST),

    // 401 Unauthorized

    API401_LOGIN_ERROR("An error occurred while logging you in : %s", UNAUTHORIZED),
    API401_TOKEN_ERROR("An error occurred while processing the token : %s", UNAUTHORIZED),

    // 404 Not Found

    API404_USER_ID_NOT_FOUND("User with id %d not found", NOT_FOUND),
    API404_USERNAME_NOT_FOUND("User with username %s not found", NOT_FOUND),
    API404_ROLE_ID_NOT_FOUND("Role with id %d not found", NOT_FOUND),
    API404_PROJECT_ID_NOT_FOUND("Project with id %d not found", NOT_FOUND),
    API404_SKILL_ID_NOT_FOUND("Skill with id %d not found", NOT_FOUND);

    private final String message;
    private final HttpStatusCode httpStatusCode;

}
