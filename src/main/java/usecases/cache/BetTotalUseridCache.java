package usecases.cache;

import com.hazelcast.config.IndexType;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.hazelcast.sql.SqlService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import usecases.dto.BetLine;
import usecases.dto.BetTotalUserid;
import usecases.utils.GameUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
public class BetTotalUseridCache {

    private IMap<String, BetTotalUserid> betTotalMap = null;
    private SqlService sqlService = null;

    public BetTotalUseridCache(HazelcastInstance hazelcastInstance) {
        this.betTotalMap = hazelcastInstance.getMap("bettotaluserid");
        this.betTotalMap.addIndex(IndexType.SORTED, "lottery", "drawNumber", "game", "contents", "userid");
        this.sqlService = hazelcastInstance.getSql();
//        this.sqlService.execute("CREATE MAPPING \"bettotaluserid\"\n" +
//                "TYPE IMap\n" +
//                "OPTIONS (\n" +
//                "  'keyFormat' = 'java',\n" +
//                "  'keyJavaClass' = 'java.lang.String',\n" +
//                "  'valueFormat' = 'java',\n" +
//                "  'valueJavaClass' = 'com.example.demo.BetTotalUserid'\n" +
//                ")");
    }

    public void add(BetLine betLine) {
        BetTotalUserid betTotalUserid = new BetTotalUserid();
        betTotalUserid.setUserid(betLine.getUserid());
        betTotalUserid.setLottery(betLine.getLottery());
        betTotalUserid.setDrawNumber(betLine.getDrawNumber());
        betTotalUserid.setGame(betLine.getGame());
        betTotalUserid.setContents(betLine.getContents());
        BetTotalUserid existing = this.betTotalMap.get(betTotalUserid.getKey());
        if (existing != null) {
            betTotalUserid.setAmount(Double.sum(existing.getAmount(), betLine.getAmount()));
        } else {
            betTotalUserid.setAmount(betLine.getAmount());
        }
        this.betTotalMap.put(betTotalUserid.getKey(), betTotalUserid);
    }

    public List<BetTotalUserid> predicate(String lottery, String drawNumber, String game, String contents, String userid) {
        Predicate lotteryPredicate = Predicates.equal( "lottery", lottery);
        Predicate drawNumberPredicate = Predicates.equal( "drawNumber", drawNumber);
        Predicate gamePredicate = Predicates.equal( "game", game);
        Predicate useridPredicate = Predicates.notEqual( "userid", userid);
        Predicate contentPredicate = Predicates.equal( "contents", GameUtils.getLMContent(contents));
        Predicate duiDaPredicate = Predicates.and( lotteryPredicate, drawNumberPredicate, gamePredicate, contentPredicate, useridPredicate);
        Instant start2 = Instant.now();

        Collection<BetTotalUserid> duiDaBetLines = this.betTotalMap.values(duiDaPredicate);
        log.info("Possible 对打 list {}", duiDaBetLines.size());
//        log.info("Possible 对打 list {}", this.betTotalMap.values( duiDaPredicate).size());
        Instant finish2 = Instant.now();
        long timeElapsed2 = Duration.between(start2, finish2).toMillis();
        log.info("Search dui da record {}", timeElapsed2);
        return duiDaBetLines.stream().collect(Collectors.toList());

//        duiDaBetLines.forEach(log::info);
    }
}
