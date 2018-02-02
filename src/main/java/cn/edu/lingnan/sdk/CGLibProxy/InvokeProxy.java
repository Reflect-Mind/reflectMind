package cn.edu.lingnan.sdk.CGLibProxy;

import net.sf.cglib.proxy.MethodProxy;

/**
 * Created by Administrator on 2018/1/27.
 */
public class InvokeProxy {
    private MethodProxy proxy;
    private Object o;
    private Object[] args;
    private String methodName;
    public InvokeProxy(MethodProxy proxy, Object o, Object[] args, String methodName){
        this.proxy = proxy;
        this.o = o;
        this.args = args;
        this.methodName = methodName;
    }

    public MethodProxy getProxy() {
        return proxy;
    }

    public Object getO() {
        return o;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object invoke(Filter resource) throws Throwable {
        if (resource == null)
            return proxy.invokeSuper(this.o, this.args);
        else
            return resource.invoke(this);
    }
}
