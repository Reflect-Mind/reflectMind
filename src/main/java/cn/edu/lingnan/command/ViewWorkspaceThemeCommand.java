package cn.edu.lingnan.command;

import cn.edu.lingnan.sdk.algorithms.classifier.BayesModel;
import cn.edu.lingnan.sdk.algorithms.classifier.BayesModelImpl;
import cn.edu.lingnan.sdk.algorithms.classifier.Classifier;
import cn.edu.lingnan.sdk.algorithms.classifier.ClassifierImpl;
import cn.edu.lingnan.utils.Config;
import cn.edu.lingnan.utils.R;
import cn.edu.lingnan.utils.SerializableUtils;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/4/11.
 */
public class ViewWorkspaceThemeCommand extends AbstractCommand<Void> {

    /**
     * 分类器
     */
    private Classifier classifier = null;
    /**
     * 分类模型
     */
    private BayesModel model = null;

    /**
     * 异步线程执行器
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Config config = R.getConfig();

    /**
     * 文本字符串
     */
    private StringProperty textProperty = this.config.textPropertyProperty();


    public ViewWorkspaceThemeCommand(){
        this.model = SerializableUtils.getLastState(BayesModelImpl.class);
        this.classifier = new ClassifierImpl(model);

    }

    /**
     * 获取图表数据填充任务
     * @return
     */
    public Task<ObservableList<XYChart.Series<String, Number>>> getSeriesTask(){
        Task<ObservableList<XYChart.Series<String, Number>>> task = new Task<ObservableList<XYChart.Series<String, Number>>>() {
            @Override
            protected ObservableList<XYChart.Series<String, Number>> call() throws Exception {
                return seriesList();
            }
        };
        this.executorService.execute(task);
        return task;
    }

    /**
     * 获取图表数据
     * 文本字符串按行切分
     * 用分类模型判断每一行的数据所属的类别
     * @return
     */
    private ObservableList<XYChart.Series<String, Number>> seriesList(){
        ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList();
        String[] paras = this.textProperty.get().split("\n");
        Vector<Pair<Integer, double[]>> pairs = new Vector<>();
        for (int i = 0; i < paras.length; i++) {
            String target = paras[i].trim();
            if (target.equals(""))
                continue;
            if (!(target.startsWith("受:") || target.startsWith("受：")))
                continue;

            this.classifier.predict(target);
            pairs.add(new Pair<>(i, this.model.getValues()));
        }
        //填充图表数据:数目确定于vector
        List<String> categories = this.model.getCategories();
        int categoryNum = categories.size();
        for (int i = 0; i < categoryNum; i++) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            ObservableList<XYChart.Data<String, Number>> dataList = series.getData();
            for (int j = 0; j < pairs.size(); j++) {
                Pair<Integer, double[]> pair = pairs.get(j);
                XYChart.Data<String, Number> data = new XYChart.Data<>(
                        String.format("行号%d", pair.getKey()), -pair.getValue()[i]
                );
                dataList.add(data);
            }
            series.setName(categories.get(i));
            seriesList.add(series);
        }
        return seriesList;

    }
}
