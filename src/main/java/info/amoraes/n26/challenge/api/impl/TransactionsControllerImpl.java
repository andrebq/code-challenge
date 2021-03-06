package info.amoraes.n26.challenge.api.impl;

import info.amoraes.n26.challenge.api.ResourceURIBuilder;
import info.amoraes.n26.challenge.api.TransactionController;
import info.amoraes.n26.challenge.models.Summary;
import info.amoraes.n26.challenge.models.Transaction;
import info.amoraes.n26.challenge.services.TransactionService;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class TransactionsControllerImpl implements TransactionController {

  private final TransactionService transactionService;
  private final ResourceURIBuilder resourceURIBuilder;

  @Autowired
  public TransactionsControllerImpl(
      TransactionService transactionService, ResourceURIBuilder resourceURIBuilder) {
    this.transactionService = transactionService;
    this.resourceURIBuilder = resourceURIBuilder;
  }

  @PostMapping(path = "/transactions", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> createTransaction(@Valid @RequestBody Transaction newTransaction) {
    this.transactionService.addTransaction(
        OffsetDateTime.now().toInstant().toEpochMilli(), newTransaction);
    return ResponseEntity.created(resourceURIBuilder.statistics()).build();
  }

  @GetMapping(path = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
  public Summary getSummary() {
    return this.transactionService.getSummary(OffsetDateTime.now().toInstant().toEpochMilli());
  }
}
