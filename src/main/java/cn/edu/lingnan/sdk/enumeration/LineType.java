package cn.edu.lingnan.sdk.enumeration;

/**
 * Created by Administrator on 2018/4/5.
 */
public enum  LineType {
    POSITIVE("积极情绪词"),
    NEGTIVE("消极情绪词"),
    SERENE("安详类情绪词"),
    HAPPY("快乐类情绪词"),
    PAIN("哀痛类情绪词"),
    SORROW("忧思类情绪词"),
    ANGRY("愤怒类情绪词"),
    FEAR("恐惧类情绪词"),
    HATE("厌恶类情绪词"),
    LIKE("喜爱类情绪词"),
    ASHAMED("羞愧类情绪词"),
    ANXIOUS("焦急类情绪词");
    private String element;
    LineType(String element){
        this.element = element;
    }
    public String toString() {
        return this.element;
    }
    //    /**
//     * 积极总类
//     */
//    String POSITIVE = "积极情绪词";
//    /**
//     * 消极总类
//     */
//    String NEGTIVE = "消极情绪词";
//    /**
//     * 积极安祥类
//     */
//    String SERENE = "安详类情绪词";
//    /**
//     * 积极快乐类
//     */
//    String HAPPY = "快乐类情绪词";
//    /**
//     * 消极痛苦类
//     */
//    String PAIN = "哀痛类情绪词";
//    /**
//     * 消极忧思类
//     */
//    String SORROW = "忧思类情绪词";
//    /**
//     * 消极愤怒类
//     */
//    String ANGRY = "愤怒类情绪词";
//    /**
//     * 消极恐惧类
//     */
//    String FEAR = "恐惧类情绪词";
//    /**
//     * 消极延误类
//     */
//    String HATE = "厌恶类情绪词";
//    /**
//     * 积极喜爱类
//     */
//    String LIKE = "喜爱类情绪词";
//    /**
//     * 消极羞愧类
//     */
//    String ASHAMED = "羞愧类情绪词";
//    /**
//     * 消极焦虑类
//     */
//    String ANXIOUS = "焦急类情绪词";

}
