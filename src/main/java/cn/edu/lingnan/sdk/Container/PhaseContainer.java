package cn.edu.lingnan.sdk.Container;

import javafx.scene.control.IndexRange;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */
public interface PhaseContainer<T, V> {
    void add(PhaseType type, T integer);
    List<T> getPhase(PhaseType type);
    void clear();
    boolean  containAll(List<V> list);
}
