package cn.edu.lingnan.sdk.document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/3.
 */
public class DocumentFactory {

    public <T extends FileType> T createFileBuilder(Class<? extends FileType> clz){
        FileType instance = null;
        try {
            instance =  clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) instance;
    }
}
