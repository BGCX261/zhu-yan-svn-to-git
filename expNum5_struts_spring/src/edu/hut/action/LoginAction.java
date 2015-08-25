package edu.hut.action;

public class LoginAction {
	String xh;
	String kl;
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getKl() {
		return kl;
	}
	public void setKl(String kl) {
		this.kl = kl;
	}
	
	public String execute() throws Exception{
		return "success";
	}
}
