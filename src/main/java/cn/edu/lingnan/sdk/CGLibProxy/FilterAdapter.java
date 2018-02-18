package cn.edu.lingnan.sdk.CGLibProxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/1/26.
 */

/**
 * 对于aroundBefore方法返回值的解释：
 *  TRUNCATE:终止所代理的方法的运行
 *  FORWARD:允许所代理的方法的运行
 * 对各通知方法的解释：
 *  try{
 *      before(..);
 *      if(aroundBefore(..))
 *          //业务代码
 *      after(..)
 *  }
 *  catch (Exception e){
 *      exception(..);
 *  }
 *  finally{
 *
 *  }
 * @param <T>
 *
 *
 *
 *  拦截器使用案例
    public static void main(String[] args){
       AhoCorasickImpl ahoCorasick = new AhoCorasickImpl();
        Filter<AhoCorasickImpl> filter = new ConcreteFilter<>(ahoCorasick, null);
        FilterAdapter<AhoCorasickImpl> filterAdapter = new FilterAdapter<>(ahoCorasick, filter);
        ahoCorasick = getProxyInstance(ahoCorasick, filterAdapter);
    }
* */
public class FilterAdapter<T> implements MethodInterceptor, Filter<T> {
    //被拦截的对象
    private T target = null;
    private InvokeProxy invokeProxy = null;
    protected Filter resource = null;
    public FilterAdapter(T _target, Filter filter){

        this.target = _target;
        this.resource = filter;
    }

    @Override
    public final Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object returnValue = null;
        this.invokeProxy = new InvokeProxy(methodProxy, o, objects, method.getName());
        returnValue = this.invoke(this.invokeProxy);
        return returnValue;
    }

    public  Object invoke(InvokeProxy invokeProxy) throws Throwable {

        return invokeProxy.invoke(resource);
    }

}
