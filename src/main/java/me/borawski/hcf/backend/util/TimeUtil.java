package me.borawski.hcf.backend.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ethan on 4/9/2017.
 */
public class TimeUtil {

    public static String getTime() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()).replace(".", " ");
    }

    public static String getTime(long time) {
        if(time == -1) {
            return "Permanent";
        }
        return new SimpleDateFormat("MM.dd HH:mm").format(new Date(time)).replace(".", " ");
    }

}
