package cn.edu.lingnan.utils;

import cn.edu.lingnan.pojo.CatchingWord;
import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.sdk.Container.PhaseContainer;
import cn.edu.lingnan.sdk.Container.PhaseContainerImpl;
import cn.edu.lingnan.sdk.algorithms.ahoCorasick.AhoCorasick;
import cn.edu.lingnan.sdk.algorithms.ahoCorasick.AhoCorasickImpl;
import cn.edu.lingnan.sdk.enumeration.ChartType;
import cn.edu.lingnan.sdk.overlay.AudioPlayer;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.IndexRange;
import javafx.util.Pair;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 * 本地工程配置文件
 * 负责桌面环境的一些选择配置
 *
 */
public class Config implements Serializable{

    private static Config config = null;
    private AhoCorasick ahoCorasick = new AhoCorasickImpl();
    //文本区域字数限制
    private int restrictLength = 30;

    //音频文件类
    private File audio = null;
    //流媒体播放类
    private AudioPlayer audioPlayer = new AudioPlayer();
    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    //获取ac自动机实例
    public AhoCorasick getAhoCorasick() {
        return ahoCorasick;
    }

    public int getRestrictLength() {
        return restrictLength;
    }
    public void setRestrictLength(int restrictLength) {
        this.restrictLength = restrictLength;
    }

    public void setAudio(File audio) {
        this.audio = audio;
    }

    //每次匹配到的单词
    @Deprecated
    public List<String> getWords() {
        List<String> words = new ArrayList<>();
        this.catchingWords.forEach(catchingWord ->
                words.add(catchingWord.getWord()));
        return words;
    }
    private List<CatchingWord> catchingWords = new ArrayList<>();
    public List<CatchingWord> getCatchingWords() {
        return catchingWords;
    }

    // 受：
    private ObservableList<Pair<Integer, IndexRange>> answers = FXCollections.observableArrayList();
    public ObservableList<Pair<Integer, IndexRange>> getAnswers() {
        return answers;
    }
    // 访：
    private ObservableList<Pair<Integer, IndexRange>> asks = FXCollections.observableArrayList();
    public ObservableList<Pair<Integer, IndexRange>> getAsks() {
        return asks;
    }
    //用于基调分析的人生四大阶段: 小学、初中、大学、工作。
    PhaseContainer<Pair<Integer, IndexRange>> phaseContainer = new PhaseContainerImpl();
    public PhaseContainer<Pair<Integer, IndexRange>> getPhaseContainer() {
        return phaseContainer;
    }

    //文本字符串
    private StringProperty textProperty = new SimpleStringProperty();
    public String getTextProperty() {
        return textProperty.get();
    }
    public StringProperty textPropertyProperty() {
        return textProperty;
    }

    //当前列号属性
    private IntegerProperty currentColumn = new SimpleIntegerProperty();
    public IntegerProperty currentColumnProperty() {
        return currentColumn;
    }

    //当前段落属性
    private IntegerProperty currentParagraph = new SimpleIntegerProperty();
    public IntegerProperty currentParagraphProperty() {
        return currentParagraph;
    }

    //是否标记信息词汇属性
    private BooleanProperty markVocabs = new SimpleBooleanProperty(true);
    public BooleanProperty markVocabsProperty() { return markVocabs; }

    //设置当前窗格属性
    private IntegerProperty currentTabIndex = new SimpleIntegerProperty();
    public IntegerProperty currentTabIndexProperty() {
        return currentTabIndex;
    }

    //数据库当中查询到的心理词汇
    private ObservableList<Vocab> vocabList = FXCollections.observableArrayList();
    public ObservableList<Vocab> getVocabList() {
        return vocabList;
    }

    //加入搜索行列的搜索关键词
    private StringProperty searchText = new SimpleStringProperty();
    public StringProperty searchTextProperty() { return searchText; }

    //基调分析图表切换事件属性:初始化属性值为LINE_CHART
    private ObjectProperty<ChartType> emotionChartProperty = new SimpleObjectProperty<>(ChartType.LINE_CHART);
    public ObjectProperty<ChartType> emotionChartProperty() {
        return emotionChartProperty;
    }

    /**
     * 为桌面环境配置初始化
     * 获取当前项目名称
     * 一个config对象
     */
    private Config(){}
    static Config getInstance(){
        if (config == null){
            String localProjectName = (String) PreferencesUtils.getParametersAsString("localProject");
            String basePath = PreferencesUtils.getParametersAsString("basePath");
            String path = basePath + "/" + localProjectName;
            config = SerializableUtils.getLastState(Config.class, path);
            //当再次为空时表明此时尚未创建工程
            if (config == null)
                config = new Config();
        }
        return config;
    }
}
