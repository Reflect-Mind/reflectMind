package cn.edu.lingnan.sdk.algorithms.classifier;

/**
 * Created by Administrator on 2018/3/11.
 * @author feng
 * 分词器
 */
public interface Tokenizer {
    void setTarget(String target);
    boolean hasNext();
    String next();
}
