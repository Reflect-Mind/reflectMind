package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.algorithms.ahoCorasick.AhoCorasick;
import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.sdk.overlay.CustomLineNumberFactory;
import cn.edu.lingnan.command.TextWorkspaceCommand;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.java.goxr3plus.javastreamplayer.stream.StreamPlayer;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.StyleSpans;

import java.net.URL;
import java.time.Duration;
import java.util.*;


/**
 * Created by Administrator on 2018/2/17.
 */
public class TextWorkspaceController extends Controller {

    @FXML
    private HiddenSidesPane hiddenSidesPane;
    @FXML
    private StyleClassedTextArea textArea;
    @FXML
    private TextWorkspaceRightController textWorkspaceManipulateController;
    @FXML
    private TabPane tabPane;

    /**
     * 选择单词后的右键菜单
     */
    @FXML
    private ContextMenu codeAreaSelectionMenu;


    private String currentReplaceText = "";

    //1：代表访, 2：代表受
    private int curCharacterIndex = 0;

    private SpreadsheetView spreadsheetView = null;

    //流媒体播放类
    private StreamPlayer streamPlayer = null;

    // config实体类
    private Config config = R.getConfig();

    //未登陆词属性
    private ObservableList<String> unregisteredWords = this.config.getUnregisteredWords();

    //匹配心理词汇的自动机
    private AhoCorasick ahoCorasick = this.config.getAhoCorasick();

    //搜索指定的关键词列表
    private ObservableList<String> searchTextList = this.config.getSearchTextList();

    public TextWorkspaceController(){
        this.makeStylesheet();

    }

    //高亮命令
    TextWorkspaceCommand highlightCommand = new TextWorkspaceCommand();

