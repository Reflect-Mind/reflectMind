package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.sdk.enumeration.WordType;
import cn.edu.lingnan.command.ViewWorkspaceOutlineCommand;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.reactfx.EventStreams;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2018/2/17.
 * @author feng
 */
public class ViewWorkspaceOutlineController extends Controller {

    private Image image;
    private ViewWorkspaceOutlineCommand viewWorkspaceOutlineCommand = new ViewWorkspaceOutlineCommand();
    private Config config = R.getConfig();
    private SimpleObjectProperty<WordType> wordTypeProperty = this.config.wordTypeProperty();

    @FXML
    private ImageView imageView ;
    @FXML
    private SplitMenuButton splitMenuButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        EventStreams.nonNullValuesOf(config.currentTabIndexProperty())
                .filter((number -> number.intValue() == 1))
                .supplyTask(() -> this.viewWorkspaceOutlineCommand.getImageFileTask(config.getTextProperty()))
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
        //
        this.imageView.setPreserveRatio(true);
        this.addMenuItemEvent();
        this.addWordTypeEvent();
    }

    /**
     * 为wordTypeProperty添加事件到响应
     */
    private void addWordTypeEvent(){
        this.wordTypeProperty.addListener((observable, oldValue, newValue) -> {
            Task<File> task = this.viewWorkspaceOutlineCommand.getImageFileTask(
                    this.config.getTextProperty()
            );
            task.setOnSucceeded(event -> {
                File file = task.getValue();
                this.updateWordCloud(file);
            });
        });
    }
    /**
     * 为MenuItem添加事件响应
     * 并将词云词汇显示类型为name
     * 对应到类型
     */
    private void addMenuItemEvent(){
        this.splitMenuButton.getItems().forEach(menuItem ->
            menuItem.setOnAction(event -> {
                String name = menuItem.getText();
                this.splitMenuButton.setText(name);
                for (WordType type: WordType.values()){
                    if (type.toString().equals(name)){
                        this.wordTypeProperty.set(type);
                        break;
                    }
                }
            }));
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
