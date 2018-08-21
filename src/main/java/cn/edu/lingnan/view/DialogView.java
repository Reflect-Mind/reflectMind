package cn.edu.lingnan.view;

import javafx.scene.control.Alert;
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

    /**
     * 生成信息展示对话框
     * @param contentText 将要被展示的信息
     * @return
     */
    public Alert showInformationDialog(String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, contentText);
        alert.initOwner(this.stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        return alert;
    }

    /**
     * 生成信息确认对话框
     * @param contentText 将要被用户确认的信息
     * @return
     */
    public Alert showConfirmationDialog(String contentText){
        Alert alert = this.showInformationDialog(contentText);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        return alert;
    }

}
