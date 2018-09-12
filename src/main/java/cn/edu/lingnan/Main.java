package cn.edu.lingnan;

import cn.edu.lingnan.sdk.service.HeavyService;
import cn.edu.lingnan.command.InitCommand;
import cn.edu.lingnan.command.ShutdownCommand;
import cn.edu.lingnan.utils.R;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * 程序入口
 * @author 李田锋
 *
 */
public class Main extends Application{

    private Stage stage = null;
    private Parent pane;

    /**
     * 设置本次应用的事件
     */
    private void setWindowEvent() {
        HeavyService<Integer> heavyService = HeavyService.getInstance();

        // 窗口关闭事件
        this.stage.setOnCloseRequest((event)->{
            event.consume();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "exit?");
            Optional<ButtonType> optional = alert.showAndWait();
            // 窗口关闭事件，提醒Controller进行装备的保存
            if (optional.get().getText().equals("确定")) {
                ShutdownCommand shutdownCommand = new ShutdownCommand();
                heavyService.setCommand(shutdownCommand);
                heavyService.executeImmediate();
                heavyService.setOnSucceeded(e -> {
                    if (heavyService.isEmpty())  System.exit(0);
                });
            }
        });

        // 出现加载窗口
        InitCommand command = new InitCommand();
        Platform.runLater(command);
        command.setOnSucceeded(event -> {
            this.stage.getIcons().add(new Image(R.getResourceAsStream("images/house.png")));
            FXMLLoader loader = new FXMLLoader(R.getResourcesAsURL("layouts/RootLayout.fxml"));
            try {
                this.pane = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(this.pane);
            this.stage.setScene(scene);
            this.stage.show();
        });
    }

    public void start(Stage primaryStage) throws Exception {

        // 为代理资源者设置资源者
        R.Owner owner = R.getOwner();
        owner.setApplication(this);
        this.stage = primaryStage;
        this.setWindowEvent();
    }

    public Stage getStage() {
        return stage;
    }


    public static void main(String[] args){

        Application.launch();
    }


}
