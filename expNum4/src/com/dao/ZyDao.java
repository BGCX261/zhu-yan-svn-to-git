package com.dao;

import java.util.List;

import com.model.Zyb;

public interface ZyDao {
	public Zyb getOneZy(int zyId);
	
	public List<Zyb> getAll();
	
	
}
