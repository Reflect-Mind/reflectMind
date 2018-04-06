package cn.edu.lingnan.sdk.Container;

import javafx.scene.control.IndexRange;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */
public interface PhaseContainer<T> {
    void add(PhaseType type, T element);
    void addAll(PhaseType type, List<T> list);
    List<T> getPhase(PhaseType type);
    void clear();
    boolean  containAll(List<T> list);
    boolean contains(T element);
    <V> PhaseType valueOf(V element);
    int size();

}
