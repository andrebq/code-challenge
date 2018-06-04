package info.amoraes.n26.challenge.services.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import info.amoraes.n26.challenge.models.Summary;
import info.amoraes.n26.challenge.models.Transaction;
import info.amoraes.n26.challenge.repository.impl.SummaryRepositoryImpl;
import info.amoraes.n26.challenge.services.TransactionService;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
  classes = {
    TransactionServiceImpl.class,
    SummaryRepositoryImpl.class,
  }
)
public class TransactionServiceIntegrationTest {

  @Autowired private TransactionService transactionService;

  @Test
  public void handlesMultithreadAccess() throws Exception {
    final Long now = OffsetDateTime.now().toInstant().toEpochMilli() - 5;
    Summary expectedResult =
        Executors.newWorkStealingPool()
            .invokeAll(
                Arrays.asList(
                    generateItems(transactionService, now),
                    generateItems(transactionService, now + 1),
                    generateItems(transactionService, now + 2),
                    generateItems(transactionService, now + 3),
                    generateItems(transactionService, now + 4)))
            .stream()
            .map(
                future -> {
                  try {
                    return future.get();
                  } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                  }
                })
            .reduce(Summary::merge)
            .orElseThrow(() -> new RuntimeException("empty summary"));

    assertThat(transactionService.getSummary(now - 5), is(expectedResult));
  }

  private Callable<Summary> generateItems(TransactionService service, Long now) {
    return () -> {
      ArrayList<Transaction> added = new ArrayList<>(10);
      for (int i = 0; i < 10; i++) {
        Transaction t = Transaction.builder().amount(1D).timestamp(now).build();
        service.addTransaction(now, t);
        added.add(t);
      }
      return added
          .stream()
          .map(t -> Summary.builder().from(t))
          .reduce(Summary::merge)
          .orElse(Summary.builder().build());
    };
  }
}
