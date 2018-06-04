package info.amoraes.n26.challenge.errors;

import info.amoraes.n26.challenge.exceptions.TransactionTooOldException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionTooOldException.class)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void handleTransactionTooOld() {
        // nothing to do, just set the expected status code
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleAnyException(Exception ex) {
        log.error("Unexpected error {}", ex);
    }
}
