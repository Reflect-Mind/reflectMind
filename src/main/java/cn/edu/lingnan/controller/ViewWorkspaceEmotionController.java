package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.Container.PhaseContainer;
import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import cn.edu.lingnan.view.DialogView;
import cn.edu.lingnan.view.WindowView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.IndexRange;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.util.Pair;
import org.controlsfx.control.GridView;
import org.controlsfx.control.PopOver;
import org.reactfx.Change;
import org.reactfx.EventStreams;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
    private PhaseContainer<String ,Pair<Integer, IndexRange>> phaseContainer
            = config.getPhaseContainer();

    //受对象的话语所在的段落。
    ObservableList<Pair<Integer, IndexRange>> answers = config.getAnswers();

    //当前所处窗格索引属性
    IntegerProperty tabIndexProperty = this.config.currentTabIndexProperty();

    //上次被匹配的文章，用于避免本视图被多次重复刷新。
    private String lastArticle = "";

    //容器视图产生类
    private DialogView dialogView = new DialogView();

    @FXML
    private AnchorPane anchorPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //人生阶段容器未被填充时触发
        EventStreams.changesOf(this.tabIndexProperty)
                .filter(tabIndex -> tabIndex.getNewValue().intValue() == CURRENT_TAB_INDEX)
                .filter(ch -> !this.phaseContainer.containAll(this.answers))
                .subscribe(this::fixPhaseContainer);

    }

    /**
     * 跳出窗口要求进行人生阶段信息填充。
     * 回跳至原来的窗口
     * @param ch
     */
    private void fixPhaseContainer(Change<Number> ch){
//        Alert alert = this.dialogView
//                .showInformationDialog("尚未对段落进行人生阶段的划分, 请完善后在进行结果查看！");
//        alert.showAndWait();
        WindowView windowView = new WindowView();
        windowView.showFillPhaseContainerView();

        this.tabIndexProperty.set(ch.getOldValue().intValue());
    }
    /**
     * 鼠标经过该目标区域时自动触发
     * 当文本区域中字符串无更新时将直接返回
     * 否则保存字符串对象
     * 更新该区域的图表信息
     * @param mouseEvent 事件封装对象。
     */
    @FXML
    private void updateMotionCharts(MouseEvent mouseEvent){
        String article = this.config.getTextProperty();
        if (article == null || article.equals(this.lastArticle))
            return;
        this.lastArticle = article;

    }
}
