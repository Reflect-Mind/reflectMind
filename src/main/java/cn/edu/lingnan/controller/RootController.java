package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.BreadCrumbBar;


import java.net.URL;
import java.util.ResourceBundle;

public class RootController extends Controller {

    @FXML
    private WorkspaceController workspaceController;
    @FXML
    private BreadCrumbBar<String> breadCrumb;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
