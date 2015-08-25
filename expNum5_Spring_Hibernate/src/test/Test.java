package test;

import org.dao.DlDao;
import org.model.Dlb;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Test {
	public static void main(String[] args) {
		Dlb dlb=new Dlb();
		dlb.setXh("0840");
		dlb.setKl("123");
		ApplicationContext context=new
			FileSystemXmlApplicationContext("WebRoot/WEB-INF/applicationContext.xml");
		DlDao dlDao=(DlDao) context.getBean("dlDao");
		dlDao.save(dlb);
		System.out.println("²Ù×÷³É¹¦!");
	}
}
