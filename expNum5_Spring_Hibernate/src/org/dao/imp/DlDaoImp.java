package org.dao.imp;

import org.dao.DlDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.model.Dlb;

public class DlDaoImp implements DlDao{
	private SessionFactory sessionFactory;
	//����ע��SeeeionFactory����set����ע��
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(Dlb dlb) {
		try {
			//��ȡSession����
			Session session=sessionFactory.openSession();
			Transaction transaction=session.beginTransaction();
			session.save(dlb);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	} 
}
