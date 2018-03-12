package cn.edu.lingnan.sdk.algorithms.Classifier;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/11.
 * @author feng
 *
 */
public interface BayesModel extends Serializable {

    /**
     * 训练分类模型
     * @param directory
     */
    void train(File directory);

}
