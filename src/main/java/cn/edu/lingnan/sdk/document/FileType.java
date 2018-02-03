package cn.edu.lingnan.sdk.document;

import jdk.internal.util.xml.impl.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2018/2/3.
 */
public abstract class FileType {


    public abstract String readContent(InputStream inputStream);
}
