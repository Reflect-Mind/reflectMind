package cn.edu.lingnan.pojo;

import javafx.scene.control.IndexRange;

/**
 * Created by Administrator on 2018/4/4.
 * @author feng
 * 捕捉到的单词数据结构
 */
public class CatchingWord {
    /**
     * 单词
     */
    private String word;

    /**
     * 单词所在的大致索引范围
     */
    private IndexRange range;

    public CatchingWord(String word, IndexRange range) {
        this.word = word;
        this.range = range;
    }

    public String getWord() {
        return word;
    }

    public IndexRange getRange() {
        return range;
    }

    public boolean equals(CatchingWord obj) {
        return this.word.equals(obj.getWord())
                && this.range.equals(obj.getRange());
    }
}
