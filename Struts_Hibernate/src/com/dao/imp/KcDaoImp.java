package com.dao.imp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dao.KcDao;
import com.model.Kcb;
import com.sessionFactory.HibernateSessionFactory;

public class KcDaoImp implements KcDao {

	public List<Kcb> getAll() {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			List<Kcb> list = session.createQuery("from Kcb").list();
			ts.commit();
			HibernateSessionFactory.closeSession();
			if(list!=null)
				return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Kcb getOneKc(String kch) {
		try {
			Session session = HibernateSessionFactory.getSession();
			Transaction ts = session.beginTransaction();
			Query query = session.createQuery("from Kcb where kch=?");
			query.setParameter(0, kch);
			query.setMaxResults(1);
			Kcb kc =(Kcb)query.uniqueResult();
			ts.commit();
			session.clear();
			if(kc!=null)
				return kc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
