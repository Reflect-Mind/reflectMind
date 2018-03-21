package cn.edu.lingnan.sdk.controller;

import cn.edu.lingnan.sdk.advice.*;
import cn.edu.lingnan.utils.R;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/27.
 * 通过值绑定的方式,进行控制器间的通信
 *
 */
public abstract class Controller implements Initializable,/* Observer,*/ Serializable {

    private Stage stage = null;
//    /**
//     * 每个控制器自带中介者类
//     * 而且接受来自其他类的通知
//     */
//    protected AbstractMediator mediator = Mediator.MediatorHolder.MEDIATOR;
    public Controller(){
        this.stage = R.getApplication().getStage();
    }

    public void setStage(Stage stage){
        this.stage = stage;

    }
    public Stage getStage(){
        if (this.stage == null)
            throw new NullPointerException("stage 为 null,请先进行stage值的注入!!");
        return this.stage;
    }
//    /**
//     * 普通事件的监听
//     * @param observable
//     * @param observer
//     * @param event
//     * @param args
//     * @param <T>
//     * @see cn.edu.lingnan.sdk.advice.Observer
//     */
//    @Override
//    public <T> void update(AbstractObservable observable, T observer, ApplicationEvent event, Object... args) {
//
//    }
//
//    /**
//     * 全局事件的监听
//     * @param observable
//     * @param observer
//     * @param event
//     * @param <T>
//     * @see cn.edu.lingnan.sdk.advice.Observer
//     */
//    @Override
//    public <T> void update(AbstractObservable observable, T observer, ApplicationEvent event) {
//
//    }
}
