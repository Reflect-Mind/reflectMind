package cn.edu.lingnan.sdk.CGLibProxy;

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
 */
public abstract class AbstractAOP<T> extends AbstractFilter<T>{
    //被拦截的对象
    private T target = null;
    public final static Boolean FORWARD = true;
    public final static Boolean TRUNCATE = false;

    public AbstractAOP(T _target){
        super(_target, null);
    }

    @Override
    public final Object invoke(InvokeProxy invokeProxy) throws Throwable {
        Object returnValue = null;
        String methodName = invokeProxy.getMethodName();
        Object[] args = invokeProxy.getArgs();
        try {
            this.before(target, methodName);
            if (this.aroundBefore(target, methodName, args));
                returnValue = super.invoke(invokeProxy);
            this.after(target, methodName);
        } catch (Exception e){
            this.exception(target, methodName, e);
        }
        finally {
            this.end(target, methodName);
        }
        return returnValue;
    }

    public void before(T target, String methodName){};
    public abstract Boolean aroundBefore(T target, String methodName, Object[] args);
    public void after(T target, String methodName){};
    public void exception(T target, String methodName, Exception e){};
    public void end(T target, String methodName){};
}
