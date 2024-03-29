package com.eviive.personalapi.exception;

import com.eviive.personalapi.dto.ErrorResponseDTO;
import com.eviive.personalapi.util.ErrorUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.List;

import static com.eviive.personalapi.exception.PersonalApiErrorsEnum.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestControllerAdvice
@RequiredArgsConstructor
public class PersonalApiExceptionHandler extends ResponseEntityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ErrorUtilities errorUtilities;

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) {
         sendError(res, API401_UNAUTHORIZED);
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException accessDeniedException) {
         sendError(res, API403_FORBIDDEN);
    }

    private void sendError(HttpServletResponse res, PersonalApiErrorsEnum personalApiErrorsEnum) {
        ErrorResponseDTO errorResponse = errorUtilities.buildError(personalApiErrorsEnum);

        res.setStatus(errorResponse.getStatus());
        res.setContentType(APPLICATION_JSON_VALUE);
        try {
            res.getOutputStream().print(objectMapper.writeValueAsString(errorResponse));
            res.flushBuffer();
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception e, WebRequest req) {
        ErrorResponseDTO errorResponse;
        boolean defaultExceptionHandler = false;
        boolean logException = true;

        if (e instanceof PersonalApiException personalApiException) {
            errorResponse = errorUtilities.buildError(personalApiException);

        } else if (e instanceof JpaSystemException jpaSystemException) {
            errorResponse = errorUtilities.buildError(API500_INTERNAL_SERVER_ERROR, jpaSystemException.getMostSpecificCause().getMessage());

        } else if (e instanceof TransactionSystemException transactionSystemException) {
            errorResponse = errorUtilities.buildError(API500_INTERNAL_SERVER_ERROR, transactionSystemException.getMostSpecificCause().getMessage());

        } else if (e instanceof NestedRuntimeException nestedRuntimeException) {
            errorResponse = errorUtilities.buildError(API500_INTERNAL_SERVER_ERROR, nestedRuntimeException.getMostSpecificCause().getMessage());

        } else if (e instanceof ClientAbortException clientAbortException) {
            errorResponse = errorUtilities.buildError(API408_REQUEST_TIMEOUT, clientAbortException.getLocalizedMessage());
            logException = false;

        } else {
            errorResponse = errorUtilities.buildError(API500_INTERNAL_SERVER_ERROR, e.getMessage());
            defaultExceptionHandler = true;
        }

        if (logException) {
            String loggerMessage = getExceptionHandlerName(e, defaultExceptionHandler) + ": " + req.getDescription(false);

            if (HttpStatusCode.valueOf(errorResponse.getStatus()).is5xxServerError()) {
                logger.error(loggerMessage, e);
            } else {
                logger.warn(loggerMessage, e);
            }
        }

        return ResponseEntity.status(errorResponse.getStatus())
                             .contentType(APPLICATION_JSON)
                             .body(errorResponse);
    }

    private String getExceptionHandlerName(Exception e, boolean defaultExceptionHandler) {
        return (defaultExceptionHandler ? "DefaultExceptionHandler (%s)" : "%sHandler").formatted(e.getClass().getSimpleName());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest req) {
        List<String> validationErrors = e.getBindingResult()
                                         .getAllErrors()
                                         .stream()
                                         .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                         .toList();

        return handleBadRequestException(validationErrors);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest req) {
        return handleBadRequestException(API400_MISSING_SERVLET_REQUEST_PARAMETER, e.getParameterName(), e.getParameterType());
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(@NotNull ServletRequestBindingException e, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest req) {
        return handleBadRequestException(e.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException e, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest req) {
        return handleBadRequestException(API400_TYPE_MISMATCH, e.getPropertyName());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, @NotNull HttpHeaders headers, @NotNull HttpStatusCode status, @NotNull WebRequest req) {
        return handleBadRequestException(e.getLocalizedMessage());
    }

    private ResponseEntity<Object> handleBadRequestException(Object message) {
        ErrorResponseDTO errorResponse = errorUtilities.buildError(BAD_REQUEST, message);

        return ResponseEntity.badRequest()
                             .contentType(APPLICATION_JSON)
                             .body(errorResponse);
    }

    private ResponseEntity<Object> handleBadRequestException(PersonalApiErrorsEnum personalApiErrorsEnum, Object... args) {
        return handleBadRequestException(personalApiErrorsEnum.getMessage().formatted(args));
    }

}
