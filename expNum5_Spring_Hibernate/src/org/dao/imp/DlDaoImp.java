package org.dao.imp;

import org.dao.DlDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.model.Dlb;

public class DlDaoImp implements DlDao{
	private SessionFactory sessionFactory;
	//依赖注入SeeeionFactory对象，set方法注入
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(Dlb dlb) {
		try {
			//获取Session对象
			Session session=sessionFactory.openSession();
			Transaction transaction=session.beginTransaction();
			session.save(dlb);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	} 
}
