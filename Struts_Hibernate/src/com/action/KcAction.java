package com.action;

import java.util.List;
import java.util.Map;

import com.dao.KcDao;
import com.dao.imp.KcDaoImp;
import com.opensymphony.xwork2.ActionContext;

public class KcAction {
	public String execute() throws Exception{
		KcDao kcDao = new KcDaoImp();
		List list =kcDao.getAll();
		Map request =(Map)ActionContext.getContext().get("request");
		request.put("list", list);
		return "success";
	}
}
