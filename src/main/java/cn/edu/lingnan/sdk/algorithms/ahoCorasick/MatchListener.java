package cn.edu.lingnan.sdk.algorithms.ahoCorasick;

/**
 * Created by Administrator on 2018/1/30.
 * 针对于ac自动机算法匹配到新词时
 * 进行单词回调,以即时显示匹配的单词
 *
 */
public interface MatchListener {
    /**
     * 匹配时回调的函数
     * @param word 匹配的单词
     * @param start 该单词开始的位置
     * @param end 该单词结束的位置
     */
    public abstract void match(String word, Integer start, Integer end);
}
