package cn.edu.lingnan.service.command;

import cn.edu.lingnan.pojo.CatchingWord;
import cn.edu.lingnan.pojo.FrqTree;
import cn.edu.lingnan.sdk.enumeration.LineType;
import cn.edu.lingnan.service.VocabService;
import cn.edu.lingnan.service.impl.VocabServiceImpl;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.PieChart;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/5/21.
 */
public class ViewWorkspaceCharacterCommand extends AbstractCommand<Void>{

    private Config config = R.getConfig();
    private List<CatchingWord> catchingWords = this.config.getCatchingWords();

    private VocabService vocabService = new VocabServiceImpl();

    //线程执行器
    private ExecutorService service = Executors.newSingleThreadExecutor();




    /**
     * 获取饼状图数据
     * @return
     */
    private ObservableList<PieChart.Data> getPieChartData(){
        //获取单词频数
        List<FrqTree> treeList = this.vocabService.getFrqTreeByContent(config.getWords());
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        int selfNum = 0;
        int thirdNum = 0;
        int thingNum = 0;
        final String selfString = LineType.SELF_FORWARD.toString();
        String thirdString = LineType.THIRD_FORWARD.toString();
        String thingString = LineType.THING_FORWARD.toString();
        for (FrqTree element: treeList){
            String category = element.getCategory();
            //个人指向
            if (category.equals(LineType.SELF_FORWARD.toString()))
                selfNum += 1;
            //事物指向
            else if (category.equals(LineType.THING_FORWARD.toString()))
                thingNum += 1;
            //他人指向
            else if (category.equals(LineType.THIRD_FORWARD.toString()))
                thirdNum += 1;
        }
        data.add(new PieChart.Data(selfString, selfNum));
        data.add(new PieChart.Data(thirdString, thirdNum));
        data.add(new PieChart.Data(thingString, thingNum));
        return data;
    }
    /**
     * 获取饼状图数据任务
     * @return
     */
    public Task<ObservableList<PieChart.Data>> getPieChartDataTask(){
        Task<ObservableList<PieChart.Data>> task = new Task<ObservableList<PieChart.Data>>() {
            @Override
            protected ObservableList<PieChart.Data> call() throws Exception {
                return getPieChartData();
            }
        };
        this.service.execute(task);
        return task;
    }


}
