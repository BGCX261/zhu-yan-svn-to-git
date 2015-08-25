package com.action;

import java.util.Map;

import com.dao.DlDao;
import com.dao.imp.DlDaoImp;
import com.model.Dlb;
import com.opensymphony.xwork2.ActionContext;

public class LoginAction {
	private Dlb dlb;

	public Dlb getDlb() {
		return dlb;
	}

	public void setDlb(Dlb dlb) {
		this.dlb = dlb;
	}
	
	public String execute() throws Exception{
		DlDao dlDao =new DlDaoImp();
		Dlb user =dlDao.validate(dlb.getXh(), dlb.getKl());
		if(user!=null){
			Map<String, Dlb> session =(Map<String, Dlb>)ActionContext.getContext().getSession();
			session.put("user", user);
			return "success";
		}else {
			return "error";
		}
	}
	
}
