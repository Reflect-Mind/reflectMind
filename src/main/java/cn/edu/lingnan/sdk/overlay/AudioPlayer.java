package cn.edu.lingnan.sdk.overlay;

import javafx.application.Platform;
import javafx.beans.property.*;
import main.java.goxr3plus.javastreamplayer.stream.*;
import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/27.
 * 音频播放类监听：shift+空格：播放或者暂停 shift+left：回退 shift+right：前进
 */
public class AudioPlayer extends StreamPlayer implements StreamPlayerListener{

    //是否已经播放
    private BooleanProperty playing = new SimpleBooleanProperty(false);
    public BooleanProperty playingProperty() {
        return playing;
    }

    //是否就绪
    private boolean isReadying = false;
    //总共时长
    private LongProperty totalSecond = new SimpleLongProperty();
    public LongProperty totalSecondProperty() {
        return totalSecond;
    }

    //比特率
    private Integer bps = null;
    //当前播放秒数
    private LongProperty currentSecond = new SimpleLongProperty();
    public LongProperty currentSecondProperty() {
        return currentSecond;
    }
    //skip的字节
    private int skipSecond = 0;

    private ObjectProperty<Status> statusObject = new SimpleObjectProperty<>();
    public ObjectProperty<Status> statusObjectProperty() {
        return statusObject;
    }

    //打开文件属性
    private BooleanProperty openProperty = new SimpleBooleanProperty(false);
    public BooleanProperty openProperty() {
        return openProperty;
    }

    public AudioPlayer(){
        this.addStreamPlayerListener(this);
        this.openProperty.set(false);
    }

    /**
     * 音频播放类开始播放音频
     * 支持的对象类型：File | URL | InputStream
     * @param audio
     */
    public boolean open(File audio) {
        if (!audio.exists())
            return false;
        try {
            if (this.isPausedOrPlaying())
                this.stop();
            super.open(audio);
            System.out.println(this.openProperty.get());
            this.openProperty.set(true);
            System.out.println(this.openProperty.get());
        } catch (StreamPlayerException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void play()  {
        try {
            if (!this.isPaused()) {
                super.play();
                this.skipSecond = 0;
            }
            else
                this.resume();
            this.playing.set(true);
        } catch (StreamPlayerException e) {
            e.printStackTrace();
        }
    }

    public boolean pause() {
        boolean isPause = super.pause();
        this.playing.set(!isPause);
        return isPause;
    }

    /**
     * 停止播放，释放音频资源
     */
    public void stop() {
        super.stop();
        this.playing.set(false);
    }

    @Override
    public long seek(long seconds)  {
        try {
            System.out.println("jump to : " + seconds * this.bps);
            long seek = super.seek(seconds * this.bps);
            this.skipSecond = (int) seconds + 1;

        } catch (StreamPlayerException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 快进
     * @param second 快进的秒数
     */
    public void forward(int second){
        long current = this.currentSecond.get();
        this.seek(current + second);
    }

    /**
     * 快退
     * @param second 快退的秒数
     */
    public void backward(int second){
        long current = this.currentSecond.get();
        this.seek(current - second);
    }

    /**
     * ctrl + space 暂停或播放放音频
     * 57
     * 57419(left)  backward
     * 57421(right) forward
     * 57416(up) increase volume
     * 57424(down) decrease volume
     */
    @Override
    public void opened(Object dataSource, Map<String, Object> properties) {
        if (properties.containsKey("mp3.bitrate.nominal.bps"))
            this.bps = ((Integer) properties.get("mp3.bitrate.nominal.bps") / 8);
        if (properties.containsKey("duration"))
            this.totalSecond.set((Long) properties.get("duration") / 1000000);
    }

    @Override
    public void progress(int nEncodedBytes, long microsecondPosition, byte[] pcmData, Map<String, Object> properties) {
        Platform.runLater(() ->{
            if (this.skipSecond == 0)
                this.currentSecond.set(microsecondPosition / 1000000 + 1);
            else
                this.currentSecond.set(microsecondPosition / 1000000 + this.skipSecond + 1);
        });

    }

    @Override
    public void statusUpdated(StreamPlayerEvent event) {
        this.statusObject.set(event.getPlayerStatus());
    }
}
