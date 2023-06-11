package com.chengxusheji.dao;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.chengxusheji.domain.PaintClass;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.domain.Paint;

@Service @Transactional
public class PaintDAO {

	@Resource SessionFactory factory;
    /*ÿҳ��ʾ��¼��Ŀ*/
    private final int PAGE_SIZE = 10;

    /*�����ѯ���ܵ�ҳ��*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*�����ѯ�����ܼ�¼��*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*���ͼ����Ϣ*/
    public void AddPaint(Paint paint) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(paint);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Paint> QueryPaintInfo(String paintName,PaintClass paintClassObj,UserInfo userObj,String addTime,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Paint paint where 1=1";
    	if(!paintName.equals("")) hql = hql + " and paint.paintName like '%" + paintName + "%'";
    	if(null != paintClassObj && paintClassObj.getPaintClassId()!=0) hql += " and paint.paintClassObj.paintClassId=" + paintClassObj.getPaintClassId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and paint.userObj.user_name='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) hql = hql + " and paint.addTime like '%" + addTime + "%'";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List paintList = q.list();
    	return (ArrayList<Paint>) paintList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Paint> QueryPaintInfo(String paintName,PaintClass paintClassObj,UserInfo userObj,String addTime) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Paint paint where 1=1";
    	if(!paintName.equals("")) hql = hql + " and paint.paintName like '%" + paintName + "%'";
    	if(null != paintClassObj && paintClassObj.getPaintClassId()!=0) hql += " and paint.paintClassObj.paintClassId=" + paintClassObj.getPaintClassId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and paint.userObj.user_name='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) hql = hql + " and paint.addTime like '%" + addTime + "%'";
    	Query q = s.createQuery(hql);
    	List paintList = q.list();
    	return (ArrayList<Paint>) paintList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Paint> QueryAllPaintInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Paint";
        Query q = s.createQuery(hql);
        List paintList = q.list();
        return (ArrayList<Paint>) paintList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String paintName,PaintClass paintClassObj,UserInfo userObj,String addTime) {
        Session s = factory.getCurrentSession();
        String hql = "From Paint paint where 1=1";
        if(!paintName.equals("")) hql = hql + " and paint.paintName like '%" + paintName + "%'";
        if(null != paintClassObj && paintClassObj.getPaintClassId()!=0) hql += " and paint.paintClassObj.paintClassId=" + paintClassObj.getPaintClassId();
        if(null != userObj && !userObj.getUser_name().equals("")) hql += " and paint.userObj.user_name='" + userObj.getUser_name() + "'";
        if(!addTime.equals("")) hql = hql + " and paint.addTime like '%" + addTime + "%'";
        Query q = s.createQuery(hql);
        List paintList = q.list();
        recordNumber = paintList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Paint GetPaintByPaintId(int paintId) {
        Session s = factory.getCurrentSession();
        Paint paint = (Paint)s.get(Paint.class, paintId);
        return paint;
    }

    /*����Paint��Ϣ*/
    public void UpdatePaint(Paint paint) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(paint);
    }

    /*ɾ��Paint��Ϣ*/
    public void DeletePaint (int paintId) throws Exception {
        Session s = factory.getCurrentSession();
        Object paint = s.load(Paint.class, paintId);
        s.delete(paint);
    }

}
