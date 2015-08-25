package org.dao;

import java.util.List;


import com.model.Kcb;

public interface KcDao {
	//根据课程号查找信息
	public Kcb getoneKc(String kch);
	//查找所有课程信息
	public List getAll(); 
}
