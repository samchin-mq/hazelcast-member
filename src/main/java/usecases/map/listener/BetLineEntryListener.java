package usecases.map.listener;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import usecases.cache.BetTotalUseridCache;
import usecases.dto.BetLine;

@Component
@Log4j2
public class BetLineEntryListener implements EntryAddedListener<String, BetLine> {

    private BetTotalUseridCache betTotalUseridCache;

    public BetLineEntryListener(BetTotalUseridCache betTotalUseridCache) {
        this.betTotalUseridCache = betTotalUseridCache;
    }

    @Override
    public void entryAdded(EntryEvent<String, BetLine> entryEvent) {
        this.betTotalUseridCache.add(entryEvent.getValue());
        log.info("Added {} to bettotaluserid", entryEvent.getValue());
    }
}
