package cn.edu.lingnan.sdk.advice;

/**
 * Created by Administrator on 2018/1/29.
 */
public class Mediator extends AbstractMediator {
    /**
     * 中介者实体类
     * 在此处加载托管容器加载的控制类
     * 又或者使用setter方法注入类
     */
    private Mediator(){
    }


    public static class MediatorHolder{
        public static AbstractMediator MEDIATOR = new Mediator();

    }
}
