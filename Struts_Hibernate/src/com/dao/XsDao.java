package com.dao;

import com.model.Xsb;

public interface XsDao {
	public Xsb getOneXs(String xh);
	
	public void update(Xsb xs);
}
