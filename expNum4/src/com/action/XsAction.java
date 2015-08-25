package com.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dao.XsDao;

import com.dao.ZyDao;
import com.dao.imp.KcDaoImp;
import com.dao.imp.XsDaoImp;
import com.dao.imp.ZyDaoImp;
import com.model.Dlb;
import com.model.Kcb;
import com.model.Xsb;
import com.model.Zyb;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class XsAction extends ActionSupport{
	XsDao xsDao;
	//定义学生对象
	private Xsb xs;
	//定义课程信息
	private Kcb kcb;
	//定义专业对象
	private Zyb zyb;
	public Xsb getXs() {
		return xs;
	}
	public void setXs(Xsb xs) {
		this.xs = xs;
	}
	public Kcb getKcb() {
		return kcb;
	}
	public void setKcb(Kcb kcb) {
		this.kcb = kcb;
	}	
	public Zyb getZyb() {
		return zyb;
	}
	public void setZyb(Zyb zyb) {
		this.zyb = zyb;
	}
	
	public String execute() throws Exception {
		Map<String, Dlb> session = (Map<String, Dlb>) ActionContext
				.getContext().getSession();
		Dlb user = (Dlb) session.get("user");
		xsDao = new XsDaoImp();
		Xsb xs = xsDao.getOneXs(user.getXh());
		Map<String, Xsb> request = (Map<String, Xsb>) ActionContext
				.getContext().get("request");
		request.put("xs", xs);
		return SUCCESS;
	}
	
	public String updateXsInfo() throws Exception {
		Map<String, Dlb> session = ActionContext.getContext().getSession();
		Dlb user = (Dlb) session.get("user");
		xsDao = new XsDaoImp();
		ZyDao zyDao = new ZyDaoImp();
		List<Zyb> zys = zyDao.getAll();
		for(int i=0;i<zys.size();i++)
			System.out.println(zys.get(i).getZym());
		Xsb xs = xsDao.getOneXs(user.getXh());
		Map request = (Map) ActionContext.getContext().get("request");
		request.put("zys", zys);
		request.put("xs", xs);
		return SUCCESS;
	}

	public String updateXs() throws Exception {
		xsDao = new XsDaoImp();
		ZyDao zyDao = new ZyDaoImp();
		Xsb stu = new Xsb();
		stu.setXh(xs.getXh());
		Set list = xsDao.getOneXs(xs.getXh()).getKcs();
		stu.setKcs(list);
		stu.setXm(xs.getXm());
		stu.setXb(xs.getXb());
		stu.setCssj(xs.getCssj());
		stu.setBz(xs.getBz());
		stu.setZxf(xs.getZxf());
		Zyb zy = zyDao.getOneZy(zyb.getId());
		stu.setZyb(zy);
		xsDao.update(stu);
		return SUCCESS;
	}
	
	//得到学生选修的课程
	public String getXsKcs() throws Exception{
		Map session=(Map)ActionContext.getContext().getSession();
		Dlb user=(Dlb)session.get("user");
		String xh=user.getXh();
		//得到当前学生的信息
		Xsb xsb=new XsDaoImp().getOneXs(user.getXh());
		System.out.println("学生选修的课程"+user.getXh());
		//取出选修的课程set
		Set list=xsb.getKcs();
		Map request=(Map)ActionContext.getContext().get("request");
		//保存
		request.put("list", list);
		return SUCCESS;		
	}
	
	//退选课程
	public String deleteKc() throws Exception {
		Map session=(Map)ActionContext.getContext().getSession();
		String xh=((Dlb)session.get("user")).getXh();
		xsDao=new XsDaoImp();
		Xsb xs2=xsDao.getOneXs(xh);
		Set list=xs2.getKcs();
		Iterator iter=list.iterator();
		//取出所有选择的课程进行迭代
		while(iter.hasNext()){
			Kcb kc2=(Kcb)iter.next();
			//如果遍历到退选的课程的课程号就从list中删除
			if(kc2.getKch().equals(kcb.getKch())){
				iter.remove();
			}
		}
		//设置课程的set
		xs2.setKcs(list);
		xsDao.update(xs2);
		return SUCCESS;	
	}
	
	//选定课程
	public String selectKc() throws Exception {
		Map session=(Map)ActionContext.getContext().getSession();
		String xh=((Dlb)session.get("user")).getXh();
		xsDao=new XsDaoImp();
		Xsb xs3=xsDao.getOneXs(xh);
		Set list=xs3.getKcs();
		Iterator iter=list.iterator();
		//选修课程时先遍历已经选的课程，如果在已经选的课程中找到就返回ERROR
		while(iter.hasNext()){
			Kcb kc3=(Kcb)iter.next();
			//如果遍历到退选的课程的课程号就从list中删除
			if(kc3.getKch().equals(kcb.getKch())){
				return ERROR;
			}
		}
		//如果没有找到，就添加到集合中
		list.add(new KcDaoImp().getoneKc(kcb.getKch()));
		xs3.setKcs(list);
		xsDao.update(xs3);
		return SUCCESS;	
	}
}



