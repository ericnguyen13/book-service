package com.nguyen.exception;

import com.nguyen.audit.LoggerFactory;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final Logger logger;

    public ApiExceptionHandler(LoggerFactory loggerFactory) {
        this.logger = loggerFactory.getLogger(ApiExceptionHandler.class);
    }

    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException exception) {
        this.logger.error(exception);
        ApiErrorResponse errorResponse =
                ApiErrorResponse.builder()
                        .message(exception.getMessage())
                        .build();
        return new ResponseEntity<>(errorResponse, exception.getStatus());
    }
}
