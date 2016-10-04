package com.gizwits.weixin.newGokitdog.db;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class HibernateModel {

	static HibernateModel hibernateModel;

	private HibernateModel() {

	}

	public static HibernateModel getInstance() {
		if (hibernateModel == null) {
			hibernateModel = new HibernateModel();
		}
		return hibernateModel;
	}

	public boolean saveOrUpdate(Object object) {
		boolean flag;
		Configuration cfg = new AnnotationConfiguration();
		SessionFactory sf = cfg.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		session.saveOrUpdate(object);
		System.out.println(session.save(object));
		try {
			session.getTransaction().commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		session.close();
		sf.close();
		return flag;
	}

	public String save(Object object) {

		Configuration cfg = new AnnotationConfiguration();
		SessionFactory sf = cfg.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		String idkey = "-1";
		try {
			idkey = session.save(object) + "";
			System.out.println(session.save(object));
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			idkey = "-1";
		}
		session.close();
		sf.close();
		return idkey;
	}

	public boolean deleteList(List<?> list) {
		boolean flag;
		Configuration cfg = new AnnotationConfiguration();
		SessionFactory sf = cfg.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		for (int i = 0; i < list.size(); i++) {
			session.delete(list.get(i));
		}
		try {
			session.getTransaction().commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		session.close();
		sf.close();
		return flag;
	}
	
	public boolean saveList(List<?> list) {
		boolean flag;
		Configuration cfg = new AnnotationConfiguration();
		SessionFactory sf = cfg.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		for (int i = 0; i < list.size(); i++) {
			session.save(list.get(i));
		}
		try {
			session.getTransaction().commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		session.close();
		sf.close();
		return flag;
	}

	public boolean update(Object object) {
		boolean flag;
		Configuration cfg = new AnnotationConfiguration();
		SessionFactory sf = cfg.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		session.update(object);

		try {
			session.getTransaction().commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		session.close();
		sf.close();
		return flag;
	}

	public <T> Object get(Class<T> clazz, int id) {
		Configuration cfg = new AnnotationConfiguration();
		SessionFactory sf = cfg.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		Object object = session.get(clazz, id);
		session.getTransaction().commit();
		session.close();
		sf.close();
		return object;
	}

	public <T> Object get(Class<T> clazz, String id) {
		Configuration cfg = new AnnotationConfiguration();
		SessionFactory sf = cfg.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		Object object = session.get(clazz, id);
		session.getTransaction().commit();
		session.close();
		sf.close();
		return (T) object;
	}

	public boolean delete(Object object) {
		boolean flag;
		Configuration cfg = new AnnotationConfiguration();
		SessionFactory sf = cfg.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		session.delete(object);
		try {
			session.getTransaction().commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		session.close();
		sf.close();

		return flag;
	}

	public <T> List<T> getList(String hql, Class<T> class1) {
		Configuration cfg = new AnnotationConfiguration();
		SessionFactory sf = cfg.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		List<?> list = session.createSQLQuery(hql).addEntity(class1).list();
		session.getTransaction().commit();
		session.close();
		sf.close();
		return (List<T>) list;
	}
	
	public boolean creatSql(String hql){
		Configuration cfg = new AnnotationConfiguration();
		SessionFactory sf = cfg.configure().buildSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();
		session.createSQLQuery(hql).executeUpdate();
		session.getTransaction().commit();
		session.close();
		sf.close();
		return true;
	}

}
