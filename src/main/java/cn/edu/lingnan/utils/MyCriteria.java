package cn.edu.lingnan.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;



public class MyCriteria extends org.hibernate.internal.CriteriaImpl{

	
	/*this.getClass().getName()*/
	public MyCriteria(String entityOrClassName, SessionImplementor session) {
		super(entityOrClassName, session);
	}
	
	public <T> void outerTravel(T condition) throws IllegalArgumentException, IllegalAccessException, IntrospectionException{
		for (Field field: condition.getClass().getDeclaredFields()){
			
			field.setAccessible(true);
			Object value = field.get(condition);
			Class<?> type = field.getType();
			if (value == null)
				continue;
			if (type == String.class && value.equals(""))
				continue;
			System.out.println(field.getName());
			if (field.getType() == String.class)
				this.add(Restrictions.like(field.getName(),"" + field.get(condition), MatchMode.ANYWHERE));
			else if (field.getName().equals("highPrice"))
				this.add(Restrictions.le("price", field.get(condition)));
			else if (field.getName().equals("lowPrice"))
				this.add(Restrictions.ge("price", field.get(condition)));
			else
				this.add(Restrictions.ge(field.getName(), field.get(condition)));
		}
		
	}
	
	public <T> void add(T condition){
		boolean outTravel = true;
		if (condition != null){
			
			try{
				this.outerTravel(condition);
				//this.outerTravel(condition.getClass().getSuperclass());
			}
			catch (Exception e){
				e.printStackTrace();
			}
			
		}
	}

}
