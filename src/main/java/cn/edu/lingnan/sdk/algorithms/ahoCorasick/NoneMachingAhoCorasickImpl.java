package cn.edu.lingnan.sdk.algorithms.ahoCorasick;

/**
 * Created on 2018/9/12
 * @author 李田锋
 * 情绪词特殊方法处理类
 * 每当新添加一个新词，总会新增一个在前面加不的词汇，是的否定词能够被识别出来。
 *
 */
public class NoneMachingAhoCorasickImpl extends AhoCorasickImpl{

    @Override
    public void append(String word) {
        super.append(word);
        super.append("不" + word);
    }


}
