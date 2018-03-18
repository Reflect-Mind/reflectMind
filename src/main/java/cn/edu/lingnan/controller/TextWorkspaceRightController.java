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
    private TreeTableView wordTable;
    @FXML
    private TreeTableColumn<Vocab, String> themeColumn;
    @FXML
    private TreeTableColumn<Vocab, String> categoryColumn;
    @FXML
    private TreeTableColumn<Vocab, String> appearColumn;
    @FXML
    private TreeTableColumn<Vocab, String> frqColumn;

    //调用service层的TextWorkspaceRightCommand
    private TextWorkspaceRightCommand textWorkspaceRightCommand =
            new TextWorkspaceRightCommand();

    //实时化
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        Task<List<Vocab>> task =
                this.textWorkspaceRightCommand.getVacabTask();

        //鼠标进入时执行查询或分析
        this.wordTable.setOnMouseEntered(event -> {
            new Thread(task).start();
        });

        //执行失败时进行报告
        task.setOnFailed(event -> {
            System.out.println(task.getException());
        });

        //key: 执行成功时的事件
        task.setOnSucceeded( event -> {

            //调用TextWorkspaceRightCommand获得wordTable
            List<Vocab> voc =
                    textWorkspaceRightCommand.getWordTable();

            for( int i=0; i < voc.size(); i++ ) {
                String str = voc.get(i).getContent();
                System.out.println("--------" + str );
            }

        }
        );
    }
}



//--2018/3/14: 词汇表vocabs
//    private ObservableList<Vocab> vocabs = null;

//--2018/3/14: 向列注入数据
//          //第4列：frqColumn, getFrq
//            this.frqColumn.setCellValueFactory( (
//                TreeTableColumn.CellDataFeatures<Vocab, String> param) -> new ReadOnlyStringWrapper(
//                    param.getValue().getValue().getFrq().toString()));


