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

    /**
     * 将date实例转换为相应的字符串
     * @param date
     * @return
     */
    public static String format(Date date){
        try {
            return format.format(date);
        }
        catch (NullPointerException e){
            return "";
        }
    }

    /**
     * 验证目标的格式字符串是否能够转换为date实例
     * @param date 将被验证的字符串
     * @return
     */
    public static boolean validate(String date){
        try {
            format.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 将目标字符串转换为Date实例
     * @param date
     * @return
     */
    public static Date parse(String date){
        try {
            return format.parse(date);
        } catch (ParseException e) {
        }
        return null;
    }
}
