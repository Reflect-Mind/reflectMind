package cn.edu.lingnan.sdk.Container;

import javafx.scene.control.IndexRange;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Administrator on 2018/3/6.
 * 人生阶段容器
 *
 */
public class PhaseContainerImpl implements PhaseContainer<Pair<Integer, IndexRange>>{
    private List<Pair<Integer, IndexRange>> childhoodPhase = new ArrayList<>();
    private List<Pair<Integer, IndexRange>> middlePhase = new ArrayList<>();
    private List<Pair<Integer, IndexRange>> collegePhase = new ArrayList<>();
    private List<Pair<Integer, IndexRange>> workPhase = new ArrayList<>();

    private List<Pair<Integer, IndexRange>> getListByPhase(PhaseType type){
        List<Pair<Integer, IndexRange>> list =
                type == null? null:
                        type == PhaseType.CHILDHOOD? this.childhoodPhase:
                                type == PhaseType.MIDDLE? this.middlePhase:
                                        type == PhaseType.COLLEGE? this.collegePhase:
                                                type == PhaseType.WORK? this.workPhase: null;
        return list;
    }

    /**
     * 获取四大人生阶段容器的囊括段落范围
     * @return
     */
    public int size(){
        return this.childhoodPhase.size() + this.middlePhase.size()
                + this.collegePhase.size() + this.workPhase.size();
    }
    /**
     * 把段落序号添加到相应人生阶段的容器当中
     * @param type
     * @param element
     */
    public void add(PhaseType type, Pair<Integer, IndexRange> element) {
        List<Pair<Integer, IndexRange>> list = this.getListByPhase(type);
        if (list != null)
            list.add(element);
    }

    /**
     * 将段落字符串列表加入到相应的人生阶段当中
     * @param type
     * @param source
     */
    @Override
    public void addAll(PhaseType type, List<Pair<Integer, IndexRange>> source) {
        List<Pair<Integer, IndexRange>> target = this.getListByPhase(type);
        target.addAll(source);
    }

    /**
     * 根据人生阶段枚举类型获取相应的列表
     * @param type
     * @return
     */
    @Override
    public List<Pair<Integer, IndexRange>> getPhase(PhaseType type) {
        List<Pair<Integer, IndexRange>> list = this.getListByPhase(type);
        return list;
    }

    /**
     * 消除人生阶段容器中的数据
     */
    public void clear() {
        this.childhoodPhase.clear();
        this.collegePhase.clear();
        this.middlePhase.clear();
        this.workPhase.clear();
    }

    /**
     * 是否包含所有的段落（受）
     * 当包含所有的受段落时，将返回true
     * 否则返回false
     * @param list
     * @return boolean
     */
    @Override
    public boolean containAll(List<Pair<Integer, IndexRange>> list) {
        int targetListSize = list.size();
        if (this.size() == targetListSize)
            return true;
        return false;
    }

    @Override
    public boolean contains(Pair<Integer, IndexRange> element) {
        return this.childhoodPhase.contains(element)
                || this.middlePhase.contains(element)
                || this.collegePhase.contains(element)
                || this.workPhase.contains(element);
    }

    @Override
    public <V> PhaseType valueOf(V element) {
        if (element instanceof Pair)
            return this.valueOf((Pair<Integer, IndexRange>)element);
        else if (element instanceof IndexRange)
            return this.valueOf((IndexRange)element);
        return null;
    }

    /**
     * 返回值所在的人生阶段
     * @param element
     * @return
     */
    public PhaseType valueOf(Pair<Integer, IndexRange> element) {

        return this.childhoodPhase.contains(element)? PhaseType.CHILDHOOD:
                this.middlePhase.contains(element)? PhaseType.MIDDLE:
                        this.collegePhase.contains(element)? PhaseType.COLLEGE:
                                this.workPhase.contains(element)? PhaseType.WORK: null;
    }

    /**
     * 查询某列表是否含有该range
     * @param list
     * @param range
     * @return
     */
    private boolean withIn(List<Pair<Integer, IndexRange>> list, IndexRange range){
        for (Pair<Integer, IndexRange> pair: list){
            if (pair.getValue().equals(range))
                return true;
        }
        return false;
    }
    /**
     * 返回值所在的人生阶段
     * @param range
     * @return
     */
    public PhaseType valueOf(IndexRange range){
        return this.withIn(this.childhoodPhase, range)? PhaseType.CHILDHOOD:
                this.withIn(this.middlePhase, range)? PhaseType.MIDDLE:
                        this.withIn(this.collegePhase, range)? PhaseType.COLLEGE:
                                this.withIn(this.workPhase, range)? PhaseType.WORK:  null;
    }
}
