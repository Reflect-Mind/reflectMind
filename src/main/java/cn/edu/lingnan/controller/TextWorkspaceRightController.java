package cn.edu.lingnan.controller;

import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.service.command.TextWorkspaceRightCommand;
import cn.edu.lingnan.utils.R;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
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
    private TreeTableView wordTable;
    @FXML
    private TreeTableColumn<Vocab, String> themeColumn;
    @FXML
    private TreeTableColumn<Vocab, String> categoryColumn;
    @FXML
    private TreeTableColumn<Vocab, String> appearColumn;
    @FXML
    private TreeTableColumn<Vocab, String> frqColumn;

    //TreeItem: root
    TreeItem<Vocab> root =
            new TreeItem<>( new Vocab());

    //调用service层的TextWorkspaceRightCommand
    private TextWorkspaceRightCommand textWorkspaceRightCommand =
            new TextWorkspaceRightCommand();

    //实时化
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //鼠标进入frqTable或wordTable时执行查询或分析
        this.wordTable.setOnMouseEntered(event -> {

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

        //调用TextWorkspaceRightCommand获得wordTable
        List<Vocab> voc =
                textWorkspaceRightCommand.getWordTable();

        System.out.println("词汇统计执行完毕");

        //key: 扩展root结点
        root.setExpanded(true);

        //key: 将voc列表添加到root结点
        voc.stream().forEach((vo) -> {
            root.getChildren().add(new TreeItem<>(vo));
        });

        //此列用content值填充
        themeColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Vocab, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getContent())
        );

        categoryColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Vocab, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getCategoryId().toString())
        );

        appearColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Vocab, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getAppearnum().toString())
        );

        frqColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<Vocab, String> param) ->
                        new ReadOnlyStringWrapper(param.getValue().getValue().getFrq().toString())
        );


        //key: 将root添加到TreeTableView
        wordTable.setRoot(root);
        //key: 将Column添加到treeTableView
        wordTable.getColumns().setAll(themeColumn, categoryColumn, appearColumn, frqColumn);
    }
}


