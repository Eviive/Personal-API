package com.eviive.personalapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum PersonalApiErrorsEnum {

    // 400 Bad Request

    API400_PAGE_NUMBER_NEGATIVE("The page number must be positive (currently %d).", BAD_REQUEST),
    API400_FILE_EMPTY("The received file is empty", BAD_REQUEST),
    API400_IMAGE_NO_NAME("The received image has no name", BAD_REQUEST),

    // 401 Unauthorized

    API401_LOGIN_ERROR("An error occurred while logging you in : %s", UNAUTHORIZED),
    API401_TOKEN_ERROR("An error occurred while processing the token : %s", UNAUTHORIZED),

    // 404 Not Found

    API404_USER_ID_NOT_FOUND("User with id %d not found", NOT_FOUND),
    API404_USERNAME_NOT_FOUND("User with username %s not found", NOT_FOUND),
    API404_ROLE_ID_NOT_FOUND("Role with id %d not found", NOT_FOUND),
    API404_PROJECT_ID_NOT_FOUND("Project with id %d not found", NOT_FOUND),
    API404_SKILL_ID_NOT_FOUND("Skill with id %d not found", NOT_FOUND),
    API404_IMAGE_NOT_FOUND("Image with uuid %s not found", NOT_FOUND),

    // 415 Unsupported Media Type

    API415_FILE_NOT_IMAGE("The received file is not an image (Content-Type: %s)", UNSUPPORTED_MEDIA_TYPE),

    // 500 Internal Server Error

    API500_UPLOAD_ERROR("An error occurred while uploading the image : %s", INTERNAL_SERVER_ERROR),
    API500_IMAGE_NO_PARENT("The image %d is linked to nothing", INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatusCode httpStatusCode;

}
