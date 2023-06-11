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
import com.chengxusheji.domain.ArticleClass;

@Service @Transactional
public class ArticleClassDAO {

	@Resource SessionFactory factory;
    /*每页显示记录数目*/
    private final int PAGE_SIZE = 10;

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加图书信息*/
    public void AddArticleClass(ArticleClass articleClass) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(articleClass);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ArticleClass> QueryArticleClassInfo(int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From ArticleClass articleClass where 1=1";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List articleClassList = q.list();
    	return (ArrayList<ArticleClass>) articleClassList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ArticleClass> QueryArticleClassInfo() { 
    	Session s = factory.getCurrentSession();
    	String hql = "From ArticleClass articleClass where 1=1";
    	Query q = s.createQuery(hql);
    	List articleClassList = q.list();
    	return (ArrayList<ArticleClass>) articleClassList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<ArticleClass> QueryAllArticleClassInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From ArticleClass";
        Query q = s.createQuery(hql);
        List articleClassList = q.list();
        return (ArrayList<ArticleClass>) articleClassList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber() {
        Session s = factory.getCurrentSession();
        String hql = "From ArticleClass articleClass where 1=1";
        Query q = s.createQuery(hql);
        List articleClassList = q.list();
        recordNumber = articleClassList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArticleClass GetArticleClassByArticleClassId(int articleClassId) {
        Session s = factory.getCurrentSession();
        ArticleClass articleClass = (ArticleClass)s.get(ArticleClass.class, articleClassId);
        return articleClass;
    }

    /*更新ArticleClass信息*/
    public void UpdateArticleClass(ArticleClass articleClass) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(articleClass);
    }

    /*删除ArticleClass信息*/
    public void DeleteArticleClass (int articleClassId) throws Exception {
        Session s = factory.getCurrentSession();
        Object articleClass = s.load(ArticleClass.class, articleClassId);
        s.delete(articleClass);
    }

}
