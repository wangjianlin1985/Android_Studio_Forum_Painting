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
import com.chengxusheji.dao.ArticleDAO;
import com.chengxusheji.domain.Article;
import com.chengxusheji.dao.ArticleClassDAO;
import com.chengxusheji.domain.ArticleClass;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class ArticleAction extends BaseAction {

    /*界面层需要查询的属性: 标题*/
    private String title;
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }

    /*界面层需要查询的属性: 文章分类*/
    private ArticleClass articleClassObj;
    public void setArticleClassObj(ArticleClass articleClassObj) {
        this.articleClassObj = articleClassObj;
    }
    public ArticleClass getArticleClassObj() {
        return this.articleClassObj;
    }

    /*界面层需要查询的属性: 发布用户*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
    }

    /*界面层需要查询的属性: 发布时间*/
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

    private int articleId;
    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
    public int getArticleId() {
        return articleId;
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
    @Resource ArticleClassDAO articleClassDAO;
    @Resource UserInfoDAO userInfoDAO;
    @Resource ArticleDAO articleDAO;

    /*待操作的Article对象*/
    private Article article;
    public void setArticle(Article article) {
        this.article = article;
    }
    public Article getArticle() {
        return this.article;
    }

    /*跳转到添加Article视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的ArticleClass信息*/
        List<ArticleClass> articleClassList = articleClassDAO.QueryAllArticleClassInfo();
        ctx.put("articleClassList", articleClassList);
        /*查询所有的UserInfo信息*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*添加Article信息*/
    @SuppressWarnings("deprecation")
    public String AddArticle() {
        ActionContext ctx = ActionContext.getContext();
        try {
            ArticleClass articleClassObj = articleClassDAO.GetArticleClassByArticleClassId(article.getArticleClassObj().getArticleClassId());
            article.setArticleClassObj(articleClassObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(article.getUserObj().getUser_name());
            article.setUserObj(userObj);
            articleDAO.AddArticle(article);
            ctx.put("message",  java.net.URLEncoder.encode("Article添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Article添加失败!"));
            return "error";
        }
    }

    /*查询Article信息*/
    public String QueryArticle() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        List<Article> articleList = articleDAO.QueryArticleInfo(title, articleClassObj, userObj, addTime, currentPage);
        /*计算总的页数和总的记录数*/
        articleDAO.CalculateTotalPageAndRecordNumber(title, articleClassObj, userObj, addTime);
        /*获取到总的页码数目*/
        totalPage = articleDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = articleDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("articleList",  articleList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("title", title);
        ctx.put("articleClassObj", articleClassObj);
        List<ArticleClass> articleClassList = articleClassDAO.QueryAllArticleClassInfo();
        ctx.put("articleClassList", articleClassList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("addTime", addTime);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryArticleOutputToExcel() { 
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        List<Article> articleList = articleDAO.QueryArticleInfo(title,articleClassObj,userObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Article信息记录"; 
        String[] headers = { "文章id","标题","文章分类","点击率","发布用户","发布时间"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<articleList.size();i++) {
        	Article article = articleList.get(i); 
        	dataset.add(new String[]{article.getArticleId() + "",article.getTitle(),article.getArticleClassObj().getArticleClassName(),
article.getHitNum() + "",article.getUserObj().getName(),
article.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Article.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询Article信息*/
    public String FrontQueryArticle() {
        if(currentPage == 0) currentPage = 1;
        if(title == null) title = "";
        if(addTime == null) addTime = "";
        List<Article> articleList = articleDAO.QueryArticleInfo(title, articleClassObj, userObj, addTime, currentPage);
        /*计算总的页数和总的记录数*/
        articleDAO.CalculateTotalPageAndRecordNumber(title, articleClassObj, userObj, addTime);
        /*获取到总的页码数目*/
        totalPage = articleDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = articleDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("articleList",  articleList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("title", title);
        ctx.put("articleClassObj", articleClassObj);
        List<ArticleClass> articleClassList = articleClassDAO.QueryAllArticleClassInfo();
        ctx.put("articleClassList", articleClassList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("addTime", addTime);
        return "front_query_view";
    }

    /*查询要修改的Article信息*/
    public String ModifyArticleQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键articleId获取Article对象*/
        Article article = articleDAO.GetArticleByArticleId(articleId);

        List<ArticleClass> articleClassList = articleClassDAO.QueryAllArticleClassInfo();
        ctx.put("articleClassList", articleClassList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("article",  article);
        return "modify_view";
    }

    /*查询要修改的Article信息*/
    public String FrontShowArticleQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键articleId获取Article对象*/
        Article article = articleDAO.GetArticleByArticleId(articleId);

        List<ArticleClass> articleClassList = articleClassDAO.QueryAllArticleClassInfo();
        ctx.put("articleClassList", articleClassList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("article",  article);
        return "front_show_view";
    }

    /*更新修改Article信息*/
    public String ModifyArticle() {
        ActionContext ctx = ActionContext.getContext();
        try {
            ArticleClass articleClassObj = articleClassDAO.GetArticleClassByArticleClassId(article.getArticleClassObj().getArticleClassId());
            article.setArticleClassObj(articleClassObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(article.getUserObj().getUser_name());
            article.setUserObj(userObj);
            articleDAO.UpdateArticle(article);
            ctx.put("message",  java.net.URLEncoder.encode("Article信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Article信息更新失败!"));
            return "error";
       }
   }

    /*删除Article信息*/
    public String DeleteArticle() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            articleDAO.DeleteArticle(articleId);
            ctx.put("message",  java.net.URLEncoder.encode("Article删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Article删除失败!"));
            return "error";
        }
    }

}
