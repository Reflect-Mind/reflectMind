package cn.edu.lingnan.sdk.CGLibProxy;

/**
 * Created by Administrator on 2018/1/27.
 */
public abstract class AbstractFilter<T> implements Filter<T> {

    protected T target = null;
    protected Filter resource = null;
    public AbstractFilter(T _target, Filter filter){
        this.target = _target;
        this.resource = filter;
    }
    public Object invoke(InvokeProxy invokeProxy) throws Throwable{
        return invokeProxy.invoke(resource);
    }
}
