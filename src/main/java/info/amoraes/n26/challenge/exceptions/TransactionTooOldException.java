package info.amoraes.n26.challenge.exceptions;

import info.amoraes.n26.challenge.models.Transaction;
import lombok.Getter;

@Getter
public class TransactionTooOldException extends RuntimeException {

  public TransactionTooOldException(Transaction transaction) {
    super(
        String.format(
            "Transaction of %f at %d is too old",
            transaction.getAmount(), transaction.getTimestamp()));
  }
}
