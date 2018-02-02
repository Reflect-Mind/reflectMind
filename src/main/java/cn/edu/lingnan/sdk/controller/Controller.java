package cn.edu.lingnan.sdk.controller;

import cn.edu.lingnan.sdk.advice.*;
import javafx.fxml.Initializable;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/27.
 *
 */
public abstract class Controller implements Initializable, Observer, Serializable {

    /**
     * 每个控制器自带中介者类
     * 而且接受来自其他类的通知
     */
    protected AbstractMediator mediator = Mediator.MediatorHolder.MEDIATOR;
    public Controller(){
        this.mediator.addObserver(this);
    }

    /**
     * 普通事件的监听
     * @param observable
     * @param observer
     * @param args
     * @param <T>
     * @see cn.edu.lingnan.sdk.advice.Observer
     */
    @Override
    public <T> void update(AbstractObservable observable, T observer, Object... args) {

    }

    /**
     * 全局事件的监听
     * @param observable
     * @param observer
     * @param event
     * @param <T>
     * @see cn.edu.lingnan.sdk.advice.Observer
     */
    @Override
    public <T> void update(AbstractObservable observable, T observer, ApplicationEvent event) {

    }
}
