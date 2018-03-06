package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.utils.R;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2018/2/18.
 * 事件控制器
 */
public class EventController extends Controller {
    private ObservableList<Task> taskObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SimpleObjectProperty<Task> taskSimpleObjectProperty = (SimpleObjectProperty<Task>)
                R.getParameters("currentTask");
        //为当前任务添加监听者,方便进行通知
        taskSimpleObjectProperty.addListener(((observable, oldValue, newValue) -> {

        }));
    }
}
