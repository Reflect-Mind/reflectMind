package cn.edu.lingnan.utils;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/31.
 * 本地工程配置文件
 * 负责桌面环境的一些选择配置
 *
 */
class Config implements Serializable{

    private static Config config = null;


    /**
     * 为桌面环境配置初始化
     * 一个config对象
     */
    private Config(){}
    static Config getInstance(){
        if (config == null){

        }
        return config;
    }
}
