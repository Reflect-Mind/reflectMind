package cn.edu.lingnan.sdk.overlay;

import main.java.goxr3plus.javastreamplayer.stream.StreamPlayer;
import main.java.goxr3plus.javastreamplayer.stream.StreamPlayerException;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by Administrator on 2018/3/1.
 */
public class AudioPlayerTest {


    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            File audio = new File("D:\\CloudMusic\\Adele - Rolling In the Deep.mp3");
            AudioPlayer audioPlayer = new AudioPlayer();
            audioPlayer.open(null);
            audioPlayer.play();
            audioPlayer.statusObjectProperty().addListener(((observable, oldValue, newValue) -> {
                System.out.println(newValue);
            }));
            audioPlayer.currentSecondProperty().addListener(((observable, oldValue, newValue) -> {
                System.out.println(newValue);
                if (newValue.longValue() == 10)
                    audioPlayer.seek(210);
            }));
        }).start();

    }
}
