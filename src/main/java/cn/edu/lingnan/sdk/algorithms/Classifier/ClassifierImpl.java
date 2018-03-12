package cn.edu.lingnan.sdk.algorithms.Classifier;

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

    @Override
    public String predict(String text) {
        return null;
    }
}
