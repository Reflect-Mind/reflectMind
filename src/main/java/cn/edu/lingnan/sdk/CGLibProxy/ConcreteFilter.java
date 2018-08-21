package cn.edu.lingnan.sdk.CGLibProxy;

/**
 * Created by Administrator on 2018/1/27.
 */
public class ConcreteFilter<T> extends AbstractFilter<T> {

    public ConcreteFilter(T _target, Filter filter) {
        super(_target, filter);
    }

    @Override
    public Object invoke(InvokeProxy invokeProxy) throws Throwable {
        System.out.println(this);
        Object returnValue = super.invoke(invokeProxy);
        System.out.println(this);
        return returnValue;
    }
}
