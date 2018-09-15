package cn.edu.lingnan.utils;

import cn.edu.lingnan.pojo.CatchingWord;
import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.sdk.Container.PhaseContainer;
import cn.edu.lingnan.sdk.Container.PhaseContainerImpl;
import cn.edu.lingnan.sdk.plain.IndexRange;
import cn.edu.lingnan.sdk.algorithms.ahoCorasick.AhoCorasick;
import cn.edu.lingnan.sdk.algorithms.ahoCorasick.NoneMachingAhoCorasickImpl;
import cn.edu.lingnan.sdk.enumeration.ChartType;
import cn.edu.lingnan.sdk.enumeration.WordType;
import cn.edu.lingnan.sdk.overlay.AudioPlayer;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/1/31.
 * 本地工程配置文件
 * 负责桌面环境的一些选择配置
 * JAXB
 * @author 李田锋
 */
public class Config implements Externalizable {

    // AC自动机
    private AhoCorasick ahoCorasick = new NoneMachingAhoCorasickImpl();
    // 文本区域字数限制
    private int restrictLength = 30;

    //音频文件类
    private File audio = null;

    /**
     * 流媒体播放类
     */
    private AudioPlayer audioPlayer = null;
    public  AudioPlayer getAudioPlayer() {
        synchronized (this) {
            if (this.audioPlayer == null) {
                this.audioPlayer = new AudioPlayer();
            }
        }
        return audioPlayer;
    }

    /**
     * 获取ac自动机实例
     */
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

    /**
     * 返回每次匹配到的单词
     * @return
     */
    @Deprecated
    public List<String> getWords() {
        List<String> words = new ArrayList<>();
        this.catchingWords.forEach(catchingWord ->
                words.add(catchingWord.getWord()));
        return words;
    }

    /**
     * 捕获到单词对象集合
     */
    private List<CatchingWord> catchingWords = new ArrayList<>();
    public List<CatchingWord> getCatchingWords() {
        return catchingWords;
    }

    /**
     * 受
     */
    private ObservableList<Pair<Integer, IndexRange>> answers = FXCollections.observableArrayList();
    public ObservableList<Pair<Integer, IndexRange>> getAnswers() { return answers; }

    /**
     * 访：
     */
    private ObservableList<Pair<Integer, IndexRange>> asks = FXCollections.observableArrayList();
    public ObservableList<Pair<Integer, IndexRange>> getAsks() {
        return asks;
    }

    /**
     * 用于基调分析的人生四大阶段: 小学、初中、大学、工作。
     */
    PhaseContainer<Pair<Integer, IndexRange>> phaseContainer = new PhaseContainerImpl();
    public PhaseContainer<Pair<Integer, IndexRange>> getPhaseContainer() {
        return phaseContainer;
    }

    /**
     * 文本字符串
     */
    private StringProperty textProperty = new SimpleStringProperty();
    public String getTextProperty() {
        return textProperty.get();
    }
    public StringProperty textPropertyProperty() { return textProperty; }

    /**
     * 当前列号属性
     */
    private IntegerProperty currentColumn = new SimpleIntegerProperty();
    public IntegerProperty currentColumnProperty() {
        return currentColumn;
    }

    /**
     * 当前段落属性
     */
    private IntegerProperty currentParagraph = new SimpleIntegerProperty();
    public IntegerProperty currentParagraphProperty() { return currentParagraph; }

    /**
     * 是否标记信息词汇属性
     */
    private BooleanProperty markVocabs = new SimpleBooleanProperty(true);
    public BooleanProperty markVocabsProperty() { return markVocabs; }

    /**
     * 设置当前窗格属性
     */
    private IntegerProperty currentTabIndex = new SimpleIntegerProperty();
    public IntegerProperty currentTabIndexProperty() {
        return currentTabIndex;
    }

    /**
     * 数据库当中查询到的心理词汇
     */
    private ObservableList<Vocab> vocabList = FXCollections.observableArrayList();
    public ObservableList<Vocab> getVocabList() {
        return vocabList;
    }

    /**
     * 加入搜索行列的搜索关键词
     */
    private StringProperty searchText = new SimpleStringProperty();
    @Deprecated
    public StringProperty searchTextProperty() { return searchText; }

