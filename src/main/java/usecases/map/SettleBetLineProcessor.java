package usecases.map;

import com.hazelcast.map.EntryProcessor;
import lombok.extern.log4j.Log4j2;
import usecases.dto.BetLine;

import java.util.Map;

@Log4j2
public class SettleBetLineProcessor implements EntryProcessor<String, BetLine, Object> {

    public Object process( Map.Entry<String, BetLine> entry) {
        BetLine betLine = entry.getValue();
        betLine.setResult(betLine.getContents().equals("D") ? 1 : 0);
        entry.setValue(betLine);
        return Thread.currentThread().getName();
    }
}
