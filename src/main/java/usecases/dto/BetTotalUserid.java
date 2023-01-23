package usecases.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BetTotalUserid implements Serializable  {
    String key; //userid+lottery+drawNumber+game+contents

    public String getKey() {
        return String.format("%s-%s-%s-%s-%s", userid,  lottery, drawNumber, game, contents);
    }

    String userid;
    String lottery;
    String drawNumber;
    String game;
    String contents;
    double amount;

}
