package cn.edu.lingnan.pojo;

/**
 * 词汇统计树表实体
 * Created by Mechan on 2018/3/30.
 */
public class PsychoTree {

    //properties
    private String theme;
    private String category;
    private Integer appearnum;
    private Double frq;

    //constructors
    //空的构造器
    public PsychoTree() {}

    public PsychoTree(String theme, String category, Integer appearnum, Double frq) {
        this.theme = theme;
        this.category = category;
        this.appearnum = appearnum;
        this.frq = frq;
    }

    //getters and setters
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getAppearnum() {
        return appearnum;
    }

    public void setAppearnum(Integer appearnum) {
        this.appearnum = appearnum;
    }

    public Double getFrq() {
        return frq;
    }

    public void setFrq(Double frq) {
        this.frq = frq;
    }
}