    /**
     * 加入搜索行列的搜索关键词列表
     */
    private ObservableList<String> searchTextList = FXCollections.observableArrayList();
    public ObservableList<String> getSearchTextList() {
        return searchTextList;
    }

    /**
     * 需要被定位的被搜索的词汇索引
     */
    private IntegerProperty searchingWordIndexProperty = new SimpleIntegerProperty(0);
    public IntegerProperty searchingWordIndexProperty() { return searchingWordIndexProperty; }

    /**
     * 搜索到的单词的个数属性
     */
    private IntegerProperty searchingWordCountProperty = new SimpleIntegerProperty();
    public IntegerProperty searchingWordCountProperty() {
        return searchingWordCountProperty;
    }

    /**
     * 基调分析图表切换图表属性:初始化属性值为LINE_CHART
     */
    private ObjectProperty<ChartType> emotionChartProperty = new SimpleObjectProperty<>(ChartType.LINE_CHART);
    public ObjectProperty<ChartType> emotionChartProperty() {
        return emotionChartProperty;
    }

    //人格分析图表切换图表属性：初始值为


    /**
     * 未登录词属性
     */
    private ObservableList<String> unregisteredWords = FXCollections.observableArrayList();
    public ObservableList<String> getUnregisteredWords() {
        return unregisteredWords;
    }

    /**
     * 概览图显示词汇类型
     */
    private SimpleObjectProperty<WordType> wordTypeProperty = new SimpleObjectProperty<>(WordType.PLAIN_WORD);
    public SimpleObjectProperty<WordType> wordTypeProperty() {
        return wordTypeProperty;
    }


    /**
     * 自定义实例化输出
     * 当定义了一个在config定义了一个需要持久化的对象时
     * 需要在该方法中编写语句
     * 需要注意的是，输出和输入顺序需要相同
     * @param out
     * @throws IOException
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        Pair<Integer, IndexRange> pair[] = new Pair[]{};
        out.writeObject(this.phaseContainer);
        out.writeObject(this.ahoCorasick);
        out.writeObject(this.answers.toArray(pair));
        out.writeObject(this.asks.toArray(pair));
        out.writeObject(this.audio);
        out.writeObject(this.catchingWords.toArray(new CatchingWord[]{}));
        out.writeObject(this.currentColumn.get());
        out.writeObject(this.currentParagraph.get());
        out.writeObject(this.currentTabIndex.get());
        out.writeObject(this.vocabList.toArray(new Vocab[]{}));
        out.writeObject(this.emotionChartProperty.get());
        out.writeObject(this.unregisteredWords.toArray(new String[]{}));
        out.writeObject(this.markVocabs.get());
        out.writeObject(this.textProperty.get());


    }

    /**
     * 自定义是实例化读取
     * 当定义了一个在config定义了一个需要持久化的对象时
     * 需要在该方法中编写语句
     * 需要注意的是，输出和输入顺序需要相同
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.phaseContainer = (PhaseContainer<Pair<Integer, IndexRange>>) in.readObject();
        this.ahoCorasick = (AhoCorasick) in.readObject();
        this.asks = FXCollections.observableArrayList(Arrays.asList((Pair<Integer, IndexRange>[])in.readObject()));
        this.answers = FXCollections.observableArrayList(Arrays.asList((Pair<Integer, IndexRange>[])in.readObject()));
        this.audio = (File) in.readObject();
        this.catchingWords = FXCollections.observableArrayList(Arrays.asList((CatchingWord[])in.readObject()));
        this.currentColumn = new SimpleIntegerProperty((Integer) in.readObject());
        this.currentParagraph = new SimpleIntegerProperty((Integer) in.readObject());
        this.currentTabIndex = new SimpleIntegerProperty((Integer) in.readObject());
        this.vocabList = FXCollections.observableArrayList(Arrays.asList((Vocab[])in.readObject()));
        this.emotionChartProperty = new SimpleObjectProperty<>((ChartType) in.readObject());
        this.unregisteredWords = FXCollections.observableArrayList(Arrays.asList((String[])in.readObject()));
        this.markVocabs = new SimpleBooleanProperty((Boolean) in.readObject());
        this.textProperty = new SimpleStringProperty((String) in.readObject());
    }
}
