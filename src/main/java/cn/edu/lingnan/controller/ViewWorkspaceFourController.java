package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.command.ViewWorkspaceCharacterCommand;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import org.reactfx.EventStreams;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2018/2/17.
 *
 * 人格分析
 */
public class ViewWorkspaceFourController extends Controller {
    @FXML
    private PieChart pieChart;

    private Config config = R.getConfig();

    //当前卡片窗格索引
    private final int CURRENT_TAB_INDEX = 4;

    private StringProperty textProperty = this.config.textPropertyProperty();

    private String lastText = "";

    private ViewWorkspaceCharacterCommand command = new ViewWorkspaceCharacterCommand();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventStreams.changesOf(this.config.currentTabIndexProperty())
                .filter(numberChange ->
                        numberChange.getNewValue().intValue() == CURRENT_TAB_INDEX  && this.shouldUpdate())
                .subscribe(numberChange -> this.updateChart());

        this.init();
    }

    private void init(){
    }
    /**
     * 判断是否有修改产生，以决定是否更新图表
     * @return true 表示要更新图表
     */
    private boolean shouldUpdate(){
        String currentText = this.textProperty.get();
        if (currentText.equals(this.lastText))
            return false;
        this.lastText = currentText;
        return true;
    }
    /**
     * 更新图表
     */
    private void updateChart(){
        Task<ObservableList<Data>> task = this.command.getPieChartDataTask();
        task.setOnSucceeded(event -> {
            ObservableList<Data> datas = task.getValue();
            this.pieChart.setData(datas);

            for(PieChart.Data data: datas) {
                Tooltip tooltip = new Tooltip(Double.toString(data.getPieValue()));
                tooltip.setFont(Font.font(14));
                Tooltip.install(data.getNode(), tooltip);
            }
        });
        task.setOnFailed(event -> {
            task.getException().printStackTrace();
        });
    }


}
