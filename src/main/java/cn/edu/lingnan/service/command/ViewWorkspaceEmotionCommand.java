package cn.edu.lingnan.service.command;

import cn.edu.lingnan.pojo.CatchingWord;
import cn.edu.lingnan.pojo.FrqTree;
import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.sdk.Container.PhaseContainer;
import cn.edu.lingnan.sdk.Container.PhaseType;
import cn.edu.lingnan.sdk.enumeration.LineType;
import cn.edu.lingnan.service.CategoryService;
import cn.edu.lingnan.service.VocabService;
import cn.edu.lingnan.service.impl.CategoryServiceImpl;
import cn.edu.lingnan.service.impl.VocabServiceImpl;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import com.kennycason.kumo.WordFrequency;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.edu.lingnan.sdk.enumeration.LineType.NEGTIVE;
import static cn.edu.lingnan.sdk.enumeration.LineType.POSITIVE;

/**
 * Created by Administrator on 2018/3/30.
 * @author feng
 *
 */
public class ViewWorkspaceEmotionCommand extends AbstractCommand<Void> {

    private ExecutorService service = Executors.newSingleThreadExecutor();

    private Config config = R.getConfig();

    //人生阶段容器
    private PhaseContainer<Pair<Integer, IndexRange>> phaseContainer
            = this.config.getPhaseContainer();

    //信息词汇列表
    private ObservableList<Vocab> vocabList = this.config.getVocabList();

    //捕获到的单词列表
    private List<CatchingWord> catchingWordList = this.config.getCatchingWords();

    private VocabService vocabService = new VocabServiceImpl();


    //童年阶段的词汇
    private List<FrqTree> childhoodList = new ArrayList<>();
    //中学阶段的词汇
    private List<FrqTree> middleList = new ArrayList<>();
    //大学阶段的词汇
    private List<FrqTree> collegeList = new ArrayList<>();
    //工作阶段的词汇
    private List<FrqTree> workList = new ArrayList<>();




    /**
     * 获取图表的填充数据的任务
     * @return
     */
    public Task<Void> buildPhaseListTask(){
        Task<Void> task =
                new Task<Void>() {
            protected Void call() throws Exception {
                Void v = null;
                try {
                    v = buildPhaseList();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return v;
            }
        };
        this.service.execute(task);
        return task;
    }

    /**
     * 构建人生四大阶段的单词频数列表
     */
    private Void buildPhaseList(){
        //整理处于人生四大阶段的词语
        List<String> list = new ArrayList<>();
        List<String > list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        for (CatchingWord word: catchingWordList){
            switch (this.phaseContainer.valueOf(word.getRange())){
                case CHILDHOOD:
                    list.add(word.getWord());
                    break;
                case MIDDLE:
                    list1.add(word.getWord());
                    break;
                case COLLEGE:
                    list2.add(word.getWord());
                    break;
                case WORK:
                    list3.add(word.getWord());
                    break;
            }
        }
        this.childhoodList = this.vocabService.getFrqTreeByContent(list);
        this.middleList = this.vocabService.getFrqTreeByContent(list1);
        this.middleList = this.vocabService.getFrqTreeByContent(list2);
        this.workList = this.vocabService.getFrqTreeByContent(list3);
        return null;
    }

    /**
     * 统计在单词列表中出现的次数
     * @param list 单词集合
     * @param type 单词类
     * @return
     */
    private int getTheSameTypeNum(List<FrqTree> list, LineType type){
        int sum = 0;
        for (FrqTree frqTree: list){
            if (type == POSITIVE || type == NEGTIVE){
                if (frqTree.getTheme().equals(type.toString()))
                    sum += frqTree.getAppearnum();
            }
            else if (frqTree.getCategory().equals(type.toString()))
                sum += frqTree.getAppearnum();
        }
        return sum;
    }
    /**
     * 获取图表的填充数据
     * @return
     */
    public XYChart.Series<String, Number> getChartData(LineType type){
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        ObservableList<XYChart.Data<String, Number>> dataList = series.getData();
        //统计各阶段的心理词汇数目
        int childhood = this.getTheSameTypeNum(this.childhoodList, type);
        int middle = this.getTheSameTypeNum(this.middleList, type);
        int college = this.getTheSameTypeNum(this.collegeList, type);
        int work = this.getTheSameTypeNum(this.workList, type);
        //为图表准备数据
        XYChart.Data<String, Number> childhoodData = new XYChart.Data<>(PhaseType.CHILDHOOD.toString(), childhood);
        XYChart.Data<String, Number> middleData = new XYChart.Data<>(PhaseType.MIDDLE.toString(), middle);
        XYChart.Data<String, Number> collegeData = new XYChart.Data<>(PhaseType.COLLEGE.toString(), college);
        XYChart.Data<String, Number> workData = new XYChart.Data<>(PhaseType.WORK.toString(), work);
        //为各大图表data设置人性化提示
//        Tooltip.install(childhoodData.getNode(), new Tooltip(type.toString() + ": "  + childhood));
//        Tooltip.install(middleData.getNode(), new Tooltip(type.toString() + ": " + middle));
//        Tooltip.install(collegeData.getNode(), new Tooltip(type.toString() + ": " + college));
//        Tooltip.install(workData.getNode(), new Tooltip(type.toString() + ": " + work));
        //图表数据的添加显示显示
        dataList.add(childhoodData);
        dataList.add(middleData);
        dataList.add(collegeData);
        dataList.add(workData);
        series.setName(type.toString());
        return series;
    }
}
