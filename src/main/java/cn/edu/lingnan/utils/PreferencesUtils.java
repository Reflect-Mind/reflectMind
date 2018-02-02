package cn.edu.lingnan.utils;

/**
 * Created by Administrator on 2018/1/24.
 */

import cn.edu.lingnan.Main;

import java.util.prefs.Preferences;

/**
 * 参数参数设置工具包
 */
public class PreferencesUtils {

    private PreferencesUtils(){}

    private static Preferences preferences = Preferences.userNodeForPackage(Main.class);

    public static String getParameters(String key){
        return preferences.get(key, null);
    }

    public static void setParameters(String key, String value){
        preferences.put(key, value);
    }

}
