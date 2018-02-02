package cn.edu.lingnan.pojo;

import javax.persistence.*;

/**
 * Created by Administrator on 2018/1/31.
 */
@Entity
public class VocabLog {
    private Integer id;
    private Integer charnum;
    private Integer recognum;
    private Integer newnum;
    private String filename;
    private String addtime;
    private String taketime;

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
    @Column(name = "charnum", nullable = true)
    public Integer getCharnum() {
        return charnum;
    }

    public void setCharnum(Integer charnum) {
        this.charnum = charnum;
    }

    @Basic
    @Column(name = "recognum", nullable = true)
    public Integer getRecognum() {
        return recognum;
    }

    public void setRecognum(Integer recognum) {
        this.recognum = recognum;
    }

    @Basic
    @Column(name = "newnum", nullable = true)
    public Integer getNewnum() {
        return newnum;
    }

    public void setNewnum(Integer newnum) {
        this.newnum = newnum;
    }

    @Basic
    @Column(name = "filename", nullable = true, length = 255)
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Basic
    @Column(name = "addtime", nullable = true, length = 255)
    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    @Basic
    @Column(name = "taketime", nullable = true, precision = 4)
    public String getTaketime() {
        return taketime;
    }

    public void setTaketime(String taketime) {
        this.taketime = taketime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VocabLog vocabLog = (VocabLog) o;

        if (id != null ? !id.equals(vocabLog.id) : vocabLog.id != null) return false;
        if (charnum != null ? !charnum.equals(vocabLog.charnum) : vocabLog.charnum != null) return false;
        if (recognum != null ? !recognum.equals(vocabLog.recognum) : vocabLog.recognum != null) return false;
        if (newnum != null ? !newnum.equals(vocabLog.newnum) : vocabLog.newnum != null) return false;
        if (filename != null ? !filename.equals(vocabLog.filename) : vocabLog.filename != null) return false;
        if (addtime != null ? !addtime.equals(vocabLog.addtime) : vocabLog.addtime != null) return false;
        if (taketime != null ? !taketime.equals(vocabLog.taketime) : vocabLog.taketime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (charnum != null ? charnum.hashCode() : 0);
        result = 31 * result + (recognum != null ? recognum.hashCode() : 0);
        result = 31 * result + (newnum != null ? newnum.hashCode() : 0);
        result = 31 * result + (filename != null ? filename.hashCode() : 0);
        result = 31 * result + (addtime != null ? addtime.hashCode() : 0);
        result = 31 * result + (taketime != null ? taketime.hashCode() : 0);
        return result;
    }
}
