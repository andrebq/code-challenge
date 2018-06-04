package info.amoraes.n26.challenge.errors;

import info.amoraes.n26.challenge.api.impl.TransactionsControllerImpl;
import info.amoraes.n26.challenge.exceptions.TransactionTooOldException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = TransactionsControllerImpl.class)
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionTooOldException.class)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void handleTransactionTooOld() {
        // nothing to do, just set the expected status code
    }
}
