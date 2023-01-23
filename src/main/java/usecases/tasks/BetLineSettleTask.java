package usecases.tasks;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.map.IMap;
import com.hazelcast.partition.PartitionAware;
import com.hazelcast.partition.PartitionAwareKey;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import lombok.extern.log4j.Log4j2;
import usecases.dto.BetLine;

import java.io.Serializable;
import java.util.Collection;

@Log4j2
public class BetLineSettleTask implements Runnable, HazelcastInstanceAware, PartitionAware, Serializable {

    private final String lottery;
    private final String drawNumber;
    private final String result;
    private HazelcastInstance hazelcastInstance;

    public BetLineSettleTask(String lottery, String drawNumber, String result ) {
        this.lottery = lottery;
        this.drawNumber = drawNumber;
        this.result = result;
    }

    @Override
    public void run() {
        IMap map = hazelcastInstance.getMap("betlineidx");
        Predicate lotteryPredicate = Predicates.equal("lottery", lottery);
        Predicate drawNumberPredicate = Predicates.equal("drawNumber", drawNumber);
        Predicate ownPredicate = Predicates.and(lotteryPredicate, drawNumberPredicate);
        Collection<BetLine> betLines = map.values(ownPredicate);
        betLines.forEach(betLine -> {
            betLine.setResult(betLine.getContents().equals("D") ? 1 : 0);
            map.set(betLine.getBid(), betLine);
//            map.set(new PartitionAwareKey(betLine.getBid(),betLine.getLottery()), betLine);
        });
        log.info("betLines {} number", betLines.size());
    }

    @Override
    public Object getPartitionKey() {
        return lottery;
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
