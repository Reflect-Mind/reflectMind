package cn.edu.lingnan.sdk.advice;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2018/1/29.
 */
public abstract class AbstractObservable {
    private Vector<Observer> observerVector = new Vector<>();

    /**
     * 测试是否会重复添加观察者
     * @param observer
     */
    public boolean addObserver(Observer observer){
        return this.observerVector.add(observer);
    }
    //删除观察者
    public boolean removeObserver(Observer observer){
         return this.observerVector.remove(observer);
    }
    //获取观察者
    public Observer getObserver(Class<? extends Observer> clz){
        for (Observer observer: this.observerVector){
            if (observer.getClass() == clz)
                return observer;
        }
        return null;
    }

    /**
     * 基于任务的通知事件
     * 通知部分观察者
     * 需要局部更新
     * @param observer 发出该通知的通知者
     * @param targets 需要被告知的通知者
     * @param args 通知的相关参数
     */
    public <V> Task<V> update(Observer observer,
                                        Class<? extends Observer>[] targets, Object... args){

        Task<V> task = new Task<V>() {
            @Override
            protected V call() throws Exception {
                V value = null;
                for (Class clz: targets){
                    for (Observer candidate: observerVector){
                        if (candidate.getClass() == clz)
                            candidate.update(AbstractObservable.this, observer, args);
                    }
                }

                return value;
            }
        };
        return task;
    }

    /**
     * 基于任务的通知事件
     * 通知所有观察者
     * 通常用于通知全局事件
     * 如：程序开始运行的事件
     * 程序将要关闭的事件
     * 相关事件请查阅:
     * @param applicationEvents
     * @param observer
     * @see cn.edu.lingnan.sdk.advice.ApplicationEvent
     */
    public <V> Task<V> updateAll(Observer observer,
                                       ApplicationEvent applicationEvents){
        Task<V> task = new Task<V>() {
            @Override
            protected V call() throws Exception {
                V value = null;
                int totalWork = observerVector.size();
                for (Observer target: observerVector){
                    target.update(AbstractObservable.this, observer, applicationEvents);

                }
                return value;
            }
        };
        return task;
    }
}
