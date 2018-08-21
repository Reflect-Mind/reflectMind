package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.service.command.ViewWorkspaceThemeCommand;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by Administrator on 2018/4/11.
 * @author feng
 */
public class ViewWorkspaceThemeController extends Controller {

    /**
     * 该控制器对应的fxml视图配置文件在tabPane中的位置
     */
    private final int TAB_INDEX = 5;

    private String lastText = null;

    private Config config = R.getConfig();

    private IntegerProperty currentTabIndexProperty = this.config.currentTabIndexProperty();

    private StringProperty textProperty = this.config.textPropertyProperty();

    private ViewWorkspaceThemeCommand command = new ViewWorkspaceThemeCommand();

    @FXML
    private XYChart<String, Number> chart;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.notifyController();
        this.initCategoryAxis(null);
        this.chart.setTitle("正负评价分析");
    }

    /**
     * 初始化分类对轴的数据
     * 不满为6个
     * 满了自增
     * @param seriesList
     */
    public void initCategoryAxis(ObservableList<XYChart.Series<String, Number>> seriesList){
        CategoryAxis axis = (CategoryAxis) this.chart.getXAxis();
        ObservableList<String> categoryNameList = axis.getCategories();
        categoryNameList.clear();
        if (seriesList == null || (seriesList.size() != 0 && seriesList.get(0).getData().size() < 6)) {
            axis.setAutoRanging(false);
            for (int i = 0; i <= 6; i++)
                categoryNameList.add("行号" + i);
            axis.setCategories(categoryNameList);
        }
        else
            axis.setAutoRanging(true);


    }
    /**
     * 唤醒控制器
     */
    public void notifyController(){
        this.currentTabIndexProperty.addListener((observable, oldValue, newValue) -> {
            String currentText = this.textProperty.get();
            if (newValue.intValue() != TAB_INDEX)
                return;
            if (currentText.equals(this.lastText))
                return;
            this.lastText = currentText;
            this.updateChart();
        });
    }

    /**
     * 获取数据
     * 更新图表
     */
    private void updateChart(){
        Task<ObservableList<XYChart.Series<String, Number>>> task = this.command.getSeriesTask();
        task.setOnSucceeded(event -> {
            ObservableList<XYChart.Series<String, Number>> seriesList = task.getValue();
            this.initCategoryAxis(seriesList);
            this.chart.setData(seriesList);
            for (XYChart.Series<String, Number> series: seriesList)
                for (XYChart.Data<String, Number> data: series.getData()){
                    Tooltip tooltip = new Tooltip(data.getXValue() + ": " + data.getYValue());
                    Tooltip.install(data.getNode(), tooltip);
                }
        });
        task.setOnFailed(event -> {
            System.out.println(task.getException());
        });

    }
}
