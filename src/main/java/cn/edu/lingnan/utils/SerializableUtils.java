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

    private static String basePath = SerializableUtils.class.getClassLoader().getResource("").getPath();

    private SerializableUtils(){}

    private static <T> T getLastState(String path)  {
        InputStream inputStream =  null;
        File file = new File(path);
        if (file.exists())
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        else
            SerializableUtils.class.getClassLoader().getResourceAsStream(basePath + "/" + path);
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
        return instance;
    }

    /**
     * 获取该上次保存的该类的实例
     * 当没有找到对应的类的实例时,则新建该类的实例
     * @param clz 获取上次状态的目标类对应的实例
     * @param <T>
     * @return 目标类的实例
     */
    public static <T> T getLastState(Class<T> clz, String path){
        T instance = null;
        instance = SerializableUtils.getLastState(path);
        return instance;
    }

    public static <T> T getLastState(Class<T> clz){
        T instance = null;
        instance = SerializableUtils.getLastState("tmp/" + clz.getSimpleName());
        return instance;
    }


    /**
     * 保存目标类的对象的到磁盘中
     * @param target 目标类
     * @param <T>
     */
    public static <T>  void saveCurrentState(T target) throws IOException {
        String className = target.getClass().getName();
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
    }

    public static <T>  void saveCurrentState(T target, String path) throws IOException {
        File file = new File(path);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(file)));
        objectOutputStream.writeObject(target);
        objectOutputStream.flush();
    }


    public static void main(String[] args) throws IOException {
//        Long a = System.currentTimeMillis();
//        JFrame frame = new JFrame();
//        System.out.println(System.currentTimeMillis() - a);
//        a = System.currentTimeMillis();
////        frame = SerializableUtils.getLastState(JFrame.class);
////        SerializableUtils.saveCurrentState(frame);
//        frame = SerializableUtils.getLastState(JFrame.class);
//        System.out.println(frame);
    }
}
