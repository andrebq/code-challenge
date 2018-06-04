package info.amoraes.n26.challenge.api.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import info.amoraes.n26.challenge.api.ResourceURIBuilder;
import info.amoraes.n26.challenge.models.Summary;
import info.amoraes.n26.challenge.models.Transaction;
import info.amoraes.n26.challenge.services.TransactionService;
import java.net.URL;
import java.time.OffsetDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TransactionsControllerImplTest {

  @Mock private TransactionService transactionService;

  @Mock private ResourceURIBuilder resourceURIBuilder;

  @InjectMocks private TransactionsControllerImpl transactionsControllerImpl;

  @Test
  public void createTransaction() throws Exception {
    doNothing().when(transactionService).addTransaction(any(), any());
    when(resourceURIBuilder.statistics()).thenReturn(new URL("http://localhost/").toURI());
    final Long now = OffsetDateTime.now().toInstant().toEpochMilli();

    final Transaction input = Transaction.builder().build();

    ResponseEntity responseEntity = transactionsControllerImpl.createTransaction(input);
    assertThat(responseEntity, notNullValue());
    assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));

    verify(transactionService).addTransaction(longThat((v) -> v >= now), eq(input));
    verify(resourceURIBuilder).statistics();

    verifyNoMoreInteractions(transactionService);
    verifyNoMoreInteractions(resourceURIBuilder);
  }

  @Test
  public void getSummary() throws Exception {
    when(transactionService.getSummary(any())).thenReturn(Summary.builder().build());
    final Long now = OffsetDateTime.now().toInstant().toEpochMilli();

    transactionsControllerImpl.getSummary();

    verify(transactionService).getSummary(longThat((v) -> v >= now));

    verifyNoMoreInteractions(transactionService);
  }
}
