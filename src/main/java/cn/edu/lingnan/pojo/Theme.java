package cn.edu.lingnan.pojo;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Administrator on 2018/1/31.
 */
@Entity
public class Theme {
    private Integer id;
    private String content;
    private String belongTo;
    private Collection<Category> categoriesById;

    @Id
    @Column(name = "id", nullable = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "content", nullable = true, length = -1)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "belongto")
    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Theme theme = (Theme) o;

        if (id != null ? !id.equals(theme.id) : theme.id != null) return false;
        if (content != null ? !content.equals(theme.content) : theme.content != null) return false;
        if (belongTo != null? !belongTo.equals(theme.belongTo): theme.belongTo != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "themeByThemeId")
    public Collection<Category> getCategoriesById() {
        return categoriesById;
    }

    public void setCategoriesById(Collection<Category> categoriesById) {
        this.categoriesById = categoriesById;
    }
}
