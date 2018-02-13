package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.advice.AbstractObservable;
import cn.edu.lingnan.sdk.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController extends Controller {

    @FXML
    private BorderPane borderPane;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    @FXML
    private void handleContextMenu(MouseEvent mouseEvent){
        System.out.println(mouseEvent);
    }

    @Override
    public <T> void update(AbstractObservable observable, T observer, Object... args) {
        System.out.println("rootController收到通知, 正在响应");
    }
}
