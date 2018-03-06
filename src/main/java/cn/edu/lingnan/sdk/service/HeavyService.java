package cn.edu.lingnan.sdk.service;

import cn.edu.lingnan.sdk.advice.AbstractObservable;
import cn.edu.lingnan.sdk.advice.ApplicationEvent;
import cn.edu.lingnan.sdk.advice.Mediator;
import cn.edu.lingnan.sdk.advice.Observer;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2018/1/28.
 */
public  class HeavyService<V> extends ScheduledService<V>{

    private  HeavyService(){
    }
    private  static HeavyService heavyService = null;
    public static <V> HeavyService<V> getInstance(){
        if (heavyService == null)
            heavyService = new HeavyService<V>();
        return heavyService;
    }

    //缓存命令
    private AbstractQueue<Task<V>> commandList = new ConcurrentLinkedQueue<Task<V>>();
    //当缓存了多少的Command才进行重型操作命令
    private final static int SIZE = 5;
    private Task task = null;
    @Override
    protected synchronized final Task<V> createTask() {
        Task<V> command = null;
        command = this.commandList.poll();
        return command;
    }

    /**
     * 设置待执行的命令，当缓存到一定数量后
     * 执行命令缓存在命令序列的中的元素
     * @param command
     */
    public synchronized void setCommand(Task<V> command){
        this.commandList.add(command);
        if (this.commandList.size() >= SIZE) {
            this.executeImmediate();
        }
    }

    /**
     * 测试是否当前还有待访问的任务对象
     * @return
     */
    public synchronized boolean isEmpty(){
        return this.commandList.isEmpty();
    }

    /**
     * 立即执行命令
     * 当迫切需要执行时，则
     * 可以使用该方法。
     */
    public synchronized void executeImmediate(){
        if (this.getState().equals(State.READY))
            super.start();
        else if (this.getState().equals(State.CANCELLED))
            super.restart();
    }

    @Override
    protected void succeeded() {
        if (this.isEmpty())
            super.cancel();
        super.succeeded();
    }

    @Deprecated
    public void start() {
        super.start();
    }

    @Deprecated
    public void restart() {
        super.restart();
    }

    @Deprecated
    public boolean cancel() {
        return super.cancel();
    }
}
