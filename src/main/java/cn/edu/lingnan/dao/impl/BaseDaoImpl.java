package cn.edu.lingnan.dao.impl;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Resource;

import cn.edu.lingnan.dao.BaseDao;
import cn.edu.lingnan.utils.HibernateSessionFactory;
import cn.edu.lingnan.utils.MyCriteria;
import org.hibernate.*;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;

public class BaseDaoImpl<T> implements BaseDao<T> {

	
	protected Class<T> entityClass;

	public Session getCurrentSession() {
		return HibernateSessionFactory.getSession();
	}

	protected Class getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}
		return entityClass;
	}

	@Override
	public Serializable save(T entity) {
		Transaction transaction = this.getCurrentSession().beginTransaction();
		Serializable id =  this.getCurrentSession().save(entity);
		transaction.commit();
		HibernateSessionFactory.closeSession();
		return id;
	}

	@Override
	public void delete(T entity) {
		Transaction transaction = this.getCurrentSession().beginTransaction();
		this.getCurrentSession().delete(entity);
		transaction.commit();
		HibernateSessionFactory.closeSession();
	}

	@Override
	public void update(T entity) {
		Transaction transaction = this.getCurrentSession().beginTransaction();
		this.getCurrentSession().update(entity);
		transaction.commit();
		HibernateSessionFactory.closeSession();
	}
	
	@Override
	public void merge(T entity) {
		Transaction transaction = this.getCurrentSession().beginTransaction();
		this.getCurrentSession().merge(entity);
		transaction.commit();
		HibernateSessionFactory.closeSession();
	}
	
	@Override //实例查询
	public List<T> find(T condition) {
		this.getCurrentSession().beginTransaction();
		Criteria criteria = this.getCurrentSession().createCriteria(getEntityClass());
		criteria.add(Example.create(condition));
		List<T> list = criteria.list();
		HibernateSessionFactory.closeSession();
		return list;
	}

	@Override
	public T findById(Serializable id) {
		this.getCurrentSession().beginTransaction();
		T load = (T) this.getCurrentSession().get(getEntityClass(), id);
		HibernateSessionFactory.closeSession();
		return load;
	}

	/**
	 * <根据HQL语句查找唯一实体>
	 * 
	 * @param hqlString
	 *            HQL语句
	 * @param values
	 *            不定参数的Object数组
	 * @return 查询实体
	 * @see )
	 */
	@Override  //  "from Auction a a.name=? and a.xxx=?"  
	public T getByHQL(String hqlString, Object... values) {
		this.getCurrentSession().beginTransaction();
		Query query = this.getCurrentSession().createQuery(hqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);  //设置占位符
			}
		}
		T result = (T) query.uniqueResult();
		HibernateSessionFactory.closeSession();
		return result;
	}

	/**
	 * <根据SQL语句查找唯一实体>
	 * 
	 * @param sqlString
	 *            SQL语句
	 * @param values
	 *            不定参数的Object数组
	 * @return 查询实体
	 * @see )
	 */
	@Override
	public T getBySQL(String sqlString, Object... values) {
		this.getCurrentSession().beginTransaction();
		Query query = this.getCurrentSession().createSQLQuery(sqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		T result = (T) query.uniqueResult();
		HibernateSessionFactory.closeSession();
		return result;
	}

	/**
	 * <根据HQL语句，得到对应的list>
	 * 
	 * @param hqlString
	 *            HQL语句
	 * @param values
	 *            不定参数的Object数组
	 * @return 查询多个实体的List集合
	 * @see )
	 */
	@Override
	public List<T> getListByHQL(String hqlString, Object... values) {
		Session session = this.getCurrentSession();
		session.beginTransaction();
		Query query = this.getCurrentSession().createQuery(hqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		List<T> list = query.list();
		HibernateSessionFactory.closeSession();
		System.out.println("baseDaoImpl:" + session);
		return list;
	}

	/**
	 * <根据SQL语句，得到对应的list>
	 * 
	 * @param sqlString
	 *            HQL语句
	 * @param values
	 *            不定参数的Object数组
	 * @return 查询多个实体的List集合
	 * @see )
	 */
	@Override
	public List<T> getListBySQL(String sqlString, Object... values) {
		this.getCurrentSession().beginTransaction();
		Query query = this.getCurrentSession().createSQLQuery(sqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		List<T> list = query.list();
		HibernateSessionFactory.closeSession();
		return list;
	}

	@Override
	public List<T> queryListObjectAllForPage(int pageSize, int page, String hqlString, Object... values) {
		this.getCurrentSession().beginTransaction();
		Query query = this.getCurrentSession().createQuery(hqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<T> list = query.list();
		HibernateSessionFactory.closeSession();
		return list;
	}

	@Override //单值查询
	public Object uniqueResult(String hqlString, Object... values) {
		this.getCurrentSession().beginTransaction();
		Query query = this.getCurrentSession().createQuery(hqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		Object result = query.uniqueResult();
		HibernateSessionFactory.closeSession();
		return result;
	}
	
	@Override //分页单值查询, 带分页
	public Object uniqueResultForPages(String hqlString, int pageSize,
			int page, Object... values) {
		this.getCurrentSession().beginTransaction();
		Query query = this.getCurrentSession().createQuery(hqlString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		Object result = query.uniqueResult();
		HibernateSessionFactory.closeSession();
		return result;
	}
	@Override
	/**
	 * 条件分页查询
	 */
	public List<T> queryListObjecgtAllForPage(int pageSize, int page,
			T condition,Order order) {
		this.getCurrentSession().beginTransaction();
		CriteriaImpl  temp = (CriteriaImpl) this.getCurrentSession().createCriteria(this.getEntityClass());
		MyCriteria criteria = new MyCriteria(this.getEntityClass().getName(),temp.getSession());
		criteria.add(condition);
		criteria.setMaxResults(pageSize);
		criteria.setFirstResult((page - 1) * pageSize);
		List<T> list = criteria.list();
		HibernateSessionFactory.closeSession();
		return list;
	}
	@Override
	public Object uniqueResultForPages(T condition) {
		this.getCurrentSession().beginTransaction();
		CriteriaImpl  temp = (CriteriaImpl) this.getCurrentSession().createCriteria(this.getEntityClass());
		MyCriteria criteria = new MyCriteria(this.getEntityClass().getName(),temp.getSession());
		criteria.add(condition);
		criteria.setProjection(Projections.rowCount());
		Object result = criteria.uniqueResult();
		HibernateSessionFactory.closeSession();
		return result;
	}
	
}
