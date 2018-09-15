package cn.edu.lingnan.sdk.Container;

import javafx.scene.control.IndexRange;
import javafx.util.Pair;

import java.io.Externalizable;
import java.util.List;

/**
 * 人生阶段容器
 * Created by Administrator on 2018/3/6.
 * @author 李田锋
 */
public interface PhaseContainer<T> extends Externalizable{
    void add(PhaseType type, T element);
    void addAll(PhaseType type, List<T> list);
    List<T> getPhase(PhaseType type);
    void clear();
    boolean  containAll(List<T> list);
    boolean contains(T element);
    <V> PhaseType valueOf(V element);
    int size();

}
