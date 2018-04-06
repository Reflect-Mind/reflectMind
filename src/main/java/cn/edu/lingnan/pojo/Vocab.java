package cn.edu.lingnan.pojo;

import javafx.beans.property.*;

import javax.persistence.*;

/**
 * Created by Administrator on 2018/1/31.
 */
@Entity
public class Vocab {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty content = new SimpleStringProperty();
    private IntegerProperty status = new SimpleIntegerProperty();
    private IntegerProperty wordlen = new SimpleIntegerProperty();
    private IntegerProperty appearnum = new SimpleIntegerProperty();
    private DoubleProperty frq = new SimpleDoubleProperty();
    private DoubleProperty solid = new SimpleDoubleProperty();
    private DoubleProperty entropy = new SimpleDoubleProperty();
    private IntegerProperty categoryId = new SimpleIntegerProperty();
    private ObjectProperty<Category> categoryByCategoryId = new SimpleObjectProperty<>();

    @Id
    @Column(name = "id", nullable = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id.getValue();
    }

    public void setId(Integer id) {
        this.id.setValue(id);
    }

    @Basic
    @Column(name = "content", nullable = true, length = 255)
    public String getContent() {
        return content.get();
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status.getValue();
    }

    public void setStatus(Integer status) {
        this.status.setValue(status);
    }

    @Basic
    @Column(name = "wordlen", nullable = true)
    public Integer getWordlen() {
        return wordlen.getValue();
    }

    public void setWordlen(Integer wordlen) {
        this.wordlen.setValue(wordlen);
    }

    @Basic
    @Column(name = "appearnum", nullable = true)
    public Integer getAppearnum() {
        return appearnum.getValue();
    }

    public void setAppearnum(Integer appearnum) {
        this.appearnum.setValue(appearnum);
    }

    @Basic
    @Column(name = "frq", nullable = true, precision = 4)
    public Double getFrq() {
        return frq.getValue();
    }

    public void setFrq(Double frq) {
        this.frq.setValue(frq);
    }

    @Basic
    @Column(name = "solid", nullable = true, precision = 4)
    public Double getSolid() {
        return solid.getValue();
    }

    public void setSolid(Double solid) {
        this.solid.setValue(solid);
    }

    @Basic
    @Column(name = "entropy", nullable = true, precision = 4)
    public Double getEntropy() {
        return entropy.getValue();
    }

    public void setEntropy(Double entropy) {
        this.entropy.setValue(entropy);
    }

    @Basic
    @Column(name = "categoryId", nullable = true, insertable = false, updatable = false)
    public Integer getCategoryId() {
        return categoryId.getValue();
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId.setValue(categoryId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vocab vocab = (Vocab) o;

        if (id != null ? !id.equals(vocab.id) : vocab.id != null) return false;
        if (content != null ? !content.equals(vocab.content) : vocab.content != null) return false;
        if (status != null ? !status.equals(vocab.status) : vocab.status != null) return false;
        if (wordlen != null ? !wordlen.equals(vocab.wordlen) : vocab.wordlen != null) return false;
        if (appearnum != null ? !appearnum.equals(vocab.appearnum) : vocab.appearnum != null) return false;
        if (frq != null ? !frq.equals(vocab.frq) : vocab.frq != null) return false;
        if (solid != null ? !solid.equals(vocab.solid) : vocab.solid != null) return false;
        if (entropy != null ? !entropy.equals(vocab.entropy) : vocab.entropy != null) return false;
        if (categoryId != null ? !categoryId.equals(vocab.categoryId) : vocab.categoryId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (wordlen != null ? wordlen.hashCode() : 0);
        result = 31 * result + (appearnum != null ? appearnum.hashCode() : 0);
        result = 31 * result + (frq != null ? frq.hashCode() : 0);
        result = 31 * result + (solid != null ? solid.hashCode() : 0);
        result = 31 * result + (entropy != null ? entropy.hashCode() : 0);
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        return result;
    }

    @ManyToOne()
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    public Category getCategoryByCategoryId() {
        return categoryByCategoryId.get();
    }

    public void setCategoryByCategoryId(Category categoryByCategoryId) {
        this.categoryByCategoryId.set(categoryByCategoryId);
    }

    public IntegerProperty idProperty() {
        return id;
    }
    public StringProperty contentProperty() {
        return content;
    }

    public IntegerProperty statusProperty() {
        return status;
    }

    public IntegerProperty wordlenProperty() {
        return wordlen;
    }

    public IntegerProperty appearnumProperty() {
        return appearnum;
    }
    public DoubleProperty frqProperty() {
        return frq;
    }
    public DoubleProperty solidProperty() {
        return solid;
    }
    public DoubleProperty entropyProperty() {
        return entropy;
    }
    public IntegerProperty categoryIdProperty() {
        return categoryId;
    }
    public ObjectProperty<Category> categoryByCategoryIdProperty() {
        return categoryByCategoryId;
    }
}
