package cn.edu.lingnan.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/6.
 * 全局配置类
 * JAXB
 * @author 李田锋
 */

@XmlRootElement
public class Global implements Serializable {

    Global() {}

    /**
     * 本程序识别的项目文件后缀
     */
    private final static String RECOGNIZE_SUFFIX = "rmd";

    /**
     * config实例路径
     */
    @XmlElement
    private String currentConfigPath = "tmp/default." + RECOGNIZE_SUFFIX;




    public String getCurrentConfigPath() {
        return currentConfigPath;
    }
}
