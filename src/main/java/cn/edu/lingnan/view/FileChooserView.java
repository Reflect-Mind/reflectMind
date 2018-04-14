package cn.edu.lingnan.view;

import cn.edu.lingnan.sdk.document.DocumentFactory;
import cn.edu.lingnan.sdk.document.Docx;
import cn.edu.lingnan.sdk.document.FileType;
import cn.edu.lingnan.sdk.document.Txt;
import cn.edu.lingnan.utils.R;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.apache.tika.Tika;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Administrator on 2018/2/13.
 * @author feng
 */
public class FileChooserView{

    /**
     * 上次被打开的文件
     */
    private File openedFile = null;

    //文件过滤器类型
    public interface ExtensionFilter{
        FileChooser.ExtensionFilter DEFAULT = new FileChooser.ExtensionFilter("ALL"
                , "*.*");
        FileChooser.ExtensionFilter XML_FILTER = new FileChooser.ExtensionFilter("XML files(*.xml)"
                , "*.xml");
        FileChooser.ExtensionFilter HTML_FILTER = new FileChooser.ExtensionFilter("HTML files(*.xml)"
                , Arrays.asList("*.html", "*.htm"));
        FileChooser.ExtensionFilter DOC_FILTER = new FileChooser.ExtensionFilter("DOCX files(*.docx)"
                , "*.docx");
        FileChooser.ExtensionFilter TXT_FILTER = new FileChooser.ExtensionFilter("TXT files(*.txt)"
                , "*.txt");
        FileChooser.ExtensionFilter AUDIO_FILTER = new FileChooser.ExtensionFilter("AUDIO files"
                , "*.mp3", "*.wma");

    }

    public enum FileChooserType{
        SINGLE_SELECT_DIALOG,
        MULTIPLE_SELECT_DIALOG,
        SAVE_DIALOG
    }

    private List<File> openFileChooser(FileChooserType type , File defaultDirectory, FileChooser.ExtensionFilter... filters){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(defaultDirectory);
        fileChooser.getExtensionFilters().addAll(filters);
        if (type == FileChooserType.SINGLE_SELECT_DIALOG)
            return Arrays.asList(fileChooser.showOpenDialog(R.getApplication().getStage()));
        else if (type == FileChooserType.SINGLE_SELECT_DIALOG)
            return fileChooser.showOpenMultipleDialog(R.getApplication().getStage());
        else
            return Arrays.asList(fileChooser.showSaveDialog(R.getApplication().getStage()));
    }

    /**
     * 打开逐字稿
     * @return 返回逐字稿File对象
     */
    public File openManuscriptDialog(){

        List<File> files = this.openFileChooser(FileChooserType.SINGLE_SELECT_DIALOG, this.openedFile,
                ExtensionFilter.TXT_FILTER, ExtensionFilter.DOC_FILTER);
        if (files.size() == 0)
            return null;
        File file = files.get(0);
        return file;
    }

    /**
     * 打开音频的文件选择对话框
     * @return
     */
    public File openAudioDialog(){
        List<File> files = this.openFileChooser(FileChooserType.SINGLE_SELECT_DIALOG, this.openedFile,
                ExtensionFilter.AUDIO_FILTER);
        if (files.size() == 0)
            return null;
        File file = files.get(0);
        return file;
    }

}
