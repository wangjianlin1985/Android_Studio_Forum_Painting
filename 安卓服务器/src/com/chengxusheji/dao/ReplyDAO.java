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
import com.chengxusheji.domain.PostInfo;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.domain.Reply;

@Service @Transactional
public class ReplyDAO {

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
    public void AddReply(Reply reply) throws Exception {
    	Session s = factory.getCurrentSession();
    	s.merge(reply);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Reply> QueryReplyInfo(PostInfo postInfoObj,UserInfo userObj,String replyTime,int currentPage) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Reply reply where 1=1";
    	if(null != postInfoObj && postInfoObj.getPostInfoId()!=0) hql += " and reply.postInfoObj.postInfoId=" + postInfoObj.getPostInfoId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and reply.userObj.user_name='" + userObj.getUser_name() + "'";
    	if(!replyTime.equals("")) hql = hql + " and reply.replyTime like '%" + replyTime + "%'";
    	Query q = s.createQuery(hql);
    	/*计算当前显示页码的开始记录*/
    	int startIndex = (currentPage-1) * this.PAGE_SIZE;
    	q.setFirstResult(startIndex);
    	q.setMaxResults(this.PAGE_SIZE);
    	List replyList = q.list();
    	return (ArrayList<Reply>) replyList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Reply> QueryReplyInfo(PostInfo postInfoObj,UserInfo userObj,String replyTime) { 
    	Session s = factory.getCurrentSession();
    	String hql = "From Reply reply where 1=1";
    	if(null != postInfoObj && postInfoObj.getPostInfoId()!=0) hql += " and reply.postInfoObj.postInfoId=" + postInfoObj.getPostInfoId();
    	if(null != userObj && !userObj.getUser_name().equals("")) hql += " and reply.userObj.user_name='" + userObj.getUser_name() + "'";
    	if(!replyTime.equals("")) hql = hql + " and reply.replyTime like '%" + replyTime + "%'";
    	Query q = s.createQuery(hql);
    	List replyList = q.list();
    	return (ArrayList<Reply>) replyList;
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public ArrayList<Reply> QueryAllReplyInfo() {
        Session s = factory.getCurrentSession(); 
        String hql = "From Reply";
        Query q = s.createQuery(hql);
        List replyList = q.list();
        return (ArrayList<Reply>) replyList;
    }

    /*计算总的页数和记录数*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void CalculateTotalPageAndRecordNumber(PostInfo postInfoObj,UserInfo userObj,String replyTime) {
        Session s = factory.getCurrentSession();
        String hql = "From Reply reply where 1=1";
        if(null != postInfoObj && postInfoObj.getPostInfoId()!=0) hql += " and reply.postInfoObj.postInfoId=" + postInfoObj.getPostInfoId();
        if(null != userObj && !userObj.getUser_name().equals("")) hql += " and reply.userObj.user_name='" + userObj.getUser_name() + "'";
        if(!replyTime.equals("")) hql = hql + " and reply.replyTime like '%" + replyTime + "%'";
        Query q = s.createQuery(hql);
        List replyList = q.list();
        recordNumber = replyList.size();
        int mod = recordNumber % this.PAGE_SIZE;
        totalPage = recordNumber / this.PAGE_SIZE;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取对象*/
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Reply GetReplyByReplyId(int replyId) {
        Session s = factory.getCurrentSession();
        Reply reply = (Reply)s.get(Reply.class, replyId);
        return reply;
    }

    /*更新Reply信息*/
    public void UpdateReply(Reply reply) throws Exception {
        Session s = factory.getCurrentSession();
        s.merge(reply);
    }

    /*删除Reply信息*/
    public void DeleteReply (int replyId) throws Exception {
        Session s = factory.getCurrentSession();
        Object reply = s.load(Reply.class, replyId);
        s.delete(reply);
    }

}
