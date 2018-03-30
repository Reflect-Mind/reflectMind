package cn.edu.lingnan.pojo;

/**
 * 词频查询树表类
 * Created by Mechan on 2018/3/28.
 */
public class FrqTree {

    //properties
    private String content;
    private Integer appearnum;
    private String category;
    private String theme;

    //constructors
    //空的构造器该怎么写？
    public FrqTree() { }

    public FrqTree(String content, Integer appearnum, String category, String theme) {
        this.content = content;
        this.appearnum = appearnum;
        this.category = category;
        this.theme = theme;
    }

    //getters and setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getAppearnum() {
        return appearnum;
    }

    public void setAppearnum(Integer appearnum) {
        this.appearnum = appearnum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

}
