package cn.edu.lingnan.sdk.Container;

/**
 * Created by Administrator on 2018/3/6.
 * 人生阶段枚举类型
 */
public enum PhaseType {
    /**
     * 童年阶段
     */
    CHILDHOOD("童年阶段"),
    /**
     * 中学阶段
     */
    MIDDLE("中学阶段"),
    /**
     * 大学阶段
     */
    COLLEGE("大学阶段"),
    /**
     * 工作阶段
     */
    WORK("工作阶段");

    PhaseType(String phaseString){
        this.phaseString = phaseString;
    }
    private String phaseString;

    public String toString() {
        return this.phaseString;
    }
}
