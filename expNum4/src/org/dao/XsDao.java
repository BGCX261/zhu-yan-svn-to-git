package org.dao;

import com.model.Xsb;

public interface XsDao {
	//����ѧ�Ų�����Ϣ
	public Xsb getOneXs(String xh);
	//�޸�ѧ����Ϣ
	public void update(Xsb xs);
}
