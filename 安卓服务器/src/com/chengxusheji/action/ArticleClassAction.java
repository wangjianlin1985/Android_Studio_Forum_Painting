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
import com.chengxusheji.dao.ArticleClassDAO;
import com.chengxusheji.domain.ArticleClass;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class ArticleClassAction extends BaseAction {

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

    private int articleClassId;
    public void setArticleClassId(int articleClassId) {
        this.articleClassId = articleClassId;
    }
    public int getArticleClassId() {
        return articleClassId;
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

    /*待操作的ArticleClass对象*/
    private ArticleClass articleClass;
    public void setArticleClass(ArticleClass articleClass) {
        this.articleClass = articleClass;
    }
    public ArticleClass getArticleClass() {
        return this.articleClass;
    }

    /*跳转到添加ArticleClass视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*添加ArticleClass信息*/
    @SuppressWarnings("deprecation")
    public String AddArticleClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            articleClassDAO.AddArticleClass(articleClass);
            ctx.put("message",  java.net.URLEncoder.encode("ArticleClass添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ArticleClass添加失败!"));
            return "error";
        }
    }

    /*查询ArticleClass信息*/
    public String QueryArticleClass() {
        if(currentPage == 0) currentPage = 1;
        List<ArticleClass> articleClassList = articleClassDAO.QueryArticleClassInfo(currentPage);
        /*计算总的页数和总的记录数*/
        articleClassDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = articleClassDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = articleClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("articleClassList",  articleClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryArticleClassOutputToExcel() { 
        List<ArticleClass> articleClassList = articleClassDAO.QueryArticleClassInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "ArticleClass信息记录"; 
        String[] headers = { "文章分类id","文章分类名称"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<articleClassList.size();i++) {
        	ArticleClass articleClass = articleClassList.get(i); 
        	dataset.add(new String[]{articleClass.getArticleClassId() + "",articleClass.getArticleClassName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"ArticleClass.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询ArticleClass信息*/
    public String FrontQueryArticleClass() {
        if(currentPage == 0) currentPage = 1;
        List<ArticleClass> articleClassList = articleClassDAO.QueryArticleClassInfo(currentPage);
        /*计算总的页数和总的记录数*/
        articleClassDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = articleClassDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = articleClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("articleClassList",  articleClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*查询要修改的ArticleClass信息*/
    public String ModifyArticleClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键articleClassId获取ArticleClass对象*/
        ArticleClass articleClass = articleClassDAO.GetArticleClassByArticleClassId(articleClassId);

        ctx.put("articleClass",  articleClass);
        return "modify_view";
    }

    /*查询要修改的ArticleClass信息*/
    public String FrontShowArticleClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键articleClassId获取ArticleClass对象*/
        ArticleClass articleClass = articleClassDAO.GetArticleClassByArticleClassId(articleClassId);

        ctx.put("articleClass",  articleClass);
        return "front_show_view";
    }

    /*更新修改ArticleClass信息*/
    public String ModifyArticleClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            articleClassDAO.UpdateArticleClass(articleClass);
            ctx.put("message",  java.net.URLEncoder.encode("ArticleClass信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ArticleClass信息更新失败!"));
            return "error";
       }
   }

    /*删除ArticleClass信息*/
    public String DeleteArticleClass() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            articleClassDAO.DeleteArticleClass(articleClassId);
            ctx.put("message",  java.net.URLEncoder.encode("ArticleClass删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("ArticleClass删除失败!"));
            return "error";
        }
    }

}
