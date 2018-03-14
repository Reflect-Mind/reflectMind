package cn.edu.lingnan.sdk.algorithms.classifier;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/11.
 * @author feng
 *
 */
public abstract class BayesModel implements Serializable {

    /**
     * 训练分类模型
     * @param directory
     */
    public abstract void train(File directory);

    /**
     * 根据模型进行单词的分类于预测
     * @param target
     */
    abstract String predict(String target);

}