    @Override
    public void recoverLastState() {
        String content = this.config.getTextProperty();
        if (content != null) {
            this.textArea.insertText(0, content);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //设置段落号
        this.textArea.setParagraphGraphicFactory(new CustomLineNumberFactory(this.textArea));
        this.highlightCommand.setCodeArea(this.textArea);
        this.setTextAreaKeyTypedEvent();
        this.initHiddenSidesPane();
        this.richChanged();
        this.othersActionListener();
        this.highlightCommand.updateAhoMatchingData();
        //划词添加到ac自动机和当前维护的词汇列表当中
        this.listeningToSelectionText();
        this.initPlayer();
        super.initialize(location, resources);
    }

    /**
     * 处理添加到未登陆词属性
     */
    @FXML
    private void handleMakeUpUnregisteredWord(){
        String selectedText = this.textArea.getSelectedText();
        this.unregisteredWords.add(selectedText);
        this.ahoCorasick.remove(selectedText);
        this.flashCodeAreaStyle();
    }


    //刷新样式
    private void flashCodeAreaStyle(){
        Task<StyleSpans<Collection<String>>> task = this.highlightCommand.getStyleSpansTask(
                this.textArea.textProperty().getValue(), false);
        task.setOnSucceeded(event -> this.textArea.setStyleSpans(0, task.getValue()));
    }
    /**
     * 监听划词策略--调出弹窗，让用户自行选择对应词汇所添加到
     * 所属的分类当中
     */
    private void listeningToSelectionText(){
        //鼠标释放时触发该事件.
        this.textArea.setOnMouseReleased(event -> {
            String selectionText = this.textArea.getSelectedText();
            //过滤掉一些非法的操作
            if (!this.highlightCommand.validateSelectionText(selectionText))
                return;
            if (event.getClickCount() >= 2)
                return;
            this.codeAreaSelectionMenu.show(this.textArea
                    , event.getScreenX(), event.getScreenY());
        });
        //取消划词菜单的出现
        this.textArea.selectionProperty().addListener((observable, oldValue, newValue) -> {
            this.codeAreaSelectionMenu.hide();
        });
    }
    /**
     * 其他的运行监听器
     */
    private void othersActionListener(){
        //运行通知
//        BooleanProperty run = (BooleanProperty) R.getParameters("run");
//        run.addListener(((observable, oldValue, newValue) -> {
//            if (newValue) {
//                this.highlightCommand.updateAhoMatchingData();
//            }
//        }));
        //当前段落样式
        this.textArea.currentParagraphProperty().addListener(((observable, oldValue, newValue) -> {
            Platform.runLater(() ->{
                this.textArea.setParagraphStyle(newValue, Collections.singleton("current-paragraph"));
                if (this.textArea.getParagraphs().size() > oldValue)
                    this.textArea.clearParagraphStyle(oldValue);
            });

        }));

        //textProperty被修改时textArea将接受到通知并及时更新文本域中的字符串
        StringProperty textProperty = R.getConfig().textPropertyProperty();
        this.textArea.textProperty().addListener(((observable, oldValue, newValue) -> {
            textProperty.setValue(newValue);
        }));
        textProperty.addListener(((observable, oldValue, newValue) -> {
            if (newValue.equals(this.textArea.getText()))
                return;
            this.textArea.replaceText(newValue);
        }));

        //自动更新文本域跳转值:有方法重复调用的嫌疑
        boolean blockNotify = false;
        IntegerProperty currentParagraph = R.getConfig().currentParagraphProperty();
        IntegerProperty currentColumn = R.getConfig().currentColumnProperty();
        currentParagraph.addListener(((observable, oldValue, newValue) -> {
            if (currentParagraph.get() == this.textArea.currentParagraphProperty().getValue())
                return;
            //this.textArea.showParagraphAtTop(newValue.intValue());
            this.textArea.showParagraphInViewport(newValue.intValue());
            this.textArea.moveTo(newValue.intValue(), 0);
        }));
        currentColumn.addListener(((observable, oldValue, newValue) -> {
            if (currentColumn.get() == newValue.intValue())
                return;
            this.textArea.moveTo(this.textArea.currentParagraphProperty().getValue()
                    , newValue.intValue());
        }));
        this.textArea.currentParagraphProperty().addListener(((observable, oldValue, newValue) -> {
            currentParagraph.set(newValue);
        }));
        this.textArea.caretColumnProperty().addListener(((observable, oldValue, newValue) -> {
            currentColumn.set(newValue);
        }));

        //窗格的定位
        IntegerProperty currentTabIndex = R.getConfig().currentTabIndexProperty();
        SingleSelectionModel<Tab> singleSelectionModel = this.tabPane.getSelectionModel();
        currentTabIndex.addListener(((observable, oldValue, newValue) -> {
            if (singleSelectionModel.getSelectedIndex() == newValue.intValue())
                return;
            singleSelectionModel.select(newValue.intValue());
        }));
        singleSelectionModel.selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
            currentTabIndex.set(newValue.intValue());
        }));
        //指定的搜索词汇监听事件
        this.searchTextList.addListener((ListChangeListener<? super String>) ch -> {
            while (ch.next()){
                if (ch.wasAdded())
                    this.highlightCommand.updateSearchWord(true, ch.getAddedSubList());
                else if (ch.wasRemoved())
                    this.highlightCommand.updateSearchWord(false, ch.getRemoved());
            }
            this.flashCodeAreaStyle();

        });

        //锁定当前聚焦关键词监听事件
        config.searchingWordIndexProperty().addListener((observable, oldValue, newValue) -> {

            Task<StyleSpans<Collection<String>>> task = this.highlightCommand.getStyleSpansTask(
                    this.textArea.textProperty().getValue(), false);
            task.setOnSucceeded(event -> this.textArea.setStyleSpans(0, task.getValue()));
        });
    }

    private void initPlayer(){
//        File audio = new File("D:\\CloudMusic\\ee.mp3");
        //this.audioPlayCommand.setAudio(audio);
    }

    //创建工作表
    private void makeStylesheet(){
        GridBase gridBase = new GridBase(1100, 1);
        ObservableList<ObservableList<SpreadsheetCell>> rows = FXCollections.observableArrayList();
        for (int row = 0; row < gridBase.getRowCount(); row++){
            ObservableList<SpreadsheetCell> currentRow = FXCollections.observableArrayList();
            for (int column = 0; column < gridBase.getColumnCount(); column++)
                currentRow.add(SpreadsheetCellType.STRING.createCell(row, column, 1, 1, ""));
            rows.add(currentRow);
        }
        gridBase.setRows(rows);
        gridBase.setDisplaySelection(false);
        this.spreadsheetView = new SpreadsheetView(gridBase);
        this.spreadsheetView.getColumns().get(0).setPrefWidth(200);
        gridBase.getColumnHeaders().add("备注");
        this.spreadsheetView.setRowHeaderWidth(35.0);
    }

    /**
     * 文本域文本改变事件(大于额定长度将自动换行)
     * 手动回车将自动切换到下一个访问或者受状态
     */
    private void richChanged(){

        //高亮
        this.textArea.plainTextChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .successionEnds(Duration.ofMillis(10))
                .supplyTask(() -> this.highlightCommand.getStyleSpansTask(textArea.getText(), true))
                .awaitLatest(this.textArea.plainTextChanges())
                .filterMap(styleSpansTry -> {
                   if (styleSpansTry.isSuccess())
                       return Optional.of(styleSpansTry.get());
                   return Optional.empty();
                })
                .subscribe(styleSpans -> this.textArea.setStyleSpans(0, styleSpans));

        //智能换行
        this.textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && event.isShortcutDown()){
                String returnString = this.highlightCommand.getStageChangingTextForIndex(++curCharacterIndex);
                int caretIndex = this.textArea.getCaretPosition();
                this.textArea.replaceText(caretIndex , caretIndex, returnString);
            }
        });
        //每行30字限制
