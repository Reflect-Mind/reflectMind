package cn.edu.lingnan.sdk.document;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2018/2/3.
 */
public class Xlsx extends FileType {
    @Override
    public String readContent(InputStream inputStream) {
        return null;
    }

    /**
     * 将信息写出到xlsx文件中
     *  Object[] 大致格式如下：
     *  Object[] objects = new Object[] { "编号", "姓名", "描述" };
     *  Object[] objects = new Object[] { "10010", "李立", "技术经理" };
     *  Object[] objects = new Object[] { "10011", "张晓龙", "微信它爹" };
     * @param content
     * @param file
     * @return
     */
    public boolean writeContent(List<Object[]> content, File file) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        for (int count = 0; count < content.size(); count++){
            XSSFRow row = sheet.createRow(count);
            Object[] objects = content.get(count);
            for (int column = 0; column < objects.length; column++){
                Cell cell = row.createCell(column);
                cell.setCellValue(String.valueOf(objects[column]));
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)){
            workbook.write(outputStream);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
