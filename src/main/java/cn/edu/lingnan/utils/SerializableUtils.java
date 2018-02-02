package cn.edu.lingnan.utils;

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
    public static <T> T getLastState(Class<T> clz){
        String name = clz.getName();
        InputStream inputStream = clz.getClassLoader().getResourceAsStream("tmp/" + name);
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
    public static <T>  void saveCurrentState(T target){
        String className = target.getClass().getName();
        try  {
            String basePath = target.getClass().getClassLoader().getResource("").getPath();
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
}
