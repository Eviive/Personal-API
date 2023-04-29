package com.eviive.personalapi.exception;

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
import java.util.Map;

import static com.eviive.personalapi.util.JsonUtilities.generateErrorBody;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception e, Object body, @NonNull HttpHeaders headers, @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {
		Map<String, Object> responseBody = generateErrorBody(statusCode, e.getMessage().split(":")[0]);

		return ResponseEntity.status(statusCode).body(responseBody);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, @NonNull HttpHeaders headers, @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {
		List<String> validationErrors = e.getBindingResult()
										 .getFieldErrors()
										 .stream()
										 .map(DefaultMessageSourceResolvable::getDefaultMessage)
										 .toList();

		Map<String, Object> body = generateErrorBody(BAD_REQUEST, validationErrors);

		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(PersonalApiException.class)
	protected ResponseEntity<Object> handlePersonalApiException(PersonalApiException e) {
		Map<String, Object> responseBody = generateErrorBody(e.getHttpStatusCode(), e.getMessage());

		return ResponseEntity.status(e.getHttpStatusCode()).body(responseBody);
	}

}
