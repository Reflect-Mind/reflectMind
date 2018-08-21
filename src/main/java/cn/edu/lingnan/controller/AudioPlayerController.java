package cn.edu.lingnan.controller;

import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.sdk.overlay.AudioPlayer;
import cn.edu.lingnan.utils.R;
import com.jfoenix.controls.JFXSlider;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.value.ObservableValue;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.reactfx.EventStreams;
import org.reactfx.value.Val;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.function.Supplier;

/**
 * Created by Administrator on 2018/3/2.
 */
public class AudioPlayerController extends Controller {

    @FXML
    private Button rewind;
    @FXML
    private Button forward;
    @FXML
    private MaterialDesignIconView icon;
    @FXML
    private JFXSlider slider;
    @FXML
    private Label current;
    @FXML
    private Label total;

    private AudioPlayer audioPlayer = R.getConfig().getAudioPlayer();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        this.audioPlayer.open(new File("D:\\CloudMusic\\ee.mp3"));
        this.buttonMapping();
    }


    private void buttonMapping(){
        //映射播放暂停
        ObservableValue<String> string = Val.map(audioPlayer.playingProperty(),(isPlaying) ->{
            if (isPlaying)
                return "PAUSE_CIRCLE_OUTLINE";
            return "PLAY_CIRCLE_OUTLINE";
        });
        this.icon.glyphNameProperty().bind(string);
        this.current.textProperty().bind(Val.map(audioPlayer.currentSecondProperty()
                , (integer) -> String.valueOf(integer)));
        this.total.textProperty().bind(Val.map(audioPlayer.totalSecondProperty()
                , (integer) -> String.valueOf(integer)));
        //绑定进度条最大值
        this.slider.maxProperty().bind(audioPlayer.totalSecondProperty());
        //实时更新进度条的值
        this.audioPlayer.currentSecondProperty().addListener(((observable, oldValue, newValue) -> {
            this.slider.setValue(newValue.doubleValue());
        }));
        //使得进度条支持一定程度的拖拽，并能够在拖拽停止处开始播放音频
        EventStreams.changesOf(this.slider.valueProperty())
                .successionEnds(Duration.ofMillis(10))
                .filter(ch ->
                    this.audioPlayer.currentSecondProperty().get() != ch.getNewValue().longValue()
                    )
                .onRecurseRetainLatest()
                .subscribe((numberChange -> {
                    new Thread(() -> {
                        this.audioPlayer.seek(numberChange.getNewValue().longValue());
                    }).start();
                }));
    }
    @FXML
    /**
     * 播放按钮事件
     * 在没有设置播放资源时,则跳出文件选择对话框
     * 让用户选择将要播放的文件，否则则进行播放暂停的
     * 基本选项
     */
    private void setOnMouseClickedWithIcon(){
        if (this.audioPlayer.isPlaying())
            this.audioPlayer.pause();
        else
            this.audioPlayer.play();
    }

    /**
     * 后退
     */
    @FXML
    private void fastPrevious(){
        this.audioPlayer.backward(1);
    }

    /**
     * 快进
     */
    @FXML
    private void fastForward(){
        this.audioPlayer.forward(1);
    }
}
