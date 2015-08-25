package org.action;

import org.model.Xsb;
import org.util.DBConn;

import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport{
	private Xsb xs;

	public Xsb getXs() {
		return xs;
	}

	public void setXs(Xsb xs) {
		this.xs = xs;
	}
	public String execute() throws Exception{
		DBConn dbConn=new DBConn();
		Xsb stu=new Xsb();
		stu.setXh(xs.getXh());
		stu.setXm(xs.getXm());
		stu.setXb(xs.getXb());
		stu.setZy(xs.getZy());
		stu.setCssj(xs.getCssj());
		stu.setBz(xs.getBz());
		if(dbConn.save(stu)){
			return SUCCESS;
		}else {
			return ERROR;
		}
	}
}
