package usecases.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GameUtils {
    public String getLMContent(String source) {
        //DX - D->X
        if ("D".equals(source)) {
            return "X";
        }

        if ("X".equals(source)) {
            return "D";
        }
        return null;
    }
}
