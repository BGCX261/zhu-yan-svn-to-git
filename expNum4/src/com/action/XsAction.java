package com.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dao.XsDao;

import com.dao.ZyDao;
import com.dao.imp.KcDaoImp;
import com.dao.imp.XsDaoImp;
import com.dao.imp.ZyDaoImp;
import com.model.Dlb;
import com.model.Kcb;
import com.model.Xsb;
import com.model.Zyb;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class XsAction extends ActionSupport{
	XsDao xsDao;
	//����ѧ������
	private Xsb xs;
	//����γ���Ϣ
	private Kcb kcb;
	//����רҵ����
	private Zyb zyb;
	public Xsb getXs() {
		return xs;
	}
	public void setXs(Xsb xs) {
		this.xs = xs;
	}
	public Kcb getKcb() {
		return kcb;
	}
	public void setKcb(Kcb kcb) {
		this.kcb = kcb;
	}	
	public Zyb getZyb() {
		return zyb;
	}
	public void setZyb(Zyb zyb) {
		this.zyb = zyb;
	}
	
	public String execute() throws Exception {
		Map<String, Dlb> session = (Map<String, Dlb>) ActionContext
				.getContext().getSession();
		Dlb user = (Dlb) session.get("user");
		xsDao = new XsDaoImp();
		Xsb xs = xsDao.getOneXs(user.getXh());
		Map<String, Xsb> request = (Map<String, Xsb>) ActionContext
				.getContext().get("request");
		request.put("xs", xs);
		return SUCCESS;
	}
	
	public String updateXsInfo() throws Exception {
		Map<String, Dlb> session = ActionContext.getContext().getSession();
		Dlb user = (Dlb) session.get("user");
		xsDao = new XsDaoImp();
		ZyDao zyDao = new ZyDaoImp();
		List<Zyb> zys = zyDao.getAll();
		for(int i=0;i<zys.size();i++)
			System.out.println(zys.get(i).getZym());
		Xsb xs = xsDao.getOneXs(user.getXh());
		Map request = (Map) ActionContext.getContext().get("request");
		request.put("zys", zys);
		request.put("xs", xs);
		return SUCCESS;
	}

	public String updateXs() throws Exception {
		xsDao = new XsDaoImp();
		ZyDao zyDao = new ZyDaoImp();
		Xsb stu = new Xsb();
		stu.setXh(xs.getXh());
		Set list = xsDao.getOneXs(xs.getXh()).getKcs();
		stu.setKcs(list);
		stu.setXm(xs.getXm());
		stu.setXb(xs.getXb());
		stu.setCssj(xs.getCssj());
		stu.setBz(xs.getBz());
		stu.setZxf(xs.getZxf());
		Zyb zy = zyDao.getOneZy(zyb.getId());
		stu.setZyb(zy);
		xsDao.update(stu);
		return SUCCESS;
	}
	
	//�õ�ѧ��ѡ�޵Ŀγ�
	public String getXsKcs() throws Exception{
		Map session=(Map)ActionContext.getContext().getSession();
		Dlb user=(Dlb)session.get("user");
		String xh=user.getXh();
		//�õ���ǰѧ������Ϣ
		Xsb xsb=new XsDaoImp().getOneXs(user.getXh());
		System.out.println("ѧ��ѡ�޵Ŀγ�"+user.getXh());
		//ȡ��ѡ�޵Ŀγ�set
		Set list=xsb.getKcs();
		Map request=(Map)ActionContext.getContext().get("request");
		//����
		request.put("list", list);
		return SUCCESS;		
	}
	
	//��ѡ�γ�
	public String deleteKc() throws Exception {
		Map session=(Map)ActionContext.getContext().getSession();
		String xh=((Dlb)session.get("user")).getXh();
		xsDao=new XsDaoImp();
		Xsb xs2=xsDao.getOneXs(xh);
		Set list=xs2.getKcs();
		Iterator iter=list.iterator();
		//ȡ������ѡ��Ŀγ̽��е���
		while(iter.hasNext()){
			Kcb kc2=(Kcb)iter.next();
			//�����������ѡ�Ŀγ̵Ŀγ̺žʹ�list��ɾ��
			if(kc2.getKch().equals(kcb.getKch())){
				iter.remove();
			}
		}
		//���ÿγ̵�set
		xs2.setKcs(list);
		xsDao.update(xs2);
		return SUCCESS;	
	}
	
	//ѡ���γ�
	public String selectKc() throws Exception {
		Map session=(Map)ActionContext.getContext().getSession();
		String xh=((Dlb)session.get("user")).getXh();
		xsDao=new XsDaoImp();
		Xsb xs3=xsDao.getOneXs(xh);
		Set list=xs3.getKcs();
		Iterator iter=list.iterator();
		//ѡ�޿γ�ʱ�ȱ����Ѿ�ѡ�Ŀγ̣�������Ѿ�ѡ�Ŀγ����ҵ��ͷ���ERROR
		while(iter.hasNext()){
			Kcb kc3=(Kcb)iter.next();
			//�����������ѡ�Ŀγ̵Ŀγ̺žʹ�list��ɾ��
			if(kc3.getKch().equals(kcb.getKch())){
				return ERROR;
			}
		}
		//���û���ҵ�������ӵ�������
		list.add(new KcDaoImp().getoneKc(kcb.getKch()));
		xs3.setKcs(list);
		xsDao.update(xs3);
		return SUCCESS;	
	}
}



