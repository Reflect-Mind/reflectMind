package cn.edu.lingnan.sdk.CGLibProxy;

import net.sf.cglib.proxy.Enhancer;

/**
 * Created by Administrator on 2018/1/27.
 */
public class ProxyFactory {

    private ProxyFactory(){}
    public static <T> T getProxyInstance( T target, FilterAdapter interceptor){
        T proxy = null;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback( interceptor);
        proxy = (T) enhancer.create();
        return proxy;
    }
}
