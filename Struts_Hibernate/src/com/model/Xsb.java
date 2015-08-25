package com.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Xsb entity. @author MyEclipse Persistence Tools
 */

public class Xsb implements java.io.Serializable {

	// Fields

	private String xh;
	private String xm;
	private Integer xb;
	private Timestamp cssj;
	private Integer zxf;
	private String bz;
	private String zp;
	private Zyb zyb;
	public Zyb getZyb() {
		return zyb;
	}

	public void setZyb(Zyb zyb) {
		this.zyb = zyb;
	}

	private Set kcs = new HashSet();

	// Constructors

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
	public Xsb(String xh) {
		this.xh = xh;
	}

	/** full constructor */
	public Xsb(String xh, String xm, Integer xb, Timestamp cssj,
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

	public Integer getXb() {
		return this.xb;
	}

	public void setXb(Integer xb) {
		this.xb = xb;
	}

	public Timestamp getCssj() {
		return this.cssj;
	}

	public void setCssj(Timestamp cssj) {
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

}