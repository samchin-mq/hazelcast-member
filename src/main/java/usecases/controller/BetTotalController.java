package usecases.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import usecases.dto.BetTotalUserid;
import usecases.cache.BetTotalUseridCache;

import java.util.List;

@RestController
@Log4j2
public class BetTotalController {

    @Autowired
    private BetTotalUseridCache betTotalUseridCache;

    @GetMapping("/bettotal/predicate")
    public List<BetTotalUserid> searchPredicate(String lottery, String drawNumber, String game, String contents, String userid) {
        return betTotalUseridCache.predicate(lottery, drawNumber, game, contents, userid);
    }
}
