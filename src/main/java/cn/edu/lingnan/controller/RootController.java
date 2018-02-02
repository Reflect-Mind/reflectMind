package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.advice.AbstractObservable;
import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.utils.DialogUtils;
import cn.edu.lingnan.utils.PreferencesUtils;
import cn.edu.lingnan.utils.R;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RootController extends Controller {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Parent personOverView;
    @FXML
    private TextArea textArea;
    @FXML
    private ContextMenu textAreaMenu;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private HTMLEditor htmlEditor;
    public Object contController;
    private File defaultDirectory = null;
    public RootController(){
        String path = PreferencesUtils.getParameters("file");
        if (path != null)
            this.defaultDirectory = new File(path);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void handleOpen(){
        this.defaultDirectory = DialogUtils.openFileChooser(DialogUtils.FileChooserType.SINGLE_DIALOG,
                this.defaultDirectory.getParentFile(), DialogUtils.ExtensionFilter.DEFAULT).get(0);
        if (this.defaultDirectory != null)
        PreferencesUtils.setParameters("file", this.defaultDirectory.getParent());
        System.out.println(this.defaultDirectory.getParentFile());
    }
    @FXML
    private void handleNew(){

    }
    @FXML
    private void handleSave(){

    }
    @FXML
    private void handleSaveAs(){

    }
    @FXML
    private void handleExit(){
        Stage stage = R.getApplication().getStage();
        System.exit(0);
    }
    @FXML
    private void handleShowStatistic(){

    }
    //上下文菜单
    public void handleContextMenu(MouseEvent mouseEvent){
        if (mouseEvent.isControlDown() && mouseEvent.getButton() == MouseButton.SECONDARY){
            this.textArea.setContextMenu(textAreaMenu);
            this.textAreaMenu.show(R.getApplication().getStage(),
                    mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }
        else
            this.textAreaMenu.hide();
    }

    @FXML
    private void handleOnMouseEntered(){
        Tooltip tooltip = new Tooltip();
        tooltip.setText("fdfdfdf");
        tooltip.setGraphic(new ImageView(new Image(R.getResourceAsStream("images/chart_1.png"))));
        //tooltip.show(R.getApplication().getStage());
        this.comboBox.setItems(FXCollections.observableList(new ArrayList<String>(){
            {
                this.add("aaaaaa");
                this.add("bbbbbb");
            }
        }));
        this.comboBox.setEditable(true);
        this.comboBox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                ListCell<String> cell = new ListCell<String>(){
                    {
                        super.setPrefWidth(100);
                    }
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null){
                            this.setText(null);
                            this.setGraphic(null);
                        }
                        else{
                            this.setText(item);
                            if (item.contains("aaaaaa"))
                                this.setTextFill(Color.RED);
                            else
                                this.setTextFill(Color.BLACK);
                        }
                    }
                };
                return cell;
            }
        });
        this.comboBox.setCellFactory(ComboBoxListCell.forListView());
        this.comboBox.valueProperty().addListener(((observable, oldValue, newValue) -> {
            System.out.println("newValue:" + newValue);
        }));
        System.out.println(this.htmlEditor.getHtmlText());
    }

    @Override
    public <T> void update(AbstractObservable observable, T observer, Object... args) {
        System.out.println("rootController收到通知, 正在响应");
    }
}
