package cn.edu.lingnan.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Administrator on 2018/1/31.
 */
@Entity
public class Character {
    private Integer id;
    private String content;
    private Integer appearnum;
    private Double frq;
    private Double entropy;

    @Id
    @Column(name = "id", nullable = true)

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "content", nullable = true, length = 255)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "appearnum", nullable = true)
    public Integer getAppearnum() {
        return appearnum;
    }

    public void setAppearnum(Integer appearnum) {
        this.appearnum = appearnum;
    }

    @Basic
    @Column(name = "frq", nullable = true, precision = 4)
    public Double getFrq() {
        return frq;
    }

    public void setFrq(Double frq) {
        this.frq = frq;
    }

    @Basic
    @Column(name = "entropy", nullable = true, precision = 4)
    public Double getEntropy() {
        return entropy;
    }

    public void setEntropy(Double entropy) {
        this.entropy = entropy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Character character = (Character) o;

        if (id != null ? !id.equals(character.id) : character.id != null) return false;
        if (content != null ? !content.equals(character.content) : character.content != null) return false;
        if (appearnum != null ? !appearnum.equals(character.appearnum) : character.appearnum != null) return false;
        if (frq != null ? !frq.equals(character.frq) : character.frq != null) return false;
        if (entropy != null ? !entropy.equals(character.entropy) : character.entropy != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (appearnum != null ? appearnum.hashCode() : 0);
        result = 31 * result + (frq != null ? frq.hashCode() : 0);
        result = 31 * result + (entropy != null ? entropy.hashCode() : 0);
        return result;
    }
}
