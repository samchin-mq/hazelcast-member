package usecases.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.cluster.Member;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import usecases.cache.BetLineIndexCache;
import usecases.dto.BetLine;
import usecases.services.SelfService;
import usecases.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@Log4j2
public class BetLineController {
    @Autowired
    private SelfService selfService;

    @Autowired
    private BetLineIndexCache betLineIndexCache;


    @GetMapping("/betline/preDump")
    public String preDump() throws Exception {
        Resource resource = new ClassPathResource("betline.dat");
        File file = resource.getFile();
        List<BetLine> betLineList = (List<BetLine>) FileUtils.readObject(file);
        Map<String, BetLine> betLineMap = new HashMap<>();
        betLineList.forEach( x -> betLineIndexCache.add(x));
        ;
//        betLineList.forEach( x -> betLineMap.put(x.getBid(), x));
//        betLineIndexCache.putAll(betLineMap);
        return String.format("Loaded %d records", betLineMap.size());
    }

    @GetMapping("/betline/load")
    public String load(Integer number) {
        try {
            if (number == null || number < 1) {
                number = 100;
            }
            BetLine lastBet = null;
            for (int i = 0; i < number; i++) {
                BetLine betLine = selfService.generateBetLine();
                betLineIndexCache.add(betLine);
                lastBet = betLine;
            }
            log.info("last bet line inserted {}, {}, {}, {}, {}", lastBet.getBid(), lastBet.getUserid(), lastBet.getLottery(), lastBet.getDrawNumber(), lastBet.getGame());
            return String.format("Inserted %d records %s %s %s %s", number, lastBet.getUserid(), lastBet.getLottery(), lastBet.getDrawNumber(), lastBet.getGame());

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/betline/search")
    public List<BetLine> search(String lottery, String drawNumber, String game) {
        return betLineIndexCache.searchSql(lottery, drawNumber, game);
    }

    @GetMapping("/betline/predicate")
    public List<BetLine> searchPredicate(String lottery, String drawNumber, String game) {
        return betLineIndexCache.predicate(lottery, drawNumber, game);
    }

    @GetMapping("/betline/export")
    public String export(String fileType) {
        List<BetLine> betLineList = betLineIndexCache.allValues();
        String suffix = ".dat";
        try {
            if ("json".equalsIgnoreCase(fileType)) {
                suffix = ".json";
                File tmpFile = File.createTempFile("betline", suffix);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.writeValue(tmpFile, betLineList);
                log.info(tmpFile.getAbsolutePath());
                return tmpFile.getAbsolutePath();
            } else {
                File tmpFile = File.createTempFile("betline", suffix);
                FileUtils.writeObject(tmpFile, betLineList);
                log.info(tmpFile.getAbsolutePath());
                return tmpFile.getAbsolutePath();
            }
        } catch (Exception ex) {
            log.error(ex);
            //do nothing
        }
        return null;
    }

    @GetMapping("/betline/predicateCount")
    public int searchPredicateCount(String lottery, String drawNumber, String game) {
        return betLineIndexCache.predicateCount(lottery, drawNumber, game);
    }

    @GetMapping("/betline/predicateUserid")
    public List<BetLine> predicateUserid(String lottery, String drawNumber, String game, String userid) {
        return betLineIndexCache.predicateUserid(lottery, drawNumber, game, userid);
    }

    @GetMapping("/betline/settle")
    public void settle(String lottery, String drawNumber, String result) {
        betLineIndexCache.settle(lottery, drawNumber, result);
    }

    @GetMapping("/betline/settleWithExec")
    public void settleWithExec(String lottery, String drawNumber, String result) {
        betLineIndexCache.settleWithExec(lottery, drawNumber, result);
    }

    @GetMapping("/betline/settleWithSubmit")
    public List<String> settleWithSubmit(String lottery, String drawNumber, String result) throws ExecutionException, InterruptedException {
        Map<Member, Future<String>> futures = betLineIndexCache.settleWithSubmit(lottery, drawNumber, result);
        List<String> responses = new ArrayList<>();
        futures.forEach((k,v) -> {
            try {
                responses.add(String.format("member %s return %s", k, v.get()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        return responses;
    }

    @GetMapping("/betline/settleWithSingleSubmit")
    public String settleWithSingleSubmit(String lottery, String drawNumber, String result) throws ExecutionException, InterruptedException {
            try {
                Future future = betLineIndexCache.settleWithSingleSubmit(lottery, drawNumber, result);
                return String.format("return %s", future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
    }

}
