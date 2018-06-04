package info.amoraes.n26.challenge.services;

import info.amoraes.n26.challenge.models.Summary;
import info.amoraes.n26.challenge.models.Transaction;

public interface TransactionService {
  void addTransaction(Long now, Transaction t);

  Summary getSummary(Long now);
}
