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
public class TXT extends FileType {
    @Override
    public String readContent(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()){
            stringBuilder.append(scanner.nextLine());
            stringBuilder.append("\n");
        }
        scanner.close();
        return stringBuilder.toString();
    }

}
