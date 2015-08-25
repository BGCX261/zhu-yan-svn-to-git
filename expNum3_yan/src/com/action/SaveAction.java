package com.action;

import com.db.Xsb;
import com.model.DBConn;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport {
	private Xsb xs;
	public Xsb getXs() {
		return xs;
	}
	public void setXs(Xsb xs) {
		this.xs = xs;
	}
	public String execute() throws Exception {
		DBConn db = new DBConn();
		Xsb stu = new Xsb();
		stu.setXh(xs.getXh());
		stu.setXm(xs.getXm());
		stu.setXb(xs.getXb());
		stu.setZy(xs.getZy());
		stu.setCssj(xs.getCssj());
		stu.setBz(xs.getBz());
		if(db.save(stu)) {
			return "success";
		} else {
			return "error";
		}
	}
}
