package cn.edu.lingnan.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Administrator on 2018/1/31.
 */
@Entity
public class Category implements Serializable{
    private Integer id;
    private String content;
    private Theme themeByThemeId;
    private Collection<Vocab> vocabsById;
    private Integer themeId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != null ? !id.equals(category.id) : category.id != null) return false;
        if (content != null ? !content.equals(category.content) : category.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "themeId", referencedColumnName = "id")
    public Theme getThemeByThemeId() {
        return themeByThemeId;
    }

    public void setThemeByThemeId(Theme themeByThemeId) {
        this.themeByThemeId = themeByThemeId;
    }

    @OneToMany(mappedBy = "categoryByCategoryId")
    public Collection<Vocab> getVocabsById() {
        return vocabsById;
    }

    public void setVocabsById(Collection<Vocab> vocabsById) {
        this.vocabsById = vocabsById;
    }

    @Basic
    @Column(name = "themeId", nullable = true, updatable = false, insertable = false)
    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }
}
