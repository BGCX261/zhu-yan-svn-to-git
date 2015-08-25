package com.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Xsb entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Xsb implements java.io.Serializable {

	// Fields

	private String xh;
	private String xm;
	private int xb;
	private Date cssj;
	private Integer zxf;
	private String bz;
	private String zp;
	private Set kcs=new HashSet();
	private Zyb zyb;

	// Constructors

	public Zyb getZyb() {
		return zyb;
	}

	public void setZyb(Zyb zyb) {
		this.zyb = zyb;
	}

	public Set getKcs() {
		return kcs;
	}

	public void setKcs(Set kcs) {
		this.kcs = kcs;
	}

	/** default constructor */
	public Xsb() {
	}

	/** minimal constructor */
	public Xsb(String xh, String xm, Byte xb) {
		this.xh = xh;
		this.xm = xm;
		this.xb = xb;
	}

	/** full constructor */
	public Xsb(String xh, String xm, Byte xb, Date cssj,
			Integer zxf, String bz, String zp) {
		this.xh = xh;
		this.xm = xm;
		this.xb = xb;
		this.cssj = cssj;
		this.zxf = zxf;
		this.bz = bz;
		this.zp = zp;
	}

	// Property accessors

	public String getXh() {
		return this.xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getXm() {
		return this.xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	
	public Date getCssj() {
		return this.cssj;
	}

	public void setCssj(Date cssj) {
		this.cssj = cssj;
	}


	public Integer getZxf() {
		return this.zxf;
	}

	public void setZxf(Integer zxf) {
		this.zxf = zxf;
	}

	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getZp() {
		return this.zp;
	}

	public void setZp(String zp) {
		this.zp = zp;
	}

	public int getXb() {
		return xb;
	}

	public void setXb(int xb) {
		this.xb = xb;
	}

}