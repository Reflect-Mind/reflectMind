package cn.edu.lingnan.sdk.service;

import javafx.application.Platform;

/**
 * Created by Administrator on 2018/1/28.
 *
 * 结果监听器,用于简单任务的结果回调
 * 一般情况下只能用于立即得到结果的查询结果
 * 的IO操作，用在更新操作上，会出现死锁状态，
 * 从而倒置无法同步用户的修改。
 * service层上查询方法的一般结构
 *        new Thread(() -> {
 *            try {
 *              resultListener.ready();
 *              //业务代码开开始
 *              ...
 *              //业务代码结束
 *              resultListener.send(FXCollections.observableArrayList(结果);
 *            } catch (Exception e) {
 *              resultListener.fail(e);
 *
 *            }
 *            }).start();
 * 对于更新的方法，将采用包装更新任务的形式
 * @see cn.edu.lingnan.service.impl.PersonServiceImpl
 */
public abstract class ResultListener<T> {
    public void send(T result){
        Platform.runLater(() -> this.finish(result));
    }
    /**
     * 执行就绪状态，通常用于窗口通知,
     * 让用户知道程序未就绪,需要进行等待。
     */
    public void ready(){};

    /**
     * 任务执行失败时,调用的方法
     *
     */
    public void fail(Exception e){}

    /**
     * 任务执行完成,传送执行需要运输的结果
     * @param result
     */
    protected abstract void finish(T result);

}
