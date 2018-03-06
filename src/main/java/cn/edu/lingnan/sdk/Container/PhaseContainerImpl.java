package cn.edu.lingnan.sdk.Container;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Administrator on 2018/3/6.
 * 人生阶段容器
 *
 */
public class PhaseContainerImpl implements PhaseContainer<Integer>{
    private List<Integer> primaryPhase = new ArrayList<>();
    private List<Integer> juniorPhase = new ArrayList<>();
    private List<Integer> seniorPhase = new ArrayList<>();
    private List<Integer> workPhase = new ArrayList<>();

    private List<Integer> getListByPhase(PhaseType type){
        List<Integer> list =
                type == null? null:
                        type == PhaseType.PRIMARY? this.primaryPhase:
                                type == PhaseType.JUNIOR? this.juniorPhase:
                                        type == PhaseType.SENIOR? this.seniorPhase:
                                                type == PhaseType.WORK? this.workPhase: null;
        return list;
    }
    /**
     * 把段落序号添加到相应人生阶段的容器当中
     * @param type
     * @param integer
     */
    public void add(PhaseType type, Integer integer) {
        List<Integer> list = this.getListByPhase(type);
        if (list != null)
            list.add(integer);
    }

    /**
     * 根据人生阶段枚举类型获取相应的列表
     * @param type
     * @return
     */
    @Override
    public List<Integer> getPhase(PhaseType type) {
        List<Integer> list = this.getListByPhase(type);
        return list;
    }

    /**
     * 消除人生阶段容器中的数据
     */
    public void clear() {
        this.juniorPhase.clear();
        this.workPhase.clear();
        this.seniorPhase.clear();
        this.workPhase.clear();
    }
}
