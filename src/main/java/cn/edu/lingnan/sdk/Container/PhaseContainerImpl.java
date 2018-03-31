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
public class PhaseContainerImpl implements PhaseContainer<String, Pair<Integer, IndexRange>>{
    private List<String> childhoodPhase = new ArrayList<>();
    private List<String> middlePhase = new ArrayList<>();
    private List<String> collegePhase = new ArrayList<>();
    private List<String> workPhase = new ArrayList<>();

    private List<String> getListByPhase(PhaseType type){
        List<String> list =
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
    private int getTotalListSize(){
        return this.childhoodPhase.size() + this.middlePhase.size()
                + this.collegePhase.size() + this.workPhase.size();
    }
    /**
     * 把段落序号添加到相应人生阶段的容器当中
     * @param type
     * @param element
     */
    public void add(PhaseType type, String element) {
        List<String> list = this.getListByPhase(type);
        if (list != null)
            list.add(element);
    }

    /**
     * 根据人生阶段枚举类型获取相应的列表
     * @param type
     * @return
     */
    @Override
    public List<String> getPhase(PhaseType type) {
        List<String> list = this.getListByPhase(type);
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
        if (this.getTotalListSize() == targetListSize)
            return true;
        return false;
    }

    @Override
    public boolean contains(String element) {
        boolean returnValue = this.childhoodPhase.contains(element)
                || this.middlePhase.contains(element)
                || this.collegePhase.contains(element)
                || this.workPhase.contains(element);
        return returnValue;
    }
}
