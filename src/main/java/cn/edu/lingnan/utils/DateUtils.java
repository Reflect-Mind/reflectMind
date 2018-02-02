package cn.edu.lingnan.utils;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/21.
 */
public class DateUtils {

    private DateUtils(){}

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static String format(Date date){
        try {
            return format.format(date);
        }
        catch (NullPointerException e){
            return "";
        }
    }

    public static boolean validate(String date){
        try {
            format.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static Date parse(String date){
        try {
            return format.parse(date);
        } catch (ParseException e) {
        }
        return null;
    }
}
