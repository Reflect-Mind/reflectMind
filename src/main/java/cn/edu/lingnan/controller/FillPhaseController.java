package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.Container.PhaseContainer;
import cn.edu.lingnan.sdk.Container.PhaseType;
import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.sdk.overlay.CustomList;
import cn.edu.lingnan.service.command.FillPhaseCommand;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
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
import javafx.util.Pair;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2018/3/30.
 * @author feng
 * 填充人生阶段视图控制器
 */
public class FillPhaseController extends Controller {


    private Config config = R.getConfig();

    private ObservableList<Pair<Integer, IndexRange>> answers = config.getAnswers();

    private StringProperty textProperty = config.textPropertyProperty();

    private final static DataFormat stringsDataFormat = new DataFormat("strings");
    //人生阶段容器
    private PhaseContainer<String ,Pair<Integer, IndexRange>> phaseContainer = config.getPhaseContainer();

    private BooleanProperty saveProperty = null;



    private FillPhaseCommand fillPhaseCommand = new FillPhaseCommand();
    @FXML
    private JFXListView<String> childhoodListView;
    @FXML
    private JFXListView<String> middleListView;
    @FXML
    private JFXListView<String> collegeListView;
    @FXML
    private JFXListView<String> workListView;
    @FXML
    private ListView<String> listView;
    @FXML
    private Button saveAndExit;
    @FXML
    private Button cancel;
    @FXML
    private Button save;
    @FXML
    private Button help;



    //gridView中的视图列表。
    private ObservableList<Color> colors;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initListView();
        this.initButton();
        this.setupViewEvent();
        this.setupCloseEvent();
    }

    /**
     * 初始化按钮
     */
    public void initButton(){
        this.saveProperty = this.save.disableProperty();
        this.saveProperty.set(true);
    }
    @FXML
    private void save(){

    }
    @FXML
    private void cancel(){

    }
    @FXML
    private void help(){

    }
    @FXML
    private void saveAndExit(){

    }
    /**
     * 设置本窗口关闭事件
     */
    public void setupCloseEvent(){
        Stage stage = this.getStage();
        stage.setOnCloseRequest(event -> {
        });
    }
    /**
     * 设置View的监听事件
     */
    public void setupViewEvent(){
        MultipleSelectionModel<String> selectionModel = this.listView.getSelectionModel();
        IntegerProperty currentParagraphProperty = config.currentParagraphProperty();
        //更改当前光标所在的段落
        selectionModel.selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            int index = this.fillPhaseCommand.getParagraphInString(newValue);
            currentParagraphProperty.set(index);
        }));

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
     * 设置拖拽事件的开始
     * @param listView
     */
    private void configureDragEmit(ListView<String> listView){
        //drag begin
        listView.setOnDragDetected(event -> {
            ObservableList<String> strings = listView.getSelectionModel().getSelectedItems();
            if (strings.size() == 0)
                return;
            List<String> list = new CustomList<>();
            list.addAll(strings);
            Dragboard dragboard = listView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(stringsDataFormat, list);
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
            Dragboard dragboard = event.getDragboard();
            //add
            List<String> strings = (List<String>) event.getDragboard()
                    .getContent(stringsDataFormat);
            listView.getItems().addAll(strings);
            //delete
            ListView source = (ListView) event.getGestureSource();
            source.getItems().removeAll(strings);
        });
    }
    /**
     * 更新listView.
     */
    public void initListView(){
        //多选
        this.listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.childhoodListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.middleListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.collegeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.workListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //数据填充
        Platform.runLater(() -> {
            ObservableList<String> strings = this.fillPhaseCommand
                    .getStringList(this.textProperty.get(), this.answers,phaseContainer);
            this.listView.setItems(strings);
            this.childhoodListView.getItems().addAll(this.phaseContainer.getPhase(PhaseType.CHILDHOOD));
            this.middleListView.getItems().addAll(this.phaseContainer.getPhase(PhaseType.MIDDLE));
            this.collegeListView.getItems().addAll(this.phaseContainer.getPhase(PhaseType.COLLEGE));
            this.workListView.getItems().addAll(this.phaseContainer.getPhase(PhaseType.WORK));
        });
    }
}
