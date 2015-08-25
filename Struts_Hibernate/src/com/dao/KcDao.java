package com.dao;

import java.util.List;

import com.model.Kcb;

public interface KcDao {
	public Kcb getOneKc(String kch);
	
	public List<Kcb> getAll();
}
