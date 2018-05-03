package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import org.apache.commons.lang.ObjectUtils;

import java.net.URL;
import java.security.Key;
import java.util.List;
import java.util.ResourceBundle;

/*  Created by Junhui
*
*/
public class TextWorkspaceLeftController extends Controller {
    /*
        搜索方面
     */
    @FXML
    private TabPane SearchPane;
    @FXML
    private Tab SearchTab;
    @FXML
    private AnchorPane SearchAnchor;
    @FXML
    private TextField SearchBar;
    @FXML
    private Button SearchUp;
    @FXML
    private Button SearchDown;
    @FXML
    private Button SearchBtn;
    @FXML
    private Button ClearBtn;

    /*
        标记方
     */
    @FXML
    private Tab SignTab;
    @FXML
    private AnchorPane SignAnchor;
    @FXML
    private TextField SignBar;
    @FXML
    private Button SignBtn;
    @FXML
    private Button SignClear;

    /*
        替换方
     */
    @FXML
    private Tab ReplaceTab;
    @FXML
    private AnchorPane ReplaceAnchor;
    @FXML
    private TextField ReplaceBar1;
    @FXML
    private TextField ReplaceBar2;
    @FXML
    private Button ReplaceUp;
    @FXML
    private Button ReplaceDown;
    @FXML
    private Button Btn;
    @FXML
    private Button ReplaceBtn;
    @FXML
    private Button ReplaceAllBtn;
    //临时文本
    private String textline1,textline2;
    //临时索引
    private int indexnum;
    //获取当前搜索字符串索引
    private int searchindex;
    //关键词数量
    private IntegerProperty num=R.getConfig().searchingWordCountProperty();
    //临时搜索关键词列表




    //预览区
    @FXML
    private TabPane ViewPane;


    //文章预览
    @FXML
    private Tab ArticleTab;
    @FXML
    private ListView<Pair<Integer,String>> ArticleList;
    //临时表
    private ObservableList<Pair<Integer,String>> artlist=FXCollections.observableArrayList();


    //结果预览
    @FXML
    private Tab ResultTab;
    @FXML
    private ListView<String> ResultList;
    //临时表
    private ObservableList<String> reslist;


    private String lasttext;
    //访/受长度
    private int length;
    //临时文本
    private String lineword;
    //段落范围
    private IndexRange len;
    private String[] paras;
    //段落索引
    private Integer indexof;
    private StringProperty textword = R.getConfig().textPropertyProperty();



    public void initialize(URL location, ResourceBundle resources){
        Search();
        Sign();
        Replace();
        artle();
    }



    /*
        搜索事件
     */

