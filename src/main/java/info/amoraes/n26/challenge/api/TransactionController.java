package info.amoraes.n26.challenge.api;

import info.amoraes.n26.challenge.models.Summary;
import info.amoraes.n26.challenge.models.Transaction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface TransactionController {

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Void> createTransaction(@RequestBody Transaction newTransaction);

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  Summary getSummary();
}
