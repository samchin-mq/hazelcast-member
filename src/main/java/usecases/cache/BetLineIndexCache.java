package usecases.cache;

import com.hazelcast.config.IndexType;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.map.IMap;
import com.hazelcast.partition.PartitionAwareKey;
import com.hazelcast.partition.PartitionService;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;
import com.hazelcast.sql.SqlService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import usecases.dto.BetLine;
import usecases.map.SettleBetLineProcessor;
import usecases.map.listener.BetLineEntryListener;
import usecases.tasks.BetLineSettleCallableTask;
import usecases.tasks.BetLineSettleTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Log4j2
@Component
public class BetLineIndexCache {


    private IMap betLineMap = null;

    private SqlService sqlService = null;

    private IExecutorService executorService = null;

    private PartitionService partitionService = null;

    public BetLineIndexCache(HazelcastInstance hazelcastInstance, BetLineEntryListener betLineEntryListener) {
        String MAP_NAME = "betlineidx";

        this.betLineMap = hazelcastInstance.getMap(MAP_NAME);
        this.betLineMap.addIndex(IndexType.SORTED, "lottery", "drawNumber", "game", "contents");
        this.sqlService = hazelcastInstance.getSql();
        this.sqlService.execute("CREATE MAPPING IF NOT EXISTS \""+MAP_NAME+"\" \n" +
                "TYPE IMap\n" +
                "OPTIONS (\n" +
                "  'keyFormat' = 'java',\n" +
                "  'keyJavaClass' = 'java.lang.String',\n" +
                "  'valueFormat' = 'java',\n" +
                "  'valueJavaClass' = 'usecases.dto.BetLine'\n" +
                ")");
        this.betLineMap.addEntryListener(betLineEntryListener, true);
        this.executorService = hazelcastInstance.getExecutorService("hazelcast-dev");
        this.partitionService = hazelcastInstance.getPartitionService();
    }

    public void putAll(Map<String, BetLine> betLineMap) {
        this.betLineMap.putAll(betLineMap);
    }

    public void add(BetLine betLine) {
        this.betLineMap.put(betLine.getBid() , betLine);
//        this.betLineMap.put(new PartitionAwareKey(betLine.getBid(),betLine.getLottery()) , betLine);
    }

    public List<BetLine> predicate(String lottery, String drawNumber, String game) {
        Predicate lotteryPredicate = Predicates.equal("lottery", lottery);
        Predicate drawNumberPredicate = Predicates.equal("drawNumber", drawNumber);
        Predicate gamePredicate = Predicates.equal("game", game);
        Predicate ownPredicate = Predicates.and(lotteryPredicate, drawNumberPredicate, gamePredicate);
        return (List<BetLine>) this.betLineMap.values(ownPredicate).stream().collect(Collectors.toList());
    }

    public int predicateCount(String lottery, String drawNumber, String game) {
        Predicate lotteryPredicate = Predicates.equal("lottery", lottery);
        Predicate drawNumberPredicate = Predicates.equal("drawNumber", drawNumber);
        Predicate gamePredicate = Predicates.equal("game", game);
        Predicate ownPredicate = Predicates.and(lotteryPredicate, drawNumberPredicate, gamePredicate);
        return this.betLineMap.values(ownPredicate).size();
    }

    public List<BetLine> predicateUserid(String lottery, String drawNumber, String game, String userid) {
        Predicate lotteryPredicate = Predicates.equal("lottery", lottery);
        Predicate drawNumberPredicate = Predicates.equal("drawNumber", drawNumber);
        Predicate gamePredicate = Predicates.equal("game", game);
        Predicate useridPredicate = Predicates.equal("userid", userid);
        Predicate ownPredicate = Predicates.and(lotteryPredicate, drawNumberPredicate, gamePredicate, useridPredicate);
        return (List<BetLine>) this.betLineMap.values(ownPredicate).stream().collect(Collectors.toList());
    }

    public List<BetLine> searchSql(String lottery, String drawNumber, String game) {
        SqlResult sqlRows = this.sqlService.execute("SELECT bid, lottery, drawNumber, game, contents, username, userKey, amount FROM betlineidx WHERE lottery = ? AND drawNumber = ? AND game = ? ", lottery, drawNumber, game);

//        List<String> result = new ArrayList<>();
        List<BetLine> result = new ArrayList<>();
        for (SqlRow sqlRow : sqlRows) {
            BetLine betLine = new BetLine();
            betLine.setBid(sqlRow.getObject("bid"));
            betLine.setLottery(sqlRow.getObject("lottery"));
            betLine.setDrawNumber(sqlRow.getObject("drawNumber"));
            betLine.setGame(sqlRow.getObject("game"));
            betLine.setContents(sqlRow.getObject("contents"));
            betLine.setUsername(sqlRow.getObject("username"));
            betLine.setUserKey(sqlRow.getObject("userKey"));
            betLine.setAmount(sqlRow.getObject("amount"));
            result.add(betLine);
//            result.add(sqlRow.toString());
//            System.out.println(sqlRow);
        }
        log.info("result size {}", result.size());
        return result;

    }

    public void settle(String lottery, String drawNumber, String result) {
        Predicate lotteryPredicate = Predicates.equal("lottery", lottery);
        Predicate drawNumberPredicate = Predicates.equal("drawNumber", drawNumber);
        Predicate ownPredicate = Predicates.and(lotteryPredicate, drawNumberPredicate);
        this.betLineMap.executeOnEntries(new SettleBetLineProcessor(), ownPredicate);
    }

    public List<BetLine> allValues() {
        return new ArrayList<>(this.betLineMap.values());
    }

    public void settleWithExec(String lottery, String drawNumber, String result) {
        this.executorService.executeOnAllMembers(new BetLineSettleTask(lottery, drawNumber, result));
    }

    public Map settleWithSubmit(String lottery, String drawNumber, String result) {
        return this.executorService.submitToAllMembers(new BetLineSettleCallableTask(lottery, drawNumber, result));
    }

    public Future settleWithSingleSubmit(String lottery, String drawNumber, String result) {

        log.info(this.partitionService.getPartitions());
        log.info(this.partitionService.getPartition(lottery).getOwner());
        return this.executorService.submitToMember(new BetLineSettleCallableTask(lottery, drawNumber, result), this.partitionService.getPartition(lottery).getOwner());
//        return this.executorService.submitToAllMembers(new BetLineSettleCallableTask(lottery, drawNumber, result));
    }

}
