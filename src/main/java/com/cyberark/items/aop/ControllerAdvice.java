package com.cyberark.items.aop;

import com.cyberark.items.entities.ErrorMessage;
import com.cyberark.items.exception.ItemNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;

import static org.slf4j.helpers.MessageFormatter.format;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage generalExceptionHandler(final Exception ex, final WebRequest request) {
        return buildErrorMessage(ex, "An internal error occurred", request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundExceptionHandler(final ItemNotFoundException ex, final WebRequest request) {

        return buildErrorMessage(ex, ex.getMessage(), request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage invalidArgumentTypeExceptionHandler(final MethodArgumentTypeMismatchException ex, final WebRequest request) {

        final String message = format("Invalid request param or path variable. value:{}", ex.getValue()).getMessage();

        return buildErrorMessage(ex, message, request, HttpStatus.BAD_REQUEST);
    }

    private ErrorMessage buildErrorMessage(final Exception ex, final String message, final WebRequest request, final HttpStatus httpStatus) {
        log.error("", ex);

        return ErrorMessage.builder()
                .message(message)
                .description(request.getDescription(false))
                .dateTime(OffsetDateTime.now())
                .errorCode(httpStatus.value())
                .build();
    }
}
