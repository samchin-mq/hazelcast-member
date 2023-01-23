package usecases.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BetTotalIp implements Serializable  {
    String key; //ip+lottery+drawNumber+game+contents
    String ip;
    String lottery;
    String drawNumber;
    String game;
    String contents;
    double amount;
}
