package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import org.controlsfx.control.HiddenSidesPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2018/2/17.
 */
public class TextWorkspaceController extends Controller {

    @FXML
    private HiddenSidesPane hiddenSidesPane;
    @FXML
    private StyleClassedTextArea textArea;
    @FXML
    private TextWorkspaceRightController textWorkspaceRightController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.hiddenSidesPane.getRight().setOnMouseEntered(event -> {
            this.hiddenSidesPane.setPinnedSide(Side.RIGHT);
        });
        this.hiddenSidesPane.getRight().setOnMouseExited(event -> {
            this.hiddenSidesPane.setPinnedSide(null);
        });
    }
}
