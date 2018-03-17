package cn.edu.lingnan.inteceptor;

import cn.edu.lingnan.sdk.CGLibProxy.AbstractFilter;
import cn.edu.lingnan.sdk.CGLibProxy.Filter;
import cn.edu.lingnan.sdk.CGLibProxy.InvokeProxy;
import cn.edu.lingnan.sdk.controller.Controller;
import cn.edu.lingnan.utils.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by Administrator on 2018/3/17.
 * @author feng
 * 打开会话过滤类
 */
public class OpenSessionInViewFilter<T> extends AbstractFilter<T>{



    public OpenSessionInViewFilter(T _target, Filter filter) {
        super(_target, filter);
    }
    @Override
    public Object invoke(InvokeProxy invokeProxy) throws Throwable {
        Object obj =  null;
        Session session = HibernateSessionFactory.getSession();
        Transaction transaction = session.beginTransaction();
        transaction.begin();
        try {
            obj = super.invoke(invokeProxy);
        } catch (Exception e){
            e.printStackTrace();
            transaction.rollback();
        }
        transaction.commit();
        HibernateSessionFactory.closeSession();
        return obj;
    }
}
