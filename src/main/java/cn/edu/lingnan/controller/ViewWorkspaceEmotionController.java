package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.Container.PhaseContainer;
import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.sdk.enumeration.ChartType;
import cn.edu.lingnan.sdk.enumeration.LineType;
import cn.edu.lingnan.service.command.ViewWorkspaceEmotionCommand;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import cn.edu.lingnan.view.DialogView;
import cn.edu.lingnan.view.WindowView;
import com.jfoenix.controls.JFXRadioButton;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import org.reactfx.EventStreams;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2018/2/17.
 * @author feng
 */
public class ViewWorkspaceEmotionController extends Controller {


    /**
     * 当前窗格的索引
     */
    private final int CURRENT_TAB_INDEX = 2;

    private Config config = R.getConfig();


    //人生阶段容器
    private PhaseContainer<Pair<Integer, IndexRange>> phaseContainer
            = config.getPhaseContainer();

    //受对象的话语所在的段落。
    ObservableList<Pair<Integer, IndexRange>> answers = config.getAnswers();

    //当前所处窗格索引属性
    IntegerProperty tabIndexProperty = this.config.currentTabIndexProperty();

    //上次被匹配的文章，用于避免本视图被多次重复刷新。
    private String lastArticle = "";

    //容器视图产生类
    private DialogView dialogView = new DialogView();

    //命令类
    private ViewWorkspaceEmotionCommand emotionCommand = new ViewWorkspaceEmotionCommand();

    // 该显示区域的中心结点属性
    private ObjectProperty<Node> centerProperty = null;

    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXRadioButton negtiveButton;
    @FXML
    private JFXRadioButton positiveButton;
    @FXML
    private JFXRadioButton sereneButton;
    @FXML
    private JFXRadioButton happyButton;
    @FXML
    private JFXRadioButton painButton;
    @FXML
    private JFXRadioButton sorrowButton;
    @FXML
    private JFXRadioButton angryButton;
    @FXML
    private JFXRadioButton fearButton;
    @FXML
    private JFXRadioButton hateButton;
    @FXML
    private JFXRadioButton likeButton;
    @FXML
    private JFXRadioButton ashamedButton;
    @FXML
    private JFXRadioButton anxiousButton;

    private CategoryAxis categoryAxis;

    private NumberAxis numberAxis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //人生阶段容器未被填充时触发
        EventStreams.changesOf(this.tabIndexProperty)
                .filter(tabIndex -> tabIndex.getNewValue().intValue() == CURRENT_TAB_INDEX)
                .subscribe(ch -> {
                    if (this.answers.size() != this.phaseContainer.size())
                        this.fixPhaseContainer();
                    else
                        this.updateMotionCharts();
                });

