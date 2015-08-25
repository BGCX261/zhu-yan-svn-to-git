package com.dao.imp;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dao.XsDao;
import com.model.Xsb;
import com.sessionFactory.HibernateSessionFactory;

public class XsDaoImp implements XsDao {

	public Xsb getOneXs(String xh) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			Query query = session.createQuery("from Xsb where xh=?");
			query.setParameter(0, xh);
			query.setMaxResults(1);
			Xsb xs =(Xsb)query.uniqueResult();
			ts.commit();
			session.clear();
			if(xs!=null)
				return xs;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void update(Xsb xs) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			session.update(xs);
			ts.commit();
			HibernateSessionFactory.closeSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
