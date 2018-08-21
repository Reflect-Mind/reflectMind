package cn.edu.lingnan.utils;

/**
 * Created by Administrator on 2018/6/6.
 * 全局配置类
 */
public class Global {
    private static Global global = null;














    private Global(){}

    public static Global getInstance(){
        if (global == null)
            global = new Global();
        return global;
    }

}
