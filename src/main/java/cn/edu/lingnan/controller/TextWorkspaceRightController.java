package cn.edu.lingnan.controller;

import cn.edu.lingnan.pojo.*;
import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.service.command.TextWorkspaceRightCommand;
import cn.edu.lingnan.utils.R;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;
import org.reactfx.EventStreams;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2018/2/17.
 */
public class TextWorkspaceRightController extends Controller {

    //词频查询组件
    @FXML
    private TitledPane frqTitle;
    @FXML
    private TreeTableView frqTable;
    @FXML
    private TreeTableColumn<FrqTree, String> contentColumn;
    @FXML
    private TreeTableColumn<FrqTree, Number> wordAppearColumn;
    @FXML
    private TreeTableColumn<FrqTree, String> wordCategoryColumn;
    @FXML
    private TreeTableColumn<FrqTree, String> wordThemeColumn;
    //FrqTree's root
    TreeItem<FrqTree> root =
            new TreeItem<>( new FrqTree());

    //词汇统计组件
    @FXML
    private TitledPane wordTitle;
    @FXML
    private TreeTableView wordTable;
    @FXML
    private TreeTableColumn<PsychoTree, String> themeColumn;
    @FXML
    private TreeTableColumn<PsychoTree, String> categoryColumn;
    @FXML
    private TreeTableColumn<PsychoTree, Number> appearColumn;
    @FXML
    private TreeTableColumn<PsychoTree, Number> frqColumn;
    //PsychoTree's root
    TreeItem<PsychoTree> root1 =
            new TreeItem<>( new PsychoTree());

    //新词预测组件
    @FXML
    private TitledPane newWordTitle;
    @FXML
    private TreeTableView newWordTable;
    @FXML
    private TreeTableColumn<Vocab, String> newWordColumn;
    @FXML
    private TreeTableColumn<Vocab, Number> newAppearColumn;
    @FXML
    private TreeTableColumn<Vocab, Number> lengthColumn;
    @FXML
    private TreeTableColumn<Vocab, Number> newFrqColumn;
    //Vocab's root
    TreeItem<Vocab> root2 =
            new TreeItem<>( new Vocab());

    //调用service层的TextWorkspaceRightCommand
    private TextWorkspaceRightCommand textWorkspaceRightCommand =
            new TextWorkspaceRightCommand();


    //界面初始化方法
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //词频查询事件： 鼠标点击frqTitle时执行查询或分析
        this.frqTitle.setOnMouseClicked(event -> {

            Task<List<Vocab>> task =
                this.textWorkspaceRightCommand.getVocabTask();

            new Thread(task).start();

            //执行失败时进行报告
            task.setOnFailed(e -> {
                System.out.println(task.getException());
            });

            //key: 执行成功时的事件
            task.setOnSucceeded( e -> this.updateFrqTable());
        });

        //词汇统计事件： 鼠标点击wordTitle时执行查询或分析
        this.wordTitle.setOnMouseClicked(event -> {

            Task<List<Vocab>> task =
                    this.textWorkspaceRightCommand.getVocabTask();

            new Thread(task).start();

            //执行失败时进行报告
            task.setOnFailed(e -> {
                System.out.println(task.getException());
            });

            //key: 执行成功时的事件
            task.setOnSucceeded( e -> this.updatePsychoTable());
        });

        //新词事件： 鼠标点击newWordTitle时执行查询或分析
        this.newWordTitle.setOnMouseClicked(event -> {

            Task<List<Vocab>> task =
                    this.textWorkspaceRightCommand.getVocabTask();

            new Thread(task).start();

            //执行失败时进行报告
            task.setOnFailed(e -> {
                System.out.println(task.getException());
            });

            //key: 执行成功时的事件
            task.setOnSucceeded( e -> this.updateNewWordTable());
        });

    }


    //更新词频查询树表
    private void updateFrqTable() {

        //调用TextWorkspaceRightCommand获得frqTable
        List<FrqTree> voc =
                textWorkspaceRightCommand.getFrqTree();

        //扩展root结点
        root.setExpanded(true);

        //清空root的Children
        root.getChildren().remove( 0, root.getChildren().size() );

        //key: 将voc列表添加到root结点
        voc.stream().forEach((vo) -> {
            root.getChildren().add(new TreeItem<>(vo));
        });


        //单词内容
        contentColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FrqTree, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getContent())
        );

        //单词频数 Number
        wordAppearColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FrqTree, Number> param) ->
                        new ReadOnlyIntegerWrapper(param.getValue().getValue().getAppearnum())
        );

        //单词分类, testing
        wordCategoryColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FrqTree, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue()
                                .getCategory())
        );

        //单词主题, testing
        wordThemeColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FrqTree, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue()
                        .getTheme())
        );


        //key: 将root添加到TreeTableView
        frqTable.setRoot(root);
        //key: 将Column添加到treeTableView
        frqTable.getColumns().setAll(contentColumn, wordAppearColumn, wordCategoryColumn, wordThemeColumn);
        frqTable.setShowRoot(false);
    }


    //更新词汇统计树表
    private void updatePsychoTable() {

        //调用TextWorkspaceRightCommand获得PsychoTree
        List<PsychoTree> voc =
                textWorkspaceRightCommand.getPsychoTree();

        //扩展root1结点
        root1.setExpanded(true);

        //清空root的Children
        root1.getChildren().remove( 0, root1.getChildren().size() );

        //key: 将voc列表添加到root1结点
        voc.stream().forEach((vo) -> {
            root1.getChildren().add(new TreeItem<>(vo));
        });


        //主题
        themeColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<PsychoTree, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue()
                                .getTheme())
        );

        //分类
        categoryColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<PsychoTree, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue()
                                .getCategory())
        );

        //频数
        appearColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<PsychoTree, Number> param) ->
                        new ReadOnlyIntegerWrapper(param.getValue().getValue()
                                .getAppearnum())
        );

        //频率
        frqColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<PsychoTree, Number> param) ->
                        new ReadOnlyDoubleWrapper(param.getValue().getValue()
                                .getFrq())
        );

        //key: 将root1添加到TreeTableView
        wordTable.setRoot(root1);
        //key: 将Column添加到treeTableView,
        wordTable.getColumns().setAll(themeColumn, categoryColumn, appearColumn, frqColumn);
        wordTable.setShowRoot(false);

    }


    //更新词汇统计树表
    private void updateNewWordTable() {

        //调用TextWorkspaceRightCommand获得NewWordTree
        List<Vocab> voc =
                textWorkspaceRightCommand.getNewWordTree();

        //扩展root1结点
        root1.setExpanded(true);

        //清空root的Children
        root1.getChildren().remove( 0, root1.getChildren().size() );

        //key: 将voc列表添加到root1结点
//        voc.stream().forEach((vo) -> {
//            root1.getChildren().add(new TreeItem<Vocab>(vo));
//        });


        //主题
        themeColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<PsychoTree, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue()
                                .getTheme())
        );

        //分类
        categoryColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<PsychoTree, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue()
                                .getCategory())
        );

        //频数
        appearColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<PsychoTree, Number> param) ->
                        new ReadOnlyIntegerWrapper(param.getValue().getValue()
                                .getAppearnum())
        );

        //频率
        frqColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<PsychoTree, Number> param) ->
                        new ReadOnlyDoubleWrapper(param.getValue().getValue()
                                .getFrq())
        );

        //key: 将root1添加到TreeTableView
        wordTable.setRoot(root1);
        //key: 将Column添加到treeTableView,
        wordTable.getColumns().setAll(themeColumn, categoryColumn, appearColumn, frqColumn);
        wordTable.setShowRoot(false);

    }


}


