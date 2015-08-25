package org.dao;

import com.model.Xsb;

public interface XsDao {
	//根据学号查找信息
	public Xsb getOneXs(String xh);
	//修改学生信息
	public void update(Xsb xs);
}
