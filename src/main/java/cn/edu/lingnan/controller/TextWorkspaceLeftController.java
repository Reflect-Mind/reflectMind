package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang.ObjectUtils;

import java.net.URL;
import java.security.Key;
import java.util.List;
import java.util.ResourceBundle;

/*  Created by Junhui
*
*/
public class TextWorkspaceLeftController extends Controller {
    /*
        搜索方面
     */
    @FXML
    private TabPane SearchPane;
    @FXML
    private Tab SearchTab;
    @FXML
    private AnchorPane SearchAnchor;
    @FXML
    private TextField SearchBar;
    @FXML
    private Button SearchUp;
    @FXML
    private Button SearchDown;
    @FXML
    private Button SearchBtn;
    @FXML
    private CheckBox AllWords1;
    @FXML
    private CheckBox UpperWords1;
    @FXML
    private CheckBox LoopWords1;
    @FXML
    private CheckBox ReverseWords1;

    /*
        标记方
     */
    @FXML
    private Tab SignTab;
    @FXML
    private AnchorPane SignAnchor;
    @FXML
    private TextField SignBar;
    @FXML
    private Button SignUp;
    @FXML
    private Button SignDown;
    @FXML
    private Button SignBtn;
    @FXML
    private Button SignClear;
    @FXML
    private CheckBox AllWords2;
    @FXML
    private CheckBox UpperWords2;
    @FXML
    private CheckBox LoopWords2;

    /*
        替换方
     */
    @FXML
    private Tab ReplaceTab;
    @FXML
    private AnchorPane ReplaceAnchor;
    @FXML
    private TextField ReplaceBar1;
    @FXML
    private TextField ReplaceBar2;
    @FXML
    private Button ReplaceUp;
    @FXML
    private Button ReplaceDown;
    @FXML
    private Button Btn;
    @FXML
    private Button ReplaceBtn;
    @FXML
    private Button ReplaceAllBtn;
    @FXML
    private CheckBox AllWords3;
    @FXML
    private CheckBox UpperWords3;
    @FXML
    private CheckBox LoopWords3;

    //预览区
    @FXML
    private TabPane ViewPane;

    //文章预览
    @FXML
    private Tab ArticleTab;
    @FXML
    private ListView<String> ArticleList;

    //结果预览
    @FXML
    private Tab ResultTab;
    @FXML
    private ListView<String> ResultList;

    private String textline1,textline2;

    private Boolean All,Upper,Loop,Reverse;

    public void initialize(URL location, ResourceBundle resources){
        /*
            搜索事件
         */
        SearchBtn.setOnMouseClicked(event -> {
            textline1=SearchBar.getText();
            All=AllWords1.isSelected();
            Upper=UpperWords1.isSelected();
            Loop=LoopWords1.isSelected();
            Reverse=ReverseWords1.isSelected();
            R.getConfig().searchTextProperty().set(textline1);
            SingleSelectionModel<Tab> Selection=ViewPane.getSelectionModel();
            Selection.select(ReplaceTab);
        });
        SearchBar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                R.getConfig().searchTextProperty().set(null);
            }
        });
        SearchUp.setOnMouseClicked(event -> {

        });
        SearchDown.setOnMouseClicked(event -> {

        });


        /*
            标记事件
         */
        SignBtn.setOnMouseClicked(event -> {
            textline1=SignBar.getText();
            All=AllWords2.isSelected();
            Upper=UpperWords2.isSelected();
            Loop=LoopWords2.isSelected();
            R.getConfig().searchTextProperty().set(textline1);
            SingleSelectionModel<Tab> Selection=ViewPane.getSelectionModel();
            Selection.select(ReplaceTab);
        });
        SignBar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                R.getConfig().searchTextProperty().set(null);
            }
        });
        SignUp.setOnMouseClicked(event -> {

        });
        SignDown.setOnMouseClicked(event -> {

        });
        SignClear.setOnMouseClicked(event -> {

        });


        /*
            替换事件
         */
        Btn.setOnMouseClicked(event -> {
            textline1=ReplaceBar1.getText();
            textline2=ReplaceBar2.getText();
            All=AllWords3.isSelected();
            Upper=UpperWords3.isSelected();
            Loop=LoopWords3.isSelected();
            R.getConfig().searchTextProperty().set(textline1);
            SingleSelectionModel<Tab> Selection=ViewPane.getSelectionModel();
            Selection.select(ReplaceTab);
        });
        ReplaceBar1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                R.getConfig().searchTextProperty().set(null);
            }
        });
        ReplaceBar2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                textline2=ReplaceBar2.getText();
            }
        });
        ReplaceUp.setOnMouseClicked(event -> {

        });
        ReplaceDown.setOnMouseClicked(event -> {

        });
        ReplaceBtn.setOnMouseClicked(event -> {

        });
        ReplaceAllBtn.setOnMouseClicked(event -> {

        });
    }
}
