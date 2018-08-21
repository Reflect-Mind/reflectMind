package cn.edu.lingnan.sdk.enumeration;

/**
 * Created by Administrator on 2018/6/6.
 */
public enum WordType {

    PLAIN_WORD("所有心理词汇"),
    MOTION_WORD("情感词汇"),
    CHARACTER_WORD("人格词汇");


    WordType(String element){
        this.element = element;
    }
    private String element;

    @Override
    public String toString() {
        return this.element;
    }
}
