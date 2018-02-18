package cn.edu.lingnan.sdk.advice;

/**
 * Created by Administrator on 2018/1/29.
 * Observable: 被观察者
 * Observer: 观察者
 * args: 传递的参数
 */
public interface Observer {

    public abstract <T> void update(AbstractObservable observable, T observer, ApplicationEvent event, Object... args);
    public abstract <T> void update(AbstractObservable observable, T observer, ApplicationEvent event);
}
