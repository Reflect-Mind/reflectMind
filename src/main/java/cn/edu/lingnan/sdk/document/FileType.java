package cn.edu.lingnan.sdk.document;

import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.util.List;

/**
 * Created by Administrator on 2018/2/3.
 */
public abstract class FileType {


    public abstract String readContent(InputStream inputStream);

    public String readContent(File file) throws FileNotFoundException {

        FileInputStream fileInputStream = new FileInputStream(file);
        return this.readContent(fileInputStream);
    }
}
