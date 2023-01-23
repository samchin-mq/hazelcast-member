package usecases.services;

import org.springframework.stereotype.Service;
import usecases.dto.BetLine;
import usecases.utils.RandomUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class SelfService {

    public BetLine generateBetLine() {
        BetLine betLine = new BetLine();
        betLine.setBid(RandomUtils.getBid());
//        betLine.setUserid("kc88-yu6m7t0yiia");
        betLine.setUserType(1);
        betLine.setBetType(0);
        betLine.setRange(RandomUtils.getRange());
        String lottery = RandomUtils.getLottery();
        String game = RandomUtils.getGame(lottery);
        String contents = RandomUtils.getContents();
        betLine.setLottery(lottery);
        betLine.setGame(game);
        betLine.setDrawNumber(RandomUtils.getDrawNumber());
        betLine.setText(RandomUtils.translate(contents));
        betLine.setGameText(RandomUtils.translate(game));
        betLine.setOddsText("2");
        betLine.setMultiple(0);
        betLine.setAmount(RandomUtils.getAmount());
        betLine.setContents(contents);
//        betLine.setState(null);
        betLine.setOddsDetail("2");
        betLine.setCreated(new Date());
        betLine.setSaveTime(new Date());
//        betLine.setResult(null);
        betLine.setOdds(2.0);
//        betLine.setDividend(null);
//        betLine.setSettleTime(null);
        betLine.setStatus(0);
        betLine.setChannel("web");
        betLine.setIp(RandomUtils.getIp());
//        betLine.setRemark(null);
        betLine.setCm(0.0);
        betLine.setCma(0.0);
        betLine.setGkey("LM");
        betLine.setGname("两面");
        betLine.setUserKey(RandomUtils.getUserKey());
        Date drawDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        betLine.setDrawDate(drawDate);
//        betLine.setWins(null);
//        betLine.setCk(null);
        betLine.setUsername(RandomUtils.getUsername());
        betLine.setParentid(RandomUtils.getParentid(betLine.getUserKey()));
//        betLine.setFreezeAmount(null);
        return betLine;
    }
}
