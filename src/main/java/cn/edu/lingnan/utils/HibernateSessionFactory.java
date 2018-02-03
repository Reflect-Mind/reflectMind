package cn.edu.lingnan.utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Created by Administrator on 2018/1/21.
 */
public class HibernateSessionFactory {

    private static ThreadLocal<Session> sessionThreadLocal = null;
    private HibernateSessionFactory(){}
    private static final SessionFactory ourSessionFactory;
    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        if (sessionThreadLocal == null)
            sessionThreadLocal = new ThreadLocal<>();
        Session session = sessionThreadLocal.get();
        if (session == null){
            session = ourSessionFactory.openSession();
            sessionThreadLocal.set(session);
        }
        return session;
    }
    public static void closeSession(){
        Session session = sessionThreadLocal.get();
        sessionThreadLocal.set(null);
        if (session != null)
            session.close();
    }
    public static SessionFactory getSessionFactory(){
        return ourSessionFactory;
    }
}
