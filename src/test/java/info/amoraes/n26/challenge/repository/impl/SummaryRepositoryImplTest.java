package info.amoraes.n26.challenge.repository.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import info.amoraes.n26.challenge.models.Summary;
import info.amoraes.n26.challenge.models.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SummaryRepositoryImplTest {

  @InjectMocks private SummaryRepositoryImpl repository;

  @Test
  public void shouldUpdateEntriesAndGetCorrectValue() throws Exception {

    Summary s = Summary.builder().from(Transaction.builder().amount(1D).timestamp(1000L).build());

    repository.updateSummary(0L, 1000L, s);

    assertThat(repository.mergeEntriesAfter(0L), is(s));
  }

  @Test
  public void shouldTrimOldValues() throws Exception {
    Summary reallyOld =
        Summary.builder().from(Transaction.builder().amount(1D).timestamp(1L).build());
    repository.updateSummary(0L, 1L, reallyOld);

    Summary notSoOld =
        Summary.builder().from(Transaction.builder().amount(1D).timestamp(2L).build());

    repository.updateSummary(1L, 2L, notSoOld);
    assertThat(repository.mergeEntriesAfter(1L), is(notSoOld));
  }
}
