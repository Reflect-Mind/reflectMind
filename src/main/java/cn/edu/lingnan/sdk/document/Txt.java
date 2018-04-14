package cn.edu.lingnan.sdk.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Administrator on 2018/2/3.
 */
public class Txt extends FileType {
    @Override
    public String readContent(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        String content = scanner.hasNext()? scanner.next(): null;
        return content;
    }

}
