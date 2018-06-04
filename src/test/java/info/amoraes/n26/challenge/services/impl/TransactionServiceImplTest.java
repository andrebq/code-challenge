package info.amoraes.n26.challenge.services.impl;

import info.amoraes.n26.challenge.exceptions.TransactionTooOldException;
import info.amoraes.n26.challenge.models.Summary;
import info.amoraes.n26.challenge.models.Transaction;
import info.amoraes.n26.challenge.repository.SummaryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TransactionServiceImplTest {
    private SummaryRepository summaryRepository;

    private TransactionServiceImpl transactionService;

    @Before
    public void setup() {
        summaryRepository = mock(SummaryRepository.class);

        transactionService = new TransactionServiceImpl(summaryRepository);
    }

    @Test
    public void addTransaction() throws Exception {
        doNothing().when(summaryRepository).updateSummary(any(), any(), any());

        final Transaction inputTransaction = Transaction.builder().amount(1D).timestamp(1L).build();
        transactionService.addTransaction(1L, inputTransaction);

        verify(summaryRepository).updateSummary(
                eq(1L - 60 * 1000L),
                eq(1L),
                eq(Summary.builder().from(inputTransaction)));

        verifyNoMoreInteractions(summaryRepository);
    }

    @Test(expected = TransactionTooOldException.class)
    public void shouldPreventTooOldTransactions() throws Exception {
        transactionService.addTransaction(1L,
                Transaction.builder().amount(1D).timestamp(-60 * 1000L).build());
    }

    @Test
    public void getSummary() throws Exception {
        when(summaryRepository.mergeEntriesAfter(any())).thenReturn(
                Summary.builder().build());
        transactionService.getSummary(1L);

        verify(summaryRepository).mergeEntriesAfter(eq(1L - 60 * 1000L));

        verifyNoMoreInteractions(summaryRepository);
    }

}
