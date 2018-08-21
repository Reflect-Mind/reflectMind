package cn.edu.lingnan.sdk.CGLibProxy;

/**
 * Created by Administrator on 2018/1/27.
 */
public interface  Filter<T> {

    public Object invoke(InvokeProxy invokeProxy) throws Throwable;
}
