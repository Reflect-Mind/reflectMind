package cn.edu.lingnan;

import cn.edu.lingnan.sdk.service.HeavyService;
import cn.edu.lingnan.service.command.InitCommand;
import cn.edu.lingnan.utils.R;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class Main extends Application{

    private Stage stage = null;
    private BorderPane pane;

    public void start(Stage primaryStage) throws Exception {
        //为代理资源者设置资源者
        R.Owner owner = R.getOwner();
        owner.setApplication(this);
        this.stage = primaryStage;
        primaryStage.getIcons().add(new Image(R.getResourceAsStream("images/house.png")));
        FXMLLoader loader = new FXMLLoader(R.getResourcesAsURL("layouts/RootLayout.fxml"));
        try {
            this.pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(this.pane);
        this.stage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "exit?");
                Optional<ButtonType> optional = alert.showAndWait();
                //窗口关闭事件，提醒Controller进行装备的保存
                if (optional.get().getText().equals("确定")) {
                    Platform.exit();
                }

            }
        });

        //出现加载窗口
        InitCommand command = new InitCommand();
        HeavyService<Integer> heavyService = HeavyService.getInstance();
        heavyService.setCommand(command);
        heavyService.executeImmediate();
        //完成加载后打开初始界面
        heavyService.setOnSucceeded(event -> this.stage.show());
    }

    public Stage getStage() {
        return stage;
    }


    public static void main(String[] args){

        Application.launch();
    }


}