        this.centerProperty = this.borderPane.centerProperty();
        this.initChart();
    }

    /**
     * 根据按钮的是否被选择
     * 决定是否在
     * @param type
     * @param button
     */
    public void buildRelationshipBetweenChartAndButton(LineType type
            , JFXRadioButton button
            , ObservableList<XYChart.Series<String, Number>> seriesList){
        XYChart.Series<String, Number> series = this.emotionCommand.getChartData(type);
        button.setUserData(series);
        button.getProperties().put("LineType", type);
        if (button.isSelected()) {
            seriesList.add(series);
            this.buildToolTip(series, button);
        }
    }

    /**
     * 为每个数据点构建toolTip
     * @param series
     * @param button
     */
    public void buildToolTip(XYChart.Series<String, Number> series, JFXRadioButton button){
        LineType type = (LineType) button.getProperties().get("LineType");
        for (XYChart.Data<String, Number> data: series.getData())
            Tooltip.install(data.getNode(), new Tooltip(type.toString() + ": "  + data.getYValue()));
    }

    /**
     * 构建图表
     * @param seriesList
     */
    private void buildChart(ObservableList<XYChart.Series<String, Number>> seriesList){
        seriesList.clear();
        this.buildRelationshipBetweenChartAndButton(LineType.POSITIVE, this.positiveButton, seriesList);
        this.buildRelationshipBetweenChartAndButton(LineType.NEGTIVE, this.negtiveButton, seriesList);
        this.buildRelationshipBetweenChartAndButton(LineType.ANGRY, this.angryButton, seriesList);
        this.buildRelationshipBetweenChartAndButton(LineType.ANXIOUS, this.anxiousButton, seriesList);
        this.buildRelationshipBetweenChartAndButton(LineType.ASHAMED, this.ashamedButton, seriesList);
        this.buildRelationshipBetweenChartAndButton(LineType.FEAR, this.fearButton, seriesList);
        this.buildRelationshipBetweenChartAndButton(LineType.HAPPY, this.happyButton, seriesList);
        this.buildRelationshipBetweenChartAndButton(LineType.HATE, this.hateButton, seriesList);
        this.buildRelationshipBetweenChartAndButton(LineType.LIKE, this.likeButton, seriesList);
        this.buildRelationshipBetweenChartAndButton(LineType.PAIN, this.painButton, seriesList);
        this.buildRelationshipBetweenChartAndButton(LineType.SORROW, this.sorrowButton, seriesList);
        this.buildRelationshipBetweenChartAndButton(LineType.SERENE, this.sereneButton, seriesList);
    }
    /**
     * 初始化图表
     */
    private void initChart(){
        //图表类型绑定
        ObjectProperty<ChartType> chartTypeProperty =
                this.config.emotionChartProperty();
        this.switchChartView(chartTypeProperty.get().getChart());
        chartTypeProperty.addListener((observable, oldValue, newValue) -> {
            this.switchChartView(newValue.getChart());
        });
    }
    /**
     * 切换图表视图
     */
    private void switchChartView(XYChart<String, Number> chart){
        this.borderPane.setCenter(chart);
        this.categoryAxis = (CategoryAxis) chart.getXAxis();
        this.numberAxis = (NumberAxis) chart.getYAxis();
        chart.setTitle("情感分析图表");
    }

    /**
     * 跳出窗口要求进行人生阶段信息填充。
     * 回跳至文本域窗口
     *
     */
    private void fixPhaseContainer(){
        Alert alert = this.dialogView
                .showInformationDialog("尚未对段落进行人生阶段的划分, 请完善后在进行结果查看！");
        alert.showAndWait();

        WindowView windowView = new WindowView();
        windowView.showFillPhaseContainerView();
        this.tabIndexProperty.set(0);
    }
    /**
     * 鼠标经过该目标区域时自动触发
     * 当文本区域中字符串无更新时将直接返回
     * 否则保存字符串对象
     * 更新该区域的图表信息
     */
    private void updateMotionCharts(){
        String article = this.config.getTextProperty();
        if (article == null || article.equals(this.lastArticle))
            return;
        this.lastArticle = article;

        XYChart<String, Number> chart = (XYChart<String, Number>) this.centerProperty.get();
        Task<Void> task =
                this.emotionCommand.buildPhaseListTask();
        task.setOnSucceeded(event -> {
            ObservableList<XYChart.Series<String, Number>> seriesList = chart.getData();
            this.buildChart(seriesList);
        });
        task.setOnFailed(event -> {
            System.out.println(task.getException());
        });
    }

    /**
     * 切换图表折线是否显示事件
     * @param
     */
    @FXML
    private void toggleCategoryLine(ActionEvent event){
        XYChart<String, Number> chart = (XYChart<String, Number>) this.centerProperty.get();
        ObservableList<XYChart.Series<String, Number>> seriesList = chart.getData();
        try {
            JFXRadioButton button = (JFXRadioButton) event.getSource();
            button.setDisable(true);
            XYChart.Series<String, Number> series = (XYChart.Series<String, Number>) button.getUserData();
            if (button.isSelected()) {
                seriesList.add(series);
                this.buildToolTip(series, button);
            }
            else
                seriesList.remove(series);
            EventStreams.ticks(Duration.ofMillis(1000))
                    .subscribe(o -> button.setDisable(false));
        } catch (Exception e){
            e.printStackTrace();
            this.buildChart(seriesList);
        }
    }
}
