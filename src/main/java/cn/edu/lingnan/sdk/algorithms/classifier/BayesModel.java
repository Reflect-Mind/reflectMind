package cn.edu.lingnan.sdk.algorithms.classifier;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/3/11.
 * @author feng
 *
 */
public abstract class BayesModel implements Serializable {

    /**
     * 分类名字属性
     *
     */
    protected List<String> categories = new ArrayList<>();
    /**
     * 分类概率属性
     */
    protected List<Double> categoryProbability = new ArrayList<>();

    /**
     * 词汇列表
     * 复写contains方法
     */
    protected Set<String> words = new TreeSet<>();
    /**
     * 从文件系统中读取而来的句子列表
     */
    protected transient List<String> sentences = new ArrayList<>();
    /**
     * 与语句相对应的句子所属的分类
     */
    protected transient List<Integer> categoryIdList = new ArrayList<>();
    /**
     * 词向量数组
     */
    protected double[][] wordVectors = null;
    /**
     * 粗糙分词器
     */
    protected  transient Tokenizer tokenizer = new UglyTokenizer();
    /**
     * 根据将要被预测的字符串在分类中占有的比率
     */
    protected transient double[] values = null;

    protected final static String ESCAPE = "[ 0-9A-z,，。.（）(){}\\[\\]“”‘’'、?？:：！!~|《》<>\"一二三四五六七八九十]";
    protected final static Pattern ESCAPE_PATTERN = Pattern.compile(ESCAPE);


    /**
     * 给该模型设置分词器
     */
    public void setTokenizer(Tokenizer tokenizer){
        this.tokenizer = tokenizer;
    }

    public List<String> getCategories() {
        return categories;
    }

    public double[] getValues() {
        return values;
    }

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
