package com.chengxusheji.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import com.opensymphony.xwork2.ActionContext;
import com.chengxusheji.dao.PostInfoDAO;
import com.chengxusheji.domain.PostInfo;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class PostInfoAction extends BaseAction {

    /*界面层需要查询的属性: 帖子标题*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*界面层需要查询的属性: 发帖人*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
    }

    /*界面层需要查询的属性: 发帖时间*/
    private String addTime;
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
    public String getAddTime() {
        return this.addTime;
    }

    /*当前第几页*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*一共多少页*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    private int postInfoId;
    public void setPostInfoId(int postInfoId) {
        this.postInfoId = postInfoId;
    }
    public int getPostInfoId() {
        return postInfoId;
    }

    /*当前查询的总记录数目*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*业务层对象*/
    @Resource UserInfoDAO userInfoDAO;
    @Resource PostInfoDAO postInfoDAO;

    /*待操作的PostInfo对象*/
    private PostInfo postInfo;
    public void setPostInfo(PostInfo postInfo) {
        this.postInfo = postInfo;
    }
    public PostInfo getPostInfo() {
        return this.postInfo;
    }

    /*跳转到添加PostInfo视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的UserInfo信息*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*添加PostInfo信息*/
    @SuppressWarnings("deprecation")
    public String AddPostInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(postInfo.getUserObj().getUser_name());
            postInfo.setUserObj(userObj);
            postInfoDAO.AddPostInfo(postInfo);
            ctx.put("message",  java.net.URLEncoder.encode("PostInfo添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PostInfo添加失败!"));
            return "error";
        }
    }

    /*查询PostInfo信息*/
    public String QueryPostInfo() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        List<PostInfo> postInfoList = postInfoDAO.QueryPostInfoInfo(title, userObj, addTime, currentPage);
        /*计算总的页数和总的记录数*/
        postInfoDAO.CalculateTotalPageAndRecordNumber(title, userObj, addTime);
        /*获取到总的页码数目*/
        totalPage = postInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = postInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("postInfoList",  postInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("title", title);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("addTime", addTime);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryPostInfoOutputToExcel() { 
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        List<PostInfo> postInfoList = postInfoDAO.QueryPostInfoInfo(title,userObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "PostInfo信息记录"; 
        String[] headers = { "帖子id","帖子标题","浏览量","发帖人","发帖时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<postInfoList.size();i++) {
        	PostInfo postInfo = postInfoList.get(i); 
        	dataset.add(new String[]{postInfo.getPostInfoId() + "",postInfo.getTitle(),postInfo.getHitNum() + "",postInfo.getUserObj().getName(),
postInfo.getAddTime()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"PostInfo.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = ServletActionContext.getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
		return null;
    }
    /*前台查询PostInfo信息*/
    public String FrontQueryPostInfo() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        List<PostInfo> postInfoList = postInfoDAO.QueryPostInfoInfo(title, userObj, addTime, currentPage);
        /*计算总的页数和总的记录数*/
        postInfoDAO.CalculateTotalPageAndRecordNumber(title, userObj, addTime);
        /*获取到总的页码数目*/
        totalPage = postInfoDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = postInfoDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("postInfoList",  postInfoList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("title", title);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("addTime", addTime);
        return "front_query_view";
    }

    /*查询要修改的PostInfo信息*/
    public String ModifyPostInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键postInfoId获取PostInfo对象*/
        PostInfo postInfo = postInfoDAO.GetPostInfoByPostInfoId(postInfoId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("postInfo",  postInfo);
        return "modify_view";
    }

    /*查询要修改的PostInfo信息*/
    public String FrontShowPostInfoQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键postInfoId获取PostInfo对象*/
        PostInfo postInfo = postInfoDAO.GetPostInfoByPostInfoId(postInfoId);

        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("postInfo",  postInfo);
        return "front_show_view";
    }

    /*更新修改PostInfo信息*/
    public String ModifyPostInfo() {
        ActionContext ctx = ActionContext.getContext();
        try {
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(postInfo.getUserObj().getUser_name());
            postInfo.setUserObj(userObj);
            postInfoDAO.UpdatePostInfo(postInfo);
            ctx.put("message",  java.net.URLEncoder.encode("PostInfo信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PostInfo信息更新失败!"));
            return "error";
       }
   }

    /*删除PostInfo信息*/
    public String DeletePostInfo() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            postInfoDAO.DeletePostInfo(postInfoId);
            ctx.put("message",  java.net.URLEncoder.encode("PostInfo删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PostInfo删除失败!"));
            return "error";
        }
    }

}
