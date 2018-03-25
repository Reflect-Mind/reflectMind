package cn.edu.lingnan.utils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.io.File;

/**
 * Created by Administrator on 2018/3/21.
 * @author feng
 * 文件基本输出输入操作类
 */
public class IOUtils {

    /**
     *
     * @return
     */
    public static boolean writeToTempFile(){

        return false;
    }

    /**
     * 获取临时性文件名后缀.
     * @param suffix
     * @param object
     * @return
     */
    public static File getTempFile(String suffix, Object object){
        File baseDirectory = new File("tmp");
        if (!baseDirectory.exists())
            baseDirectory.mkdir();
        String fileName = String.format("%s.%s", object.getClass().getSimpleName(), suffix);
        File file = new File(baseDirectory ,fileName);
        return file;
    }

}
