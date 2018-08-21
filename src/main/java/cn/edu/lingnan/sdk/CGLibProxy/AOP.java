package cn.edu.lingnan.sdk.CGLibProxy;

import cn.edu.lingnan.sdk.controller.Controller;

/**
 * Created by Administrator on 2018/1/27.
 */
public class AOP<Controller> extends AbstractAOP<Controller> {

    public AOP(Controller _target) {
        super(_target);
    }

    @Override
    public Boolean aroundBefore(Controller target, String methodName, Object[] args) {
        System.out.println("aroundBefore");
        return FORWARD;
    }
}
