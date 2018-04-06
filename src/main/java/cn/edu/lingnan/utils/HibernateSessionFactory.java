package cn.edu.lingnan.utils;

import cn.edu.lingnan.pojo.Vocab;
import cn.edu.lingnan.service.VocabService;
import cn.edu.lingnan.service.impl.VocabServiceImpl;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

/**
 * Created by Administrator on 2018/1/21.
 */
public class HibernateSessionFactory {

    private static ThreadLocal<Session> sessionThreadLocal = null;
    private HibernateSessionFactory(){}
    private static  SessionFactory ourSessionFactory;
    public static void init () {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("xml/hibernate.cfg.xml");
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.out.println(ex);
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

//    public static void main(String[] args){
//        HibernateSessionFactory.init();
//        VocabService vocabService = new VocabServiceImpl();
//        vocabService.findAllPsyChoVocab();
//    }
}
