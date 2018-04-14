package cn.edu.lingnan.sdk.document;

import cn.edu.lingnan.utils.R;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2018/2/3.
 */
public class Docx extends FileType {

    @Override
    public String readContent(InputStream inputStream) {

        XWPFDocument document = null;
        try {
            document = new XWPFDocument(inputStream);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            return extractor.getText().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
