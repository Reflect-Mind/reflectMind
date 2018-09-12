package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.sdk.overlay.AudioPlayer;
import cn.edu.lingnan.command.RootCommand;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import cn.edu.lingnan.view.DialogView;
import cn.edu.lingnan.view.FileChooserView;
import cn.edu.lingnan.view.WindowView;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created By Feng on 2018/4/14
 * @author feng
 */
public class RootController extends Controller {

    /**
     * 文件选择对话框生成类
     */
    private FileChooserView fileChooserView = new FileChooserView();

    /**
     * 普通对话框生成类
     */
    private DialogView dialogView = new DialogView();
    /**
     *
     */
    private WindowView windowView = new WindowView();

    private RootCommand command = new RootCommand();

    private Config config = R.getConfig();

    /**
     * 文本字符串属性
     */
    private StringProperty textProperty = this.config.textPropertyProperty();

    /**
     * 播放器类
     */
    private AudioPlayer audioPlayer = this.config.getAudioPlayer();

    @FXML
    private MenuItem playMenuItem;
    @FXML
    private MenuItem previousMenuItem;
    @FXML
    private MenuItem forwardMenuItem;
    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initAudioManipulate();
    }

    /**
     * 初始化音频操作
     */
    public void initAudioManipulate(){
        this.playMenuItem.setDisable(true);
        this.previousMenuItem.setDisable(true);
        this.forwardMenuItem.setDisable(true);

        this.audioPlayer.openProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue){
                this.playMenuItem.setDisable(false);
                this.previousMenuItem.setDisable(false);
                this.forwardMenuItem.setDisable(false);
            }
            else {
                this.playMenuItem.setDisable(true);
                this.previousMenuItem.setDisable(true);
                this.forwardMenuItem.setDisable(true);
            }

        });

        this.playMenuItem.setOnAction(event -> {
            String text = this.playMenuItem.getText();
            if (text.equals("播放")){
                this.playMenuItem.setText("暂停");
                this.audioPlayer.play();
            }
            else{
                this.playMenuItem.setText("播放");
                this.audioPlayer.pause();
            }
        });
        this.vBox.setOnKeyReleased(event -> {
            if (!event.isControlDown())
                return;
            if (!this.audioPlayer.openProperty().get())
                return;
            switch (event.getCode()){
                case LEFT:
                    this.audioPlayer.backward(3);
                    break;
                case RIGHT:
                    this.audioPlayer.forward(3);
                    break;
            }
        });
    }

    /**
     * 导入逐字稿
     */
    @FXML
    public void handleOpenScript(){
        File file = this.fileChooserView.openManuscriptDialog();
        Task<String> task = this.command.getStringFromFileTask(file);
        task.setOnSucceeded(event -> this.textProperty.set(task.getValue()));
        task.setOnFailed(event -> {
            System.out.println(task.getException());
            this.dialogView.showInformationDialog("打开错误, 请检查！！！");
        });

    }

    /**
     * 导入音频
     */
    @FXML
    private void handleOpenAudio(){
        File file = this.fileChooserView.openAudioDialog();
        if (file == null)
            return;
        this.audioPlayer.open(file);
    }

    /**
     * 打开关于对话框
     *
     */
    @FXML
    private void handleOpenAboutDialog(){
        try {
            this.windowView.showAboutView();
        } catch (IOException e) {
            this.dialogView.showInformationDialog("内部有误...");
        }
    }
}
