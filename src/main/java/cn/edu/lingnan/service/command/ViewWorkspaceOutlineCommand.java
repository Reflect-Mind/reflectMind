package cn.edu.lingnan.service.command;

import cn.edu.lingnan.sdk.enumeration.WordType;
import cn.edu.lingnan.service.VocabService;
import cn.edu.lingnan.service.impl.VocabServiceImpl;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.IOUtils;
import cn.edu.lingnan.utils.R;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.FontWeight;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.ColorPalette;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/3/19.
 * @author feng
 */
public class ViewWorkspaceOutlineCommand {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Config config = R.getConfig();
    private SimpleObjectProperty<WordType> wordTypeProperty = this.config.wordTypeProperty();
    private VocabService vocabService = new VocabServiceImpl();
    /**
     * 产生词云图片文件任务类
     * @param text
     * @return
     */
    public Task<File> getImageFileTask(String text){
        Task<File> task =  new Task<File>() {
            @Override
            protected File call() throws Exception {
                return generateWordCloudImage(text);
            }
        };
        this.executorService.execute(task);
        return task;
    }
    /**
     * 产生词云图片方法
     * @param text
     * @return
     */
    private File generateWordCloudImage(String text) throws IOException {
        File file = IOUtils.getTempFile("png", this);
        try {
            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
            frequencyAnalyzer.setWordFrequenciesToReturn(600);
            frequencyAnalyzer.setMinWordLength(2);
            List<WordFrequency> frequencyList = frequencyAnalyzer.load(this.getWordsWithMatchingCharType());
            Dimension dimension = new Dimension(600, 600);
            WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
            wordCloud.setPadding(2);
            wordCloud.setBackground(new CircleBackground(300));
            wordCloud.setBackgroundColor(new Color(244, 244, 244));
            wordCloud.setColorPalette(new LinearGradientColorPalette(Color.RED, Color.BLUE, Color.GREEN, 30, 30));
            wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
            java.awt.Font font = new java.awt.Font("STSong-Light", 2, 20);
            wordCloud.setKumoFont(new KumoFont(font));
            wordCloud.build(frequencyList);
            wordCloud.writeToFile(file.toString());

        } catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }

    /**
     *
     * @return
     */
    private List<String> getWordsWithMatchingCharType(){
        List<String> list = null;
        switch (this.wordTypeProperty.get()){
            case PLAIN_WORD:
                list = R.getConfig().getWords();
                break;
            case MOTION_WORD:
                list = this.vocabService.getMotionWords(R.getConfig().getWords());
                break;
            case CHARACTER_WORD:
                list = this.vocabService.getCharacterWords(R.getConfig().getWords());
                break;
        }
        return list;
    }

}
