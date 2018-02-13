package cn.edu.lingnan.sdk.document;

import cn.edu.lingnan.utils.R;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/3.
 */
public class DocumentTest {

    @Test
    public void readDocxDocumentTest(){
        InputStream inputStream = R.getResourceAsStream("text/test.docx");
        DocumentFactory factory = new DocumentFactory();
        String string = factory.createFileBuilder(Docx.class).readContent(inputStream);
        System.out.println(string);
    }
    @Test
    public void writeExcelDocumentTest() throws IOException {
        List<Object[]> list = new ArrayList<>();
        list.add(new Object[] { "编号", "姓名", "描述" });
        list.add(new Object[] { "10010", "李立", "技术经理" });
        DocumentFactory factory = new DocumentFactory();
        Xlsx xlsx = factory.createFileBuilder(Xlsx.class);
        File file = new File("D://te.xlsx");
        if (!file.exists())
            file.createNewFile();
        xlsx.writeContent(list, file);
    }
    @Test
    public void readTxtDocumentTest() throws FileNotFoundException {
        Txt txt = new DocumentFactory().createFileBuilder(Txt.class);
        String string = txt.readContent(new FileInputStream("D://outcome.txt"));
        List<String> list = new ArrayList<>();
        System.out.println(string);
    }
}
