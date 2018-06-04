package info.amoraes.n26.challenge.services.impl;

import info.amoraes.n26.challenge.exceptions.TransactionTooOldException;
import info.amoraes.n26.challenge.models.Summary;
import info.amoraes.n26.challenge.models.Transaction;
import info.amoraes.n26.challenge.repository.SummaryRepository;
import info.amoraes.n26.challenge.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Constant-time guarantee for the endpoints:
 *
 * Then underlying data-structure used to store the entries is a TreeMap, which has big-O(log n) for reads.
 *
 * But on every call to updateSummary, the system will trim the tree from old records, meaning there is a upper-bound
 * number of records stored in the system.
 *
 * This upper-bound value is constant, therefore the upper-bound time-complexity for the underlying tree is limited
 * to a constant value.
 *
 * The system uses a precision of 1ms and keeps the history of the last 60 seconds, meaning the upper-bound size of the
 * tree is 60 * 1000 items even if we process more than 1 transaction per millisecond.
 *
 * If the precision is reduced to 1 second, then the upper-bound size will be 60 items,
 * even if we process 1000 transactions per second.
 *
 */
@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private static final Long MAX_AGE = 60 * 1000L;
    private static final Long PRECISION = 1L;

    private final SummaryRepository summaryRepository;

    @Autowired
    public TransactionServiceImpl(SummaryRepository summaryRepositoryImpl) {
        this.summaryRepository = summaryRepositoryImpl;
    }

    @Override
    public void addTransaction(Long now, Transaction t) {
        if (t.getTimestamp() < (now - MAX_AGE)) {
            log.info("Now is {} / Transaction is {} / delta is {}", now, t.getTimestamp(), now - t.getTimestamp());
            throw new TransactionTooOldException(t);
        }

        this.summaryRepository.updateSummary((now - MAX_AGE)/PRECISION, t.getTimestamp()/PRECISION,
                Summary.builder().from(t));
    }

    @Override
    public Summary getSummary(Long now) {
        log.info("get summary - now is {}, head is {}", now, now - MAX_AGE);
        return summaryRepository.mergeEntriesAfter((now - MAX_AGE)/PRECISION);
    }
}
