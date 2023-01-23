package usecases.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class BetLine implements Serializable {
    @Override
    public String toString() {
        return "BetLine{" +
                "bid='" + bid + '\'' +
                ", userid='" + userKey +'-'+ username + + '\'' +
                ", lottery='" + lottery + '\'' +
                ", game='" + game + '\'' +
                ", drawNumber='" + drawNumber + '\'' +
                ", amount=" + amount +
                ", contents='" + contents + '\'' +
                '}';
    }

    /**
     * BID	VARCHAR2(30 BYTE)	No		1	注单号
     * USERID	VARCHAR2(30 BYTE)	Yes		2	用户ID (Users table id column)
     * USER_TYPE	NUMBER(5,0)	Yes		3	1 - 会员；2 - 代理
     * BET_TYPE	NUMBER(5,0)	Yes		4	0，投注；1，补货
     * RANGE	CHAR(1 BYTE)	Yes		5	盘口
     * LOTTERY	VARCHAR2(10 BYTE)	Yes		6	彩票ID
     * GAME	VARCHAR2(30 BYTE)	Yes		7	玩法 (Games table ID)
     * DRAW_NUMBER	VARCHAR2(30 BYTE)	Yes		8	期数
     * TEXT	VARCHAR2(100 BYTE)	Yes		9	下注内容的显示
     * GAME_TEXT	VARCHAR2(20 CHAR)	Yes		10	彩种玩法 display text
     * ODDS_TEXT	VARCHAR2(50 BYTE)	Yes		11	赔率 (组合赔率由 “，” 隔开）
     * MULTIPLE	NUMBER(5,0)	Yes		12	复式下注数量
     * AMOUNT	NUMBER(20,4)	Yes		13	投注金额
     * CONTENTS	VARCHAR2(100 BYTE)	Yes		14	下注内容
     * STATE	VARCHAR2(100 BYTE)	Yes		15	复试下注种类 （用来查相对赔率）
     */
    String bid;
    String userid;
    public String getUserid() {
        return userKey + "-" + username;
    }
    int userType;
    int betType;
    char range;
    String lottery;
    String game;
    String drawNumber;
    String text;
    String gameText;
    String oddsText;
    Integer multiple;
    double amount;
    String contents;
    String state;

    /**
     * ODDS_DETAIL	VARCHAR2(50 BYTE)	Yes		16	赔率详情，如Z2=20.9,Z3=60。
     * CREATED	DATE	Yes		17	DB数据创建时间
     * SAVE_TIME	TIMESTAMP(6)	Yes	sysdate	18	备份时间
     * RESULT	NUMBER(5,0)	Yes		19	1 - 赢；-1 输; 0 - 无成绩
     * ODDS	NUMBER(20,6)	Yes		20	实际结算赔率
     * DIVIDEND	NUMBER(20,6)	Yes		21	投注结果
     * SETTLE_TIME	DATE	Yes		22	结算时间
     * STATUS	NUMBER(5,0)	Yes	0	23	-1:cancelled not visible 0:settle with result 1:cancelled visible 2:settle without result
     * CHANNEL	VARCHAR2(10 BYTE)	Yes		24	投注通道
     * IP	VARCHAR2(39 CHAR)	Yes		25	下注IP
     * REMARK	VARCHAR2(100 BYTE)	Yes		26	取消的备注说明
     */
    String oddsDetail;
    Date created;
    Date saveTime;
    Integer result;
    Double odds;
    Double dividend;
    Date settleTime;
    int status;
    String channel;
    String ip;
    String remark;

   /**
     * CM	NUMBER(8,6)	Yes		27	退水比率
     * CMA	NUMBER(20,6)	Yes		28	实际退水金额
     * GKEY	VARCHAR2(30 BYTE)	Yes		29	彩种玩法的grouping key
     * GNAME	VARCHAR2(20 CHAR)	Yes		30	分组玩法名
     * USER_KEY	VARCHAR2(12 BYTE)	Yes		31	公司用户名
     * DRAW_DATE	DATE	No		32	整合报表时间（每天6.10 到6.10 算一天）
     * WINS	NUMBER(5,0)	Yes		33	复式中奖注数
     * CK	VARCHAR2(12 BYTE)	Yes		34	安全哈兮代码
     * USERNAME	VARCHAR2(15 BYTE)	Yes		35	用户名 (Users table username column)
     * PARENTID	VARCHAR2(30 BYTE)	Yes		36	上级ID
     * FREEZE_AMOUNT	NUMBER(20,4)	Yes		37	某些彩种玩法可能会冻结部分会员资金
     */
   Double cm;
   Double cma;
   String gkey;
   String gname;
   String userKey;
   Date drawDate;
   Integer wins;
   String ck;
   String username;
   String parentid;
   Double freezeAmount;
}
