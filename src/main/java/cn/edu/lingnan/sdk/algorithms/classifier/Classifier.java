package cn.edu.lingnan.sdk.algorithms.classifier;

/**
 * Created by Administrator on 2018/3/11.
 * @author feng
 */
public interface Classifier {
    /**
     * 根据输入的文本进行分类的预测并返回相应的
     * 分类情况
     * @param text
     * @return 所处分类字符串
     */
    String predict(String text);
}
