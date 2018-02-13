package cn.edu.lingnan.utils;

import javax.swing.*;
import java.io.*;

/**
 * Created by Administrator on 2018/1/29.
 * 对于一些重型类，
 * 离线加载能够提高
 * 加载速度
 * 上次无状态，又或者暂存文件被删
 * 则将对象重新实例化对象, 但同时要确保
 * 该类拥有默认的构造方法
 */
public class SerializableUtils {
    private SerializableUtils(){}

    /**
     * 获取该上次保存的该类的实例
     * 当没有找到对应的类的实例时,则新建该类的实例
     * @param clz 获取上次状态的目标类对应的实例
     * @param <T>
     * @return 目标类的实例
     */
    public static <T> T getLastState(Class<T> clz){
        String name = clz.getName();
        InputStream inputStream = SerializableUtils.class.getClassLoader().getResourceAsStream("tmp/" + name);
        T instance = null;
        if (inputStream != null) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(inputStream))) {
                instance = (T) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                instance = clz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * 保存目标类的对象的到磁盘中
     * @param target 目标类
     * @param <T>
     */
    public static <T>  void saveCurrentState(T target){
        String className = target.getClass().getName();
        try  {
            String basePath = SerializableUtils.class.getClassLoader().getResource("").getPath();
            File directory = new File(basePath, "tmp");
            if (!directory.exists())
                directory.mkdir();
            File file = new File(directory, className);
            if (!file.exists())
                file.createNewFile();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(file)));
            objectOutputStream.writeObject(target);
            objectOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
        }
    }

//    public static void main(String[] args){
//        Long a = System.currentTimeMillis();
//        JFrame frame = new JFrame();
//        System.out.println(System.currentTimeMillis() - a);
//        a = System.currentTimeMillis();
//        frame = SerializableUtils.getLastState(JFrame.class);
//        System.out.println(System.currentTimeMillis() - a);
//    }
}
