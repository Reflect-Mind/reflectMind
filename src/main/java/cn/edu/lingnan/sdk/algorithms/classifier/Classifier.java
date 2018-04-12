package cn.edu.lingnan.sdk.algorithms.classifier;

/**
 * Created by Administrator on 2018/3/11.
 * @author feng
 */
public abstract class Classifier {

    private BayesModel model;


    public Classifier(BayesModel model){
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

    public BayesModel getModel(){
        return this.model;
    }
}
