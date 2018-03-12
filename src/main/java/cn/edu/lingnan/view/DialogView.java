package cn.edu.lingnan.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

/**
 * Created by Administrator on 2018/2/26.
 */
public class DialogView extends AbstractView {

    public TextInputDialog showTextInputDialog(){
        TextInputDialog dialog = new TextInputDialog("");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(true);
        dialog.getDialogPane().setExpandableContent(new Label("尚未确定项目工作目录,请选择工作目录"));
        GridPane grid = (GridPane) dialog.getDialogPane().getContent();
        grid.setHgap(3);
        grid.add(new Button("..."), 2, 0);
        dialog.getDialogPane().setExpanded(true);
        return  dialog;
    }
}