package usecases.utils;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Collections.emptyList;

@UtilityClass
public class RandomUtils {

    public static void main(String[] args) throws Exception {
        System.out.println(getBid());
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    Random random = new Random();

    private String getNumber(String number) {
        return ("00000000000000"+number).substring(number.length());
    }

    public String getBid() {
        //2021101311363928616893480001
        //yyyyMMddHHmmss00000000000000
        return simpleDateFormat.format(new Date()) + getNumber(String.valueOf(random.nextInt(999999999)));
    }

    public String getUserKey() {
        List<String> list = Arrays.asList("kiki", "lala", "nono");
        return list.get(random.nextInt(list.size()));
    }

    public String getUsername() {
        List<String> list = Arrays.asList("terence", "james", "nicholas", "sam", "sally");
        return list.get(random.nextInt(list.size())) + random.nextInt(9999999);
    }

    public String getLottery() {
        List<String> list = Arrays.asList("PK10JSC", "SSCJSC", "11X5JSC");
        return list.get(random.nextInt(list.size()));
    }

    public String getGame(String lottery) {
        List<String> list;
        switch (lottery) {
            case "PK10JSC":
                list = Arrays.asList("PK10-DX1", "PK10-DX2", "PK10-DX3");
                break;
            case "SSCJSC":
                list = Arrays.asList("SSC-DX1", "SSC-DX2", "SSC-DX3");
                break;
            case "11X5JSC":
                list = Arrays.asList("11X5-DX1", "11X5-DX2", "11X5-DX3");
                break;
            default:
                list = emptyList();
        }
        return list.get(random.nextInt(list.size()));
    }

    public String getContents() {
        //only support DX
        List<String> list = Arrays.asList("D", "X");
        return list.get(random.nextInt(list.size()));
    }

    public String getDrawNumber() {
        List<String> list = Arrays.asList("2023001", "2023002");
        return list.get(random.nextInt(list.size()));
    }

    public Double getAmount() {
        double min = 1.00;
        double max = 1000.00;
        return (new Random().nextDouble() * (max - min)) + min;
    }

    public String getIp() {
        Integer first = random.nextInt(255);
        Integer second = random.nextInt(255);
        Integer third = random.nextInt(255);
        Integer four = random.nextInt(255);
        String ip = String.format("%d.%d.%d.%d", first, second, third, four);
        return ip;
//        List<String> list = Arrays.asList("1.1.1.1", "2.2.2.2", "3.3.3.3", "4.4.4.4", "5.5.5.5", "6.6.6.6", "7.7.7.7", "8.8.8.8", "9.9.9.9");
//        return list.get(random.nextInt(list.size()));
    }

    public String getParentid(String userKey) {
        List<String> list = Arrays.asList("agent1", "agent2", "agent3", "agent4");
        String parent = list.get(random.nextInt(list.size()));
        return userKey.concat("-").concat(parent);
    }

    public String translate(String key) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("D", "大");
        map.put("X", "小");
        map.put("PK10-DX1", "冠军");
        map.put("PK10-DX2", "亚军");
        map.put("PK10-DX3", "第三名");
        map.put("SSC-DX1", "第一球");
        map.put("SSC-DX2", "第二球");
        map.put("SSC-DX3", "第三球");
        map.put("11X5-DX1", "第一球");
        map.put("11X5-DX2", "第二球");
        map.put("11X5-DX3", "第三球");
        return map.getOrDefault(key, "");
    }

    public char getRange() {
        List<String> list = Arrays.asList("A", "B", "C", "D");
        return list.get(random.nextInt(list.size())).charAt(0);
    }
}
