package cn.edu.lingnan.controller;

import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.service.command.TextWorkspaceRightCommand;
import cn.edu.lingnan.utils.R;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2018/2/17.
 */
public class TextWorkspaceRightController extends Controller {

    @FXML
    private TreeTableView frqTable;
    @FXML
    private TreeTableColumn<Vocab, String> contentColumn;
    @FXML
    private TreeTableColumn<Vocab, String> wordAppearColumn;
    @FXML
    private TreeTableColumn<Vocab, String> wordCategoryColumn;
    @FXML
    private TreeTableColumn<Vocab, String> wordThemeColumn;

    //TreeItem: root
    TreeItem<Vocab> root =
            new TreeItem<>( new Vocab());

    //调用service层的TextWorkspaceRightCommand
    private TextWorkspaceRightCommand textWorkspaceRightCommand =
            new TextWorkspaceRightCommand();

    //词频查询实时化
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //鼠标进入frqTable时执行查询或分析
        this.frqTable.setOnMouseEntered(event -> {

            Task<List<Vocab>> task =
                this.textWorkspaceRightCommand.getVacabTask();

            new Thread(task).start();

            //执行失败时进行报告
            task.setOnFailed(e -> {
                System.out.println(task.getException());
            });

            //key: 执行成功时的事件
            task.setOnSucceeded( e -> this.updateTreeItem());
        });

    }


    private void updateTreeItem(){

        //调用TextWorkspaceRightCommand获得frqTable
        List<Vocab> voc =
                textWorkspaceRightCommand.getWordTable();

        System.out.println("词汇统计执行完毕");

        //key: 扩展root结点
        root.setExpanded(true);

        //key: 将voc列表添加到root结点
        voc.stream().forEach((vo) -> {
            root.getChildren().add(new TreeItem<>(vo));
        });

        //单词内容
        contentColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Vocab, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getContent())
        );

        //单词频数
        wordAppearColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Vocab, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getAppearnum().toString())
        );

        //单词分类, bugged
//        wordCategoryColumn.setCellValueFactory(
//                (TreeTableColumn.CellDataFeatures<Vocab, String> param) ->
//                        new ReadOnlyStringWrapper(param.getValue().getValue()
//                                .getCategoryByCategoryId().getContent())
//        );

        //单词主题, bugged
//        wordThemeColumn.setCellValueFactory(
//                (TreeTableColumn.CellDataFeatures<Vocab, String> param) ->
//                        new ReadOnlyStringWrapper(param.getValue().getValue()
//                        .getCategoryByCategoryId().getThemeByThemeId().getContent())
//        );


        //key: 将root添加到TreeTableView
        frqTable.setRoot(root);
        //key: 将Column添加到treeTableView
        frqTable.getColumns().setAll(contentColumn, wordAppearColumn, wordCategoryColumn, wordThemeColumn);
        frqTable.setShowRoot(false);
    }
}


