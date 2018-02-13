package cn.edu.lingnan.view;

import cn.edu.lingnan.utils.R;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/2/13.
 */
public class FileChooserView extends AbstractView {

    //文件过滤器类型
    public interface ExtensionFilter{
        FileChooser.ExtensionFilter DEFAULT = new FileChooser.ExtensionFilter("ALL", "*.*");
        FileChooser.ExtensionFilter XML_FILTER = new FileChooser.ExtensionFilter("XML files(*.xml)", "*.xml");
        FileChooser.ExtensionFilter HTML_FILTER = new FileChooser.ExtensionFilter("HTML files(*.xml)"
                , Arrays.asList("*.html", "*.htm"));
    }

    public enum FileChooserType{
        SINGLE_DIALOG,
        MULTIPLE_DIALOG,
        SAVE_DIALOG
    }

    public void SimpleDialog(String content){

        Alert alert = new Alert(Alert.AlertType.INFORMATION, content);
        alert.setTitle("提示");
        alert.setHeaderText("消息框");
        alert.showAndWait();
    }

    public List<File> openFileChooser(FileChooserType type , File defaultDirectory, FileChooser.ExtensionFilter... filters){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(defaultDirectory);
        fileChooser.getExtensionFilters().addAll(filters);
        if (type == FileChooserType.SINGLE_DIALOG)
            return Arrays.asList(fileChooser.showOpenDialog(R.getApplication().getStage()));
        else if (type == FileChooserType.MULTIPLE_DIALOG)
            return fileChooser.showOpenMultipleDialog(R.getApplication().getStage());
        else
            return Arrays.asList(fileChooser.showSaveDialog(R.getApplication().getStage()));
    }

}
