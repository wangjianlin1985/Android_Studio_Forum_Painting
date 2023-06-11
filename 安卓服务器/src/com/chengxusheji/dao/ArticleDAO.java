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
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.domain.Article;

@Service @Transactional
public class ArticleDAO {

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
    public void AddArticle(Article article) throws Exception {
    	Session s = factory.getCurrentSession();
     s.save(article);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Article> QueryArticleInfo(String title,ArticleClass articleClassObj,UserInfo userObj,String addTime,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Article article where 1=1";
    	if(!title.equals("")) hql = hql + " and article.title like '%" + title + "%'";
    	if(null != articleClassObj && articleClassObj.getArticleClassId()!=0) hql += " and article.articleClassObj.articleClassId=" + articleClassObj.getArticleClassId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and article.userObj.user_name='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) hql = hql + " and article.addTime like '%" + addTime + "%'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List articleList = q.list();
    	return (ArrayList<Article>) articleList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Article> QueryArticleInfo(String title,ArticleClass articleClassObj,UserInfo userObj,String addTime) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Article article where 1=1";
    	if(!title.equals("")) hql = hql + " and article.title like '%" + title + "%'";
    	if(null != articleClassObj && articleClassObj.getArticleClassId()!=0) hql += " and article.articleClassObj.articleClassId=" + articleClassObj.getArticleClassId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and article.userObj.user_name='" + userObj.getUser_name() + "'";
    	if(!addTime.equals("")) hql = hql + " and article.addTime like '%" + addTime + "%'";
    	Query q = s.createQuery(hql);
    	List articleList = q.list();
    	return (ArrayList<Article>) articleList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Article> QueryAllArticleInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Article";
        Query q = s.createQuery(hql);
        List articleList = q.list();
        return (ArrayList<Article>) articleList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(String title,ArticleClass articleClassObj,UserInfo userObj,String addTime) {
        Session s = factory.getCurrentSession();
        String hql = "From Article article where 1=1";
        if(!title.equals("")) hql = hql + " and article.title like '%" + title + "%'";
        if(null != articleClassObj && articleClassObj.getArticleClassId()!=0) hql += " and article.articleClassObj.articleClassId=" + articleClassObj.getArticleClassId();
        if(null != userObj && !userObj.getUser_name().equals("")) hql += " and article.userObj.user_name='" + userObj.getUser_name() + "'";
        if(!addTime.equals("")) hql = hql + " and article.addTime like '%" + addTime + "%'";
        Query q = s.createQuery(hql);
        List articleList = q.list();
        recordNumber = articleList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Article GetArticleByArticleId(int articleId) {
        Session s = factory.getCurrentSession();
        Article article = (Article)s.get(Article.class, articleId);
        return article;
    }

    /*更新Article信息*/
    public void UpdateArticle(Article article) throws Exception {
        Session s = factory.getCurrentSession();
        s.update(article);
    }

    /*删除Article信息*/
    public void DeleteArticle (int articleId) throws Exception {
        Session s = factory.getCurrentSession();
        Object article = s.load(Article.class, articleId);
        s.delete(article);
    }

}
