package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.Container.PhaseContainer;
import cn.edu.lingnan.sdk.Container.PhaseType;
import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.sdk.overlay.CustomList;
import cn.edu.lingnan.service.command.FillPhaseCommand;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import cn.edu.lingnan.view.DialogView;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;


import java.net.URL;
import java.util.*;

/**
 * Created by Administrator on 2018/3/30.
 * @author feng
 * 填充人生阶段视图控制器
 */
public class FillPhaseController extends Controller {


    private Config config = R.getConfig();

    private ObservableList<Pair<Integer, IndexRange>> answers = config.getAnswers();

    private StringProperty textProperty = config.textPropertyProperty();

    //人生阶段容器
    private PhaseContainer<Pair<Integer, IndexRange>> phaseContainer = config.getPhaseContainer();

    private BooleanProperty disableProperty = null;

    private FillPhaseCommand fillPhaseCommand = new FillPhaseCommand();

    //每个列表之间传输的数据
    private List<Pair<Integer, IndexRange>> transmittedData =
            new ArrayList<Pair<Integer, IndexRange>>(){
        public boolean addAll(Collection<? extends Pair<Integer, IndexRange>> c) {
            this.clear();
            return super.addAll(c);
        }
    };

    private DialogView dialogView = new DialogView();

    @FXML
    private JFXListView<Pair<Integer, IndexRange>> childhoodListView;
    @FXML
    private JFXListView<Pair<Integer, IndexRange>> middleListView;
    @FXML
    private JFXListView<Pair<Integer, IndexRange>> collegeListView;
    @FXML
    private JFXListView<Pair<Integer, IndexRange>> workListView;
    @FXML
    private ListView<Pair<Integer, IndexRange>> listView;
    @FXML
    private Button save;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initListView();
        this.initButton();
        this.setupViewEvent();
    }

    /**
     * 初始化按钮
     * 保存按钮初始化为不能使用
     */
    public void initButton(){
        this.disableProperty = this.save.disableProperty();
        this.disableProperty.set(true);
    }

    /**
     * 保存用户的操作状态:
     * 把用户的操作的数据
     * displayProperty为true时已是不能掉用该方法
     * 保存完成时将disable按钮设置为true
     */
    @FXML
    private void save(){
        this.phaseContainer.clear();
        this.phaseContainer.addAll(PhaseType.CHILDHOOD, this.childhoodListView.getItems());
        this.phaseContainer.addAll(PhaseType.MIDDLE, this.middleListView.getItems());
        this.phaseContainer.addAll(PhaseType.COLLEGE, this.collegeListView.getItems());
        this.phaseContainer.addAll(PhaseType.WORK, this.workListView.getItems());
        this.disableProperty.set(true);
    }

    /**
     * 取消操作
     */
    @FXML
    private void cancel(){
        if (!this.disableProperty.get()){
            Alert alert = this.dialogView.showConfirmationDialog("尚未保存阶段分类，确认退出？");
            Optional<ButtonType> buttonType = alert.showAndWait();
            buttonType.ifPresent(type -> {
                if (type.getButtonData() == ButtonBar.ButtonData.OK_DONE)
                    this.getStage().hide();
            });
        }
        else
            this.getStage().hide();
    }
    @FXML
    private void help(){

    }

    /**
     * 保存并退出事件
     */
    @FXML
    private void saveAndExit(){
        if (!this.disableProperty.get())
            this.save();
        this.getStage().hide();

    }

    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        this.setupCloseEvent();
    }

    /**
     * 设置本窗口关闭事件
     */
    public void setupCloseEvent(){
        Stage stage = this.getStage();
        stage.setOnCloseRequest(event -> {
            if (!this.disableProperty.get()){
                Alert alert = this.dialogView
                        .showConfirmationDialog("尚未保存阶段分类，确认退出？");
                Optional<ButtonType> buttonType = alert.showAndWait();
                buttonType.ifPresent(type -> {
                    if (buttonType.get().getButtonData() != ButtonBar.ButtonData.OK_DONE)
                        event.consume();
                });
            }
        });
    }
    /**
     * 设置View的监听事件
     */
    public void setupViewEvent(){
        //设置listView点击事件
        this.showCurrentParagraph(this.listView);
        this.showCurrentParagraph(this.childhoodListView);
        this.showCurrentParagraph(this.middleListView);
        this.showCurrentParagraph(this.collegeListView);
        this.showCurrentParagraph(this.workListView);

        //设置listView值拖拽开始事件
        this.configureDragEmit(this.listView);
        this.configureDragEmit(this.childhoodListView);
        this.configureDragEmit(this.middleListView);
        this.configureDragEmit(this.collegeListView);
        this.configureDragEmit(this.workListView);
        //设置listView值拖拽结束事件
        this.configureDragAccept(this.listView);
        this.configureDragAccept(this.childhoodListView);
        this.configureDragAccept(this.middleListView);
        this.configureDragAccept(this.collegeListView);
        this.configureDragAccept(this.workListView);
    }

    /**
     * 为每个listView设置当前段落点击事件监听
     * @param listView
     */
    private void showCurrentParagraph(ListView listView){
        MultipleSelectionModel<Pair<Integer, IndexRange>> selectionModel = listView.getSelectionModel();
        IntegerProperty currentParagraphProperty = config.currentParagraphProperty();
        //更改当前光标所在的段落
        listView.setOnMouseClicked(event -> {
            Pair<Integer, IndexRange> pair = selectionModel.getSelectedItem();
            if (pair == null)
                return;
            int index = this.fillPhaseCommand.getParagraphInString(pair);
            currentParagraphProperty.set(index);

        });

    }
    /**
     * 设置拖拽事件的开始
     * @param listView
     */
    private void configureDragEmit(ListView<Pair<Integer, IndexRange>> listView){
        //drag begin
        listView.setOnDragDetected(event -> {
            ObservableList<Pair<Integer, IndexRange>> strings = listView.getSelectionModel().getSelectedItems();
            if (strings.size() == 0)
                return;
            this.transmittedData.addAll(strings);
            Dragboard dragboard = listView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("");
            dragboard.setContent(content);
        });
    }
    /**
     * 为每个listView配置拖拽接受事件
     * @param listView
     */
    private void configureDragAccept(ListView listView){
        listView.setOnDragOver(event -> event.acceptTransferModes(TransferMode.MOVE));
        listView.setOnDragDropped(event -> {
            //add
            listView.getItems().addAll(this.transmittedData);

            ListView source = (ListView) event.getGestureSource();
            //消除源列表所选移动项
            source.getSelectionModel().clearSelection();
            //delete
            source.getItems().removeAll(this.transmittedData);
            //使得按钮处于可以保存的状态
            source.scrollTo(0);
            this.disableProperty.set(false);
        });
    }
    /**
     * 更新listView.
     */
    public void initListView(){
        //设置列表的单元格显示工厂
        this.listView.setCellFactory(this.fillPhaseCommand.getCallback());
        this.childhoodListView.setCellFactory(this.fillPhaseCommand.getCallback());
        this.middleListView.setCellFactory(this.fillPhaseCommand.getCallback());
        this.collegeListView.setCellFactory(this.fillPhaseCommand.getCallback());
        this.workListView.setCellFactory(this.fillPhaseCommand.getCallback());
        //
        //多选
        this.listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.childhoodListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.middleListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.collegeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.workListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //数据填充
        Platform.runLater(() -> {
            ObservableList<Pair<Integer, IndexRange>> strings = this.fillPhaseCommand
                    .getStringList(this.answers,phaseContainer);
            this.listView.setItems(strings);
            this.childhoodListView.getItems().addAll(this.phaseContainer.getPhase(PhaseType.CHILDHOOD));
            this.middleListView.getItems().addAll(this.phaseContainer.getPhase(PhaseType.MIDDLE));
            this.collegeListView.getItems().addAll(this.phaseContainer.getPhase(PhaseType.COLLEGE));
            this.workListView.getItems().addAll(this.phaseContainer.getPhase(PhaseType.WORK));
        });
    }
}
