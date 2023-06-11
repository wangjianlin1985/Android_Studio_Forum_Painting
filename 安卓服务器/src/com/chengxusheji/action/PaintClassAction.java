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
import com.chengxusheji.dao.PaintClassDAO;
import com.chengxusheji.domain.PaintClass;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class PaintClassAction extends BaseAction {

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

    private int paintClassId;
    public void setPaintClassId(int paintClassId) {
        this.paintClassId = paintClassId;
    }
    public int getPaintClassId() {
        return paintClassId;
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
    @Resource PaintClassDAO paintClassDAO;

    /*待操作的PaintClass对象*/
    private PaintClass paintClass;
    public void setPaintClass(PaintClass paintClass) {
        this.paintClass = paintClass;
    }
    public PaintClass getPaintClass() {
        return this.paintClass;
    }

    /*跳转到添加PaintClass视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*添加PaintClass信息*/
    @SuppressWarnings("deprecation")
    public String AddPaintClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            paintClassDAO.AddPaintClass(paintClass);
            ctx.put("message",  java.net.URLEncoder.encode("PaintClass添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PaintClass添加失败!"));
            return "error";
        }
    }

    /*查询PaintClass信息*/
    public String QueryPaintClass() {
        if(currentPage == 0) currentPage = 1;
        List<PaintClass> paintClassList = paintClassDAO.QueryPaintClassInfo(currentPage);
        /*计算总的页数和总的记录数*/
        paintClassDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = paintClassDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = paintClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("paintClassList",  paintClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*后台导出到excel*/
    public String QueryPaintClassOutputToExcel() { 
        List<PaintClass> paintClassList = paintClassDAO.QueryPaintClassInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "PaintClass信息记录"; 
        String[] headers = { "绘画分类id","绘画分类名称"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<paintClassList.size();i++) {
        	PaintClass paintClass = paintClassList.get(i); 
        	dataset.add(new String[]{paintClass.getPaintClassId() + "",paintClass.getPaintClassName()});
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
			response.setHeader("Content-disposition","attachment; filename="+"PaintClass.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询PaintClass信息*/
    public String FrontQueryPaintClass() {
        if(currentPage == 0) currentPage = 1;
        List<PaintClass> paintClassList = paintClassDAO.QueryPaintClassInfo(currentPage);
        /*计算总的页数和总的记录数*/
        paintClassDAO.CalculateTotalPageAndRecordNumber();
        /*获取到总的页码数目*/
        totalPage = paintClassDAO.getTotalPage();
        /*当前查询条件下总记录数*/
        recordNumber = paintClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("paintClassList",  paintClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*查询要修改的PaintClass信息*/
    public String ModifyPaintClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键paintClassId获取PaintClass对象*/
        PaintClass paintClass = paintClassDAO.GetPaintClassByPaintClassId(paintClassId);

        ctx.put("paintClass",  paintClass);
        return "modify_view";
    }

    /*查询要修改的PaintClass信息*/
    public String FrontShowPaintClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键paintClassId获取PaintClass对象*/
        PaintClass paintClass = paintClassDAO.GetPaintClassByPaintClassId(paintClassId);

        ctx.put("paintClass",  paintClass);
        return "front_show_view";
    }

    /*更新修改PaintClass信息*/
    public String ModifyPaintClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            paintClassDAO.UpdatePaintClass(paintClass);
            ctx.put("message",  java.net.URLEncoder.encode("PaintClass信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PaintClass信息更新失败!"));
            return "error";
       }
   }

    /*删除PaintClass信息*/
    public String DeletePaintClass() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            paintClassDAO.DeletePaintClass(paintClassId);
            ctx.put("message",  java.net.URLEncoder.encode("PaintClass删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PaintClass删除失败!"));
            return "error";
        }
    }

}
