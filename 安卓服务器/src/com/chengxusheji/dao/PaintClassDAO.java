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

@Service @Transactional
public class PaintClassDAO {

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
    public void AddPaintClass(PaintClass paintClass) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(paintClass);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<PaintClass> QueryPaintClassInfo(int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From PaintClass paintClass where 1=1";
    	Query q = s.createQuery(hql);
    	/*���㵱ǰ��ʾҳ��Ŀ�ʼ��¼*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List paintClassList = q.list();
    	return (ArrayList<PaintClass>) paintClassList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<PaintClass> QueryPaintClassInfo() { 
    	Session s = factory.getCurrentSession();
    	String hql = "From PaintClass paintClass where 1=1";
    	Query q = s.createQuery(hql);
    	List paintClassList = q.list();
    	return (ArrayList<PaintClass>) paintClassList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<PaintClass> QueryAllPaintClassInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From PaintClass";
        Query q = s.createQuery(hql);
        List paintClassList = q.list();
        return (ArrayList<PaintClass>) paintClassList;
    }

    /*�����ܵ�ҳ���ͼ�¼��*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber() {
        Session s = factory.getCurrentSession();
        String hql = "From PaintClass paintClass where 1=1";
        Query q = s.createQuery(hql);
        List paintClassList = q.list();
        recordNumber = paintClassList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*����������ȡ����*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public PaintClass GetPaintClassByPaintClassId(int paintClassId) {
        Session s = factory.getCurrentSession();
        PaintClass paintClass = (PaintClass)s.get(PaintClass.class, paintClassId);
        return paintClass;
    }

    /*����PaintClass��Ϣ*/
    public void UpdatePaintClass(PaintClass paintClass) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(paintClass);
    }

    /*ɾ��PaintClass��Ϣ*/
    public void DeletePaintClass (int paintClassId) throws Exception {
        Session s = factory.getCurrentSession();
        Object paintClass = s.load(PaintClass.class, paintClassId);
        s.delete(paintClass);
    }

}
