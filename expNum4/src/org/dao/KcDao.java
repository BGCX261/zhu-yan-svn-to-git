package org.dao;

import java.util.List;


import com.model.Kcb;

public interface KcDao {
	//���ݿγ̺Ų�����Ϣ
	public Kcb getoneKc(String kch);
	//�������пγ���Ϣ
	public List getAll(); 
}
