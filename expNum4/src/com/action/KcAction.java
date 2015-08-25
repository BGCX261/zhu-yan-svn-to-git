package com.action;

import java.util.List;
import java.util.Map;

import org.dao.KcDao;

import com.dao.imp.KcDaoImp;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class KcAction extends ActionSupport{
	public String execute() throws Exception {
		KcDao kcDao=new KcDaoImp();
		List list=kcDao.getAll();
		Map request=(Map)ActionContext.getContext().get("request");
		request.put("list", list);
		return SUCCESS;
	}
}
