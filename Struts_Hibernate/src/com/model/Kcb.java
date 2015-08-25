package com.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Kcb entity. @author MyEclipse Persistence Tools
 */

public class Kcb implements java.io.Serializable {

	// Fields

	private String kch;
	private String kcm;
	private Integer kxxq;
	private Integer xs;
	private Integer xf;
	private Set xss = new HashSet();
 
	// Constructors

	public Set getXss() {
		return xss;
	}

	public void setXss(Set xss) {
		this.xss = xss;
	}

	/** default constructor */
	public Kcb() {
	}

	/** minimal constructor */
	public Kcb(String kch) {
		this.kch = kch;
	}

	/** full constructor */
	public Kcb(String kch, String kcm, Integer kxxq, Integer xs, Integer xf) {
		this.kch = kch;
		this.kcm = kcm;
		this.kxxq = kxxq;
		this.xs = xs;
		this.xf = xf;
	}

	// Property accessors

	public String getKch() {
		return this.kch;
	}

	public void setKch(String kch) {
		this.kch = kch;
	}

	public String getKcm() {
		return this.kcm;
	}

	public void setKcm(String kcm) {
		this.kcm = kcm;
	}

	public Integer getKxxq() {
		return this.kxxq;
	}

	public void setKxxq(Integer kxxq) {
		this.kxxq = kxxq;
	}

	public Integer getXs() {
		return this.xs;
	}

	public void setXs(Integer xs) {
		this.xs = xs;
	}

	public Integer getXf() {
		return this.xf;
	}

	public void setXf(Integer xf) {
		this.xf = xf;
	}

}