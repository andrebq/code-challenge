package info.amoraes.n26.challenge.repository;

import info.amoraes.n26.challenge.models.Summary;

public interface SummaryRepository {
  void updateSummary(Long minTimestamp, Long timestamp, Summary newItem);

  Summary mergeEntriesAfter(Long timestamp);
}
