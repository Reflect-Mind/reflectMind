package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.sdk.overlay.CustomLineNumberFactory;
import cn.edu.lingnan.service.command.TextWorkspaceCommand;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.java.goxr3plus.javastreamplayer.stream.StreamPlayer;
import main.java.goxr3plus.javastreamplayer.stream.StreamPlayerEvent;
import main.java.goxr3plus.javastreamplayer.stream.StreamPlayerException;
import main.java.goxr3plus.javastreamplayer.stream.StreamPlayerListener;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.*;
import org.fxmisc.richtext.model.StyleSpans;
import org.reactfx.EventStreams;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.Logger;


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

    private String currentReplaceText = "";

    //1：代表访, 2：代表受
    private int curCharacterIndex = 0;

    private SpreadsheetView spreadsheetView = null;

    //流媒体播放类
    private StreamPlayer streamPlayer = null;


    public TextWorkspaceController(){
        this.makeStylesheet();

    }

    //高亮命令
    TextWorkspaceCommand highlightCommand = new TextWorkspaceCommand();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //设置段落号
        this.textArea.setParagraphGraphicFactory(new CustomLineNumberFactory(this.textArea));
        this.setTextAreaKeyTypedEvent();
        this.initHiddenSidesPane();
        this.richChanged();
        this.othersActionListener();
        this.highlightCommand.updateAhoMatchingData();
        //this.initPlayer();
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

        //自动更新文本域跳转值
        IntegerProperty showParagraph = R.getConfig().showParagraphProperty();
        showParagraph.addListener(((observable, oldValue, newValue) -> {
            this.textArea.showParagraphAtTop(newValue.intValue());
            this.textArea.moveTo(newValue.intValue(), 0);
        }));

        IntegerProperty currentColumn = R.getConfig().currentColumnProperty();
        IntegerProperty currentParagraph = R.getConfig().currentParagraphProperty();
        //绑定当前行号
        currentParagraph.bind(this.textArea.currentParagraphProperty());
        //绑定当前列号
        currentColumn.bind(this.textArea.caretColumnProperty());
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
    }

    private void initPlayer(){
        File audio = new File("D:\\CloudMusic\\ee.mp3");
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
        Config config = R.getConfig();
        int restrictLength = config.getRestrictLength();
        //高亮
        this.textArea.plainTextChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .successionEnds(Duration.ofMillis(10))
                .supplyTask(() -> this.highlightCommand.getStyleSpansTask(textArea.getText()))
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
