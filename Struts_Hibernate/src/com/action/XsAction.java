package com.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dao.XsDao;
import com.dao.ZyDao;
import com.dao.imp.KcDaoImp;
import com.dao.imp.XsDaoImp;
import com.dao.imp.ZyDaoImp;
import com.model.Dlb;
import com.model.Kcb;
import com.model.Xsb;
import com.model.Zyb;
import com.opensymphony.xwork2.ActionContext;

public class XsAction {
	XsDao xsDao;
	private Xsb xs;

	private Kcb kcb;

	public Kcb getKcb() {
		return kcb;
	}

	public void setKcb(Kcb kcb) {
		this.kcb = kcb;
	}

	private Zyb zyb;

	public Zyb getZyb() {
		return zyb;
	}

	public void setZyb(Zyb zyb) {
		this.zyb = zyb;
	}

	public Xsb getXs() {
		System.out.println("get xs");
		return xs;
	}

	public void setXs(Xsb xs) {
		this.xs = xs;
		System.out.println("set xs");
	}



	public String execute() throws Exception {
		Map<String, Dlb> session = (Map<String, Dlb>) ActionContext
				.getContext().getSession();
		Dlb user = (Dlb) session.get("user");
		xsDao = new XsDaoImp();
		Xsb xs = xsDao.getOneXs(user.getXh());
		System.out.println(xs.getXm());
		System.out.println(xs.getXh());
		Map<String, Xsb> request = (Map<String, Xsb>) ActionContext
				.getContext().get("request");
		request.put("xs", xs);
		return "success";
	}

	public String updateXsInfo() throws Exception {
		Map<String, Dlb> session = ActionContext.getContext().getSession();
		Dlb user = (Dlb) session.get("user");
		xsDao = new XsDaoImp();
		ZyDao zyDao = new ZyDaoImp();
		List<Zyb> zys = zyDao.getAll();
		Xsb xs = xsDao.getOneXs(user.getXh());
		Map request = (Map) ActionContext.getContext().get("request");
		request.put("zys", zys);
		request.put("xs", xs);
		return "success";
	}

	public String updateXs() throws Exception {
		xsDao = new XsDaoImp();
		ZyDao zyDao = new ZyDaoImp();
		Xsb stu = new Xsb();
		stu.setXh(xs.getXh());
		System.out.println(xs.getXm());
		Set list = xsDao.getOneXs(xs.getXh()).getKcs();
		stu.setKcs(list);
		stu.setXm(xs.getXm());
		System.out.println();
		stu.setXb(xs.getXb());
		stu.setCssj(xs.getCssj());
		stu.setBz(xs.getBz());
		stu.setZxf(xs.getZxf());
		Zyb zy = zyDao.getOneZy(zyb.getId());
		stu.setZyb(zy);
		xsDao.update(stu);
		// stu.setZyId(xs.getZyb().getId());
		return "success";
	}

	public String getXsKcs() throws Exception {
		Map session = (Map) ActionContext.getContext().getSession();
		Dlb user = (Dlb)session.get("user");
		String xh = user.getXh();
		Xsb xsb = new XsDaoImp().getOneXs(xh);
		Set list =xsb.getKcs();
		System.out.println("xuesh kcs "+list.size());
		Map request =(Map)ActionContext.getContext().get("request");
		request.put("list", list);
		return "success";
 	}
	
	public String deleteKc() throws Exception {
		Map session = (Map) ActionContext.getContext().getSession();
		Dlb user = (Dlb)session.get("user");
		String xh = user.getXh();
		XsDao xsDao =new XsDaoImp();
		Xsb xsb = xsDao.getOneXs(xh);
		Set list =xsb.getKcs();
		Iterator iter =list.iterator();
		while(iter.hasNext()){
			Kcb kc =(Kcb)iter.next();
			if(kc.getKch().equals(kcb.getKch())){
				iter.remove();
			}
		}
		xsb.setKcs(list);
		xsDao.update(xsb);
		return "success";
 	}
	
	public String selectKc() throws Exception {
		Map session = (Map) ActionContext.getContext().getSession();
		Dlb user = (Dlb)session.get("user");
		String xh = user.getXh();
		xsDao = new XsDaoImp();
		Xsb xs3 =xsDao.getOneXs(xh);
		Set list =xs3.getKcs();
		System.out.println(list.size()+"before add");
		Iterator iter =list.iterator();
		while(iter.hasNext()){
			Kcb kc =(Kcb)iter.next();
			if(kc.getKch().equals(kcb.getKch())){
				return "error";
			}
		}
		list.add(new KcDaoImp().getOneKc(kcb.getKch()));
		System.out.println(list.size()+"after add");
		xs3.setKcs(list);
		xsDao.update(xs3);
		return "success";
 	}
}
