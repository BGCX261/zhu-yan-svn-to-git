package com.dao.imp;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dao.DlDao;
import com.model.Dlb;
import com.sessionFactory.HibernateSessionFactory;

public class DlDaoImp implements DlDao {

	public Dlb validate(String xh, String kl) {
		try {
			Session session = HibernateSessionFactory.getSession();
			if(session == null)
				System.out.println("aaaaaaaaaaaaaaaaaaaa");
			Transaction ts = session.beginTransaction();
			Query query = session.createQuery("from Dlb where xh =? and kl=?");
			query.setParameter(0, xh);
			query.setParameter(1, kl);
			query.setMaxResults(1);
			Dlb dlb = (Dlb)query.uniqueResult();
			ts.commit();
			if(dlb!=null)
				return dlb;
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}

}
