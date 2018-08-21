package cn.edu.lingnan.sdk;

import cn.edu.lingnan.sdk.CGLibProxy.ConcreteFilter;
import cn.edu.lingnan.sdk.controller.Controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Created by Administrator on 2018/1/28.
 */
public class ReflectionUtils {
    private ReflectionUtils(){}

    public static  <T> Constructor<T> getFitConstruct(Class<T> target ,Class<?>... params){
        Constructor<T> constructor = null;
        Constructor<T>[] constructors = (Constructor<T>[]) target.getConstructors();
        for (int count = 0; count < constructors.length; count++){
            try{
                Class<?>[] parameterTypes = constructors[count].getParameterTypes();
                constructor = target.getConstructor(parameterTypes);
            } catch (Exception e){
            }
        }
        return constructor;
    }

}
