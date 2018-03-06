package cn.edu.lingnan.sdk.Container;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */
public interface PhaseContainer<T> {
    void add(PhaseType type, T integer);
    List<T> getPhase(PhaseType type);
    void clear();
}