//        this.textArea.plainTextChanges()
//                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
//                .subscribe(change -> {
//                    String inserted = change.getInserted();
//                    if (!inserted.equals("") &&
//                            !this.currentReplaceText.equals(inserted)) {
//                        int length = this.textArea.getCaretColumn();
//                        if ((length == restrictLength + 1 && !TextWorkspaceCommand.NONE_LINE_CHARS.contains(inserted))
//                                || length > restrictLength + 1) {
//                            Robot robot = R.getRobot();
//                            int pos = change.getInsertionEnd() - 1;
//                            this.currentReplaceText = "\n\t";
//                            this.textArea.insertText(pos, this.currentReplaceText);
//                            robot.keyPress(java.awt.event.KeyEvent.VK_RIGHT);
//                            robot.keyPress(java.awt.event.KeyEvent.VK_DOWN);
//                            robot.keyPress(java.awt.event.KeyEvent.VK_RIGHT);
//                        }
//                    }
//                });
    }

    //初始化根据点击次数展开的窗格
    private void initHiddenSidesPane(){
        this.hiddenSidesPane.setRight(this.spreadsheetView);

        this.hiddenSidesPane.setTriggerDistance(0);
        this.hiddenSidesPane.setOnMouseClicked(event -> {
            //点击次数为2时展开窗格
            if (event.getClickCount() == 2) {
                double paneWidth = this.hiddenSidesPane.getWidth();
                double paneHeight = this.hiddenSidesPane.getHeight();
                double offsetX = event.getX() > paneWidth / 2 ? paneWidth - event.getX() : event.getX();
                double offsetY = event.getY() > paneHeight / 2 ? paneHeight - event.getY() : event.getY();
                //展开左右窗格:设置spreadsheetView到响应的位置
                Side side = this.hiddenSidesPane.getPinnedSide();
                if (offsetX < offsetY) {
                    if (event.getX() > paneWidth / 2) {
                        side = side == Side.RIGHT ? null : Side.RIGHT;
                        this.spreadsheetView
                                .scrollToRow(this.textArea.currentParagraphProperty().getValue() / 2);
                    }
                    else
                        side = side == Side.LEFT? null: Side.LEFT;
                }
                //展开上下窗格
                else {
                    if (event.getY() > paneHeight / 2)
                        side = side == Side.BOTTOM? null: Side.BOTTOM;
                    else
                        side = side == Side.TOP? null: Side.TOP;
                }
                this.hiddenSidesPane.setPinnedSide(side);

            }
        });
    }
    /**
     * 对于复制粘贴事件要重整行数字段限制(control + v),该过程事件要进行通知
     */
    private void setTextAreaKeyTypedEvent(){

        this.textArea.setOnKeyTyped((KeyEvent event) -> {
            if (event.isControlDown() && event.getCharacter().equals("\u0016")){
                Task<String> textReformedTask = this.highlightCommand.textReformed(this.textArea.getText(), 30);
                new Thread(textReformedTask).start();
                //设置当前任务到临时数据交换区中:用于进行通知
                SimpleObjectProperty<Task<String>> taskProperty = (SimpleObjectProperty<Task<String>>)
                        R.getParameters("currentTask");
                taskProperty.set(textReformedTask);
                //禁用文本区
                this.textArea.setDisable(true);
                //启用文本区,更换一个格式化的文本
                textReformedTask.setOnSucceeded(e ->{
                    this.textArea.replaceText(textReformedTask.getValue());
                    this.textArea.setDisable(false);
//                    BooleanProperty run = (BooleanProperty) R.getParameters("run");
//                    run.set(true);
                });
                textReformedTask.setOnFailed(e -> {
                    System.out.println(textReformedTask.getException());
                });
            }
        });
    }
}
