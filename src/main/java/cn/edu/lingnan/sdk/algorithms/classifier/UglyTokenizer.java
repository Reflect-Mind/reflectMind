package cn.edu.lingnan.sdk.algorithms.classifier;

/**
 * Created by Administrator on 2018/3/11.
 * @author feng
 * 简陋的分词器实现类
 */
public class UglyTokenizer implements Tokenizer{

    /**
     * 分割目标字符串
     */
    private String target;
    /**
     * 字符串的长度
     */
    private int length;
    /**
     * 当前索引值
     */
    private int currentIndex ;

    public void setTarget(String target) {
        this.target = target.trim();
        this.currentIndex = 1;
        this.length = this.target.length();
    }

    @Override
    public boolean hasNext() {
        if (this.currentIndex < this.length)
            return true;
        return false;
    }

    @Override
    public String next() {
        String string =  this.target
                .substring(currentIndex - 1, currentIndex + 1);
        this.currentIndex += 1;
        return string;
    }
}
