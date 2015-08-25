package com.model;

/**
 * Dlb entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Dlb implements java.io.Serializable {

	// Fields

	private Integer id;
	private String xh;
	private String kl;

	// Constructors

	/** default constructor */
	public Dlb() {
	}

	/** minimal constructor */
	public Dlb(Integer id, String xh) {
		this.id = id;
		this.xh = xh;
	}

	/** full constructor */
	public Dlb(Integer id, String xh, String kl) {
		this.id = id;
		this.xh = xh;
		this.kl = kl;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getXh() {
		return this.xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getKl() {
		return this.kl;
	}

	public void setKl(String kl) {
		this.kl = kl;
	}

}