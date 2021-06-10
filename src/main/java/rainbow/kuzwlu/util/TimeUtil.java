package rainbow.kuzwlu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2020/7/6 09:19
 * @Version 1.0
 */

public class TimeUtil {
    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String nowtimes){
        String time = null;
        SimpleDateFormat sdfh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdfh.parse(nowtimes);
            long ts = date.getTime();
            time = String.valueOf(ts);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(Long time){
        SimpleDateFormat sdfh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(time);
        Date date = new Date(lt);
        return sdfh.format(date);
    }

    public static String getNowtime(){
        SimpleDateFormat sdfh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdfh.format(new Date());
    }

}
