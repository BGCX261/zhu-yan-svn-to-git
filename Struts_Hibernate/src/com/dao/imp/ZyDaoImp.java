package com.dao.imp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dao.ZyDao;
import com.model.Zyb;
import com.sessionFactory.HibernateSessionFactory;

public class ZyDaoImp implements ZyDao {

	public List<Zyb> getAll() {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			List<Zyb> list = (List<Zyb>)session.createQuery("from Zyb").list();
			ts.commit();
			HibernateSessionFactory.closeSession();
			if(list!=null)
				return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Zyb getOneZy(int zyId) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			Query query = session.createQuery("from Zyb where id=?");
			query.setParameter(0, zyId);
			query.setMaxResults(1);
			Zyb zy =(Zyb)query.uniqueResult();
			ts.commit();
			session.clear();
			if(zy!=null)
				return zy;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
