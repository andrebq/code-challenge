package info.amoraes.n26.challenge.repository.impl;

import info.amoraes.n26.challenge.models.Summary;
import info.amoraes.n26.challenge.repository.SummaryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class SummaryRepositoryImpl implements SummaryRepository {

    private SortedMap<Long, Summary> entries;
    private final ReentrantReadWriteLock rwrLock;
    private final Lock readLock;
    private final Lock writeLock;

    public SummaryRepositoryImpl() {
        this.entries = new TreeMap<>();
        this.rwrLock = new ReentrantReadWriteLock();
        this.readLock = rwrLock.readLock();
        this.writeLock = rwrLock.writeLock();
    }

    /**
     * update summary adds the given summary to the current entry set,
     * it will trim items that are older than minTimestamp
     * @param timestamp
     */
    public void updateSummary(Long minTimestamp, Long timestamp, Summary newItem) {
        writeLock.lock();
        try {
            trimIfNeeded(minTimestamp);
            entries.put(timestamp,
                    Optional.ofNullable(entries.get(timestamp)).map(a -> Summary.merge(a, newItem)).orElse(newItem));
        } finally {
            writeLock.unlock();
        }
    }

    private void trimIfNeeded(Long timestamp) {
        if (!entries.headMap(timestamp+1).isEmpty()) {
            // create a new map to release memory from the old one
            entries = new TreeMap<>(entries.tailMap(timestamp+1));
        }
    }

    /**
     * mergeEntriesAfter will run the merge operation for all entries after the given timestamp
     * @param timestamp
     * @return
     */
    public Summary mergeEntriesAfter(Long timestamp) {
        readLock.lock();
        try {
            return entries.tailMap(timestamp).values().stream().reduce(Summary::merge).orElse(Summary.builder().build());
        } finally {
            readLock.unlock();
        }
    }

}
