package cn.edu.lingnan.sdk.algorithms.classifier;

/**
 * Created by Administrator on 2018/3/11.
 * @author feng
 * 贝叶斯模型分类器
 */
public class ClassifierImpl implements Classifier{

    private BayesModel model;

    public ClassifierImpl(BayesModel model){
        this.model = model;
    }

    /**
     * 预测该段文件的所属
     * @param text
     * @return
     */
    public String predict(String text) {
        return this.model.predict(text);
    }
}