    private void Search(){


        /*
        num.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("count: "+num.get());
            }
        });
        */

        /*
            查询按钮
         */
        SearchBtn.setOnMouseClicked(event -> {
            if(!SearchBar.getText().equals(null)){
                textline1=SearchBar.getText();
                R.getConfig().getSearchTextList().add(textline1);
                searchindex=R.getConfig().getSearchTextList().size();

                /*
                for(int i=0;i<R.getConfig().getSearchTextList().size();i++)
                    System.out.println("List "+i+" : "+R.getConfig().getSearchTextList().get(i));
                System.out.println("index: "+R.getConfig().searchingWordIndexProperty().get());
                */

                /*数据更新速率慢于该事件发生,waiting!!
                if(num.get()<=0) {
                    R.getConfig().searchTextProperty().set(null);
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("出错");
                    alert.setHeaderText("文中没有出现该字段");
                    alert.showAndWait();
                }
                else {
                    SingleSelectionModel<Tab> Selection = ViewPane.getSelectionModel();
                    Selection.select(ResultTab);
                }
                */
            }
        });

        /*
            输入框
         */
        SearchBar.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //if(!SearchBar.textProperty().get().equals(null)&&searchindex!=0)
                try {
                    R.getConfig().getSearchTextList().remove(searchindex - 1);
                }catch (Exception e){}
            }
        });

        /*
            查询上一个
         */
        SearchUp.setOnMouseClicked(event -> {
            indexnum=R.getConfig().searchingWordIndexProperty().get()-1;
            if(indexnum>=0) {
                R.getConfig().searchingWordIndexProperty().set(indexnum);
                System.out.println("index: " + indexnum);
            }
        });

        /*
           查询下一个
         */
        SearchDown.setOnMouseClicked(event -> {
            indexnum=R.getConfig().searchingWordIndexProperty().get()+1;
            if(indexnum<num.get()) {
                R.getConfig().searchingWordIndexProperty().set(indexnum);
                System.out.println("index: " + indexnum);
            }
        });

        /*
            清除按钮
         */
        ClearBtn.setOnMouseClicked(event -> {
            SearchBar.setText(null);
            if(searchindex>0)
                R.getConfig().getSearchTextList().remove(searchindex-1);
        });

    }

    /*
        标记事件
     */
    private void Sign(){
        SignBtn.setOnMouseClicked(event -> {
            if(!SignBar.getText().equals(null)){
                textline1=SignBar.getText();
                R.getConfig().searchTextProperty().set(textline1);
                R.getConfig().getSearchTextList().add(textline1);
            }
        });
        SignClear.setOnMouseClicked(event -> {
            SignBar.setText(null);
            R.getConfig().getSearchTextList().clear();
        });
    }

    /*
        替换事件
     */

    private void Replace(){
        Btn.setOnMouseClicked(event -> {
            if(!ReplaceBar1.getText().equals(null)){
                textline1=ReplaceBar1.getText();
                R.getConfig().searchTextProperty().set(textline1);
                if(R.getConfig().searchingWordCountProperty().get()<=0) {
                    R.getConfig().searchTextProperty().set(null);
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("出错");
                    alert.setHeaderText("文中没有出现该字段");
                    alert.showAndWait();
                }
                else {
                    SingleSelectionModel<Tab> Selection = ViewPane.getSelectionModel();
                    Selection.select(ResultTab);
                }
            }
            if(!ReplaceBar2.getText().equals(null)){
                textline2=ReplaceBar2.getText();
            }
        });
        ReplaceBar1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                R.getConfig().searchTextProperty().set(null);
            }
        });
        ReplaceUp.setOnMouseClicked(event -> {

        });
        ReplaceDown.setOnMouseClicked(event -> {

        });
        ReplaceBtn.setOnMouseClicked(event -> {

        });
        ReplaceAllBtn.setOnMouseClicked(event -> {

        });
    }

    /*
        文章预览
     */

    private void artle(){
        /*
            点击定位事件
        if(!ArticleList.getSelectionModel().isEmpty()) {
            indexof=ArticleList.getSelectionModel().getSelectedItem().getKey();
            R.getConfig().currentParagraphProperty().set(indexof);
        }
        else{
            R.getConfig().currentParagraphProperty().setValue(null);
        }
        */
        //文本改变时修改信息
        textword.addListener(((observable, oldValue, newValue) -> {

            ObservableList<Pair<Integer, IndexRange>> anslist = R.getConfig().getAnswers();
            ObservableList<Pair<Integer, IndexRange>> asklist = R.getConfig().getAsks();
            //获取文本每行的文字
            if(!textword.get().equals(null)) {
                paras = textword.get().split("\n");
            }
            else
                paras=null;
            /*
            for(int i=0;i<paras.length;i++)
                if(paras[i].equals(null))
                {
                    paras.
                }
                */
           //System.out.println("运行成功:  "+paras.length+"..."+anslist.size()+".."+asklist.size());
            //临时数据清零
            artlist.clear();
            /*
                for(int i=0;i<anslist.size();i++)
                {
                    System.out.println("ans位置:"+anslist.get(i).getKey());
                }
                for(int i=0;i<asklist.size();i++)
                {
                    System.out.println("ask位置:"+asklist.get(i).getKey());
                }
            */
            for (int i = 0, j = 0; i < anslist.size() || j < asklist.size(); ) {
                //System.out.println("循环进入");
                if(paras.equals(null))
                    break;
                lasttext = "";
                //System.out.println("ans\\ask范围:"+anslist.size()+"..."+asklist.size());
                /*
                if(anslist.size()!=0&&i < anslist.size())
                    System.out.println("ans范围:"+anslist.get(i).getKey());
                if(asklist.size()!=0&&j < asklist.size())
                    System.out.println("ask范围:"+asklist.get(j).getKey());
                    */
                //同时存在的先后顺序排列
                // i为受,j为访
                //for(int k=0;k<paras.length;k++)
                //    System.out.println(paras[i]+"..."+i);


                if (i < anslist.size() && j < asklist.size()) {
                    if (anslist.get(i).getKey() <= asklist.get(j).getKey()) {
                        /*
                            受
                         */
                        len = anslist.get(i).getValue();
                        lasttext+=paras[anslist.get(i).getKey()];
                        indexof=anslist.get(i).getKey();
                        i++;
                    } else {
                        /*
                            访
                         */
                        len = asklist.get(j).getValue();
                        lasttext=paras[asklist.get(j).getKey()];
                        indexof=asklist.get(j).getKey();
                        j++;
                    }
                } else if (i < anslist.size()) {
                    /*
                        受
                     */
                    len = anslist.get(i).getValue();
                    lasttext+=paras[anslist.get(i).getKey()];
                    indexof=anslist.get(i).getKey();
                    i++;
                } else {
                    /*
                        访
                     */
                    len = asklist.get(j).getValue();
                    lasttext = paras[asklist.get(j).getKey()];
                    indexof = asklist.get(j).getKey();
                    j++;
                }
                /*
                    为临时表添加内容
                 */
                //System.out.println("wenben:"+lasttext);
                //length = lasttext.length();
                /*长度判断
                if (length <= 30) {
                    lineword = lasttext.substring(0);
                } else {
                    lineword = lasttext.substring(0, 29);
                }*/
                Pair<Integer,String> pair=new Pair<Integer,String>(indexof,lasttext);
                artlist.add(pair);
                //System.out.println("linshi:"+artlist.get(0));
            }

            /*
                listview内容添加
             */
            ArticleList.setItems(artlist);
        }));

    };



    /*
        结果预览
     */
    private void result(){

    };
}
