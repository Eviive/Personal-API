package com.eviive.personalapi.exception;

import com.eviive.personalapi.dto.ErrorResponseDTO;
import com.eviive.personalapi.util.JsonUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    private final JsonUtilities jsonUtilities;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception e, Object body, @NonNull HttpHeaders headers, @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {
        String message = e.getMessage() != null
                ? e.getMessage().split(":")[0]
                : e.getClass().getSimpleName();

        ErrorResponseDTO responseBody = jsonUtilities.generateErrorBody(statusCode, message);

        return ResponseEntity.status(statusCode)
                             .contentType(APPLICATION_JSON)
                             .body(responseBody);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, @NonNull HttpHeaders headers, @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {
        List<String> validationErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ErrorResponseDTO responseBody = jsonUtilities.generateErrorBody(BAD_REQUEST, validationErrors);

        return ResponseEntity.badRequest()
                             .contentType(APPLICATION_JSON)
                             .body(responseBody);
    }

    @ExceptionHandler(PersonalApiException.class)
    protected ResponseEntity<ErrorResponseDTO> handlePersonalApiException(PersonalApiException e) {
        ErrorResponseDTO responseBody = jsonUtilities.generateErrorBody(e.getHttpStatusCode(), e.getMessage());

        return ResponseEntity.status(e.getHttpStatusCode())
                             .contentType(APPLICATION_JSON)
                             .body(responseBody);
    }

}
