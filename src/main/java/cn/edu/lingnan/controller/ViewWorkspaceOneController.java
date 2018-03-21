package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.service.command.ViewWorkspaceOneCommand;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import jdk.internal.util.xml.impl.Input;
import org.reactfx.EventStreams;
import org.reactfx.util.Try;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

/**
 * Created by Administrator on 2018/2/17.
 * @author feng
 */
public class ViewWorkspaceOneController extends Controller {


    @FXML
    private ImageView imageView ;
    @FXML
    private AnchorPane anchorPane;

    private Image image;
    private ViewWorkspaceOneCommand viewWorkspaceOneCommand = new ViewWorkspaceOneCommand();
    private Config config = R.getConfig();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventStreams.nonNullValuesOf(config.currentTabIndexProperty())
                .filter((number -> number.intValue() == 1))
                .supplyTask(() -> this.viewWorkspaceOneCommand.getImageFileTask(config.getTextProperty()))
                .await()
                .filterMap(fileTry -> {
                    if (fileTry.isSuccess())
                        return Optional.ofNullable(fileTry.get());
                    return Optional.empty();
                })
                .subscribe(this::updateWordCloud);
        this.init();
    }

    /**
     * 初始化组件属性
     */
    private void init(){
        this.imageView.setPreserveRatio(true);
    }
    /**
     * 更新词云
     */
    private void updateWordCloud(File file){
        try {
            InputStream inputStream = new FileInputStream(file);
            this.image = new Image(inputStream);
            this.imageView.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
