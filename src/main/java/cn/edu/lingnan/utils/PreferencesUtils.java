package cn.edu.lingnan.utils;

/**
 * Created by Administrator on 2018/1/24.
 */

import cn.edu.lingnan.Main;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * 全局参数设置工具包
 * 设置的参数情况:
 *  basePath:   基础路径
 *  LocalProject: 当前对象名称
 *
 */
public class PreferencesUtils {

    private PreferencesUtils(){}

    private static Preferences preferences = Preferences.userNodeForPackage(Main.class);

    public static String getParametersAsString(String key){
        return preferences.get(key, null);
    }

    /**
     * 根据键名获取某对象集合
     * @param key 某对象对应的键名
     * @param <T>
     * @return
     */
    public static <T> List<T> getParametersAsList(String key){
        URL url = PreferencesUtils.class.getClassLoader().getResource("xml");
        File file = new File(String.join("/",url.getPath(), key + ".xml"));
        List<T> list = null;
        try (XMLDecoder xmlDecoder = new XMLDecoder(
                new BufferedInputStream(new FileInputStream(file)))) {
            list = (List<T>) xmlDecoder.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void setParametersAsString(String key, String value){
        preferences.put(key, value);
    }

    /**
     * 根据键名存储对象
     * @param key 将要存储对象的键名
     * @param value 将要存储的对象
     * @param <T>
     */
    public static <T> void setParametersAsList(String key, List<T> value){
        URL url = PreferencesUtils.class.getClassLoader().getResource("xml");
        File file = new File(String.join("/",url.getPath(), key + ".xml"));
        if (value == null)
            file.delete();
        else {
            try (XMLEncoder encoder = new XMLEncoder(
                    new BufferedOutputStream(new FileOutputStream(file)))) {
                encoder.writeObject(value);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
