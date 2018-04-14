package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.sdk.overlay.AudioPlayer;
import cn.edu.lingnan.service.command.RootCommand;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import cn.edu.lingnan.view.DialogView;
import cn.edu.lingnan.view.FileChooserView;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.BreadCrumbBar;


import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
}
