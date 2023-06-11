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

    /*��ǰ�ڼ�ҳ*/
    private int currentPage;
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }

    /*һ������ҳ*/
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

    /*��ǰ��ѯ���ܼ�¼��Ŀ*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*ҵ������*/
    @Resource PaintClassDAO paintClassDAO;

    /*��������PaintClass����*/
    private PaintClass paintClass;
    public void setPaintClass(PaintClass paintClass) {
        this.paintClass = paintClass;
    }
    public PaintClass getPaintClass() {
        return this.paintClass;
    }

    /*��ת�����PaintClass��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        return "add_view";
    }

    /*���PaintClass��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddPaintClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            paintClassDAO.AddPaintClass(paintClass);
            ctx.put("message",  java.net.URLEncoder.encode("PaintClass��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PaintClass���ʧ��!"));
            return "error";
        }
    }

    /*��ѯPaintClass��Ϣ*/
    public String QueryPaintClass() {
        if(currentPage == 0) currentPage = 1;
        List<PaintClass> paintClassList = paintClassDAO.QueryPaintClassInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        paintClassDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = paintClassDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = paintClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("paintClassList",  paintClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryPaintClassOutputToExcel() { 
        List<PaintClass> paintClassList = paintClassDAO.QueryPaintClassInfo();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "PaintClass��Ϣ��¼"; 
        String[] headers = { "�滭����id","�滭��������"};
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
		HttpServletResponse response = null;//����һ��HttpServletResponse���� 
		OutputStream out = null;//����һ����������� 
		try { 
			response = ServletActionContext.getResponse();//��ʼ��HttpServletResponse���� 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"PaintClass.xls");//filename�����ص�xls���������������Ӣ�� 
			response.setContentType("application/msexcel;charset=UTF-8");//�������� 
			response.setHeader("Pragma","No-cache");//����ͷ 
			response.setHeader("Cache-Control","no-cache");//����ͷ 
			response.setDateHeader("Expires", 0);//��������ͷ  
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
    /*ǰ̨��ѯPaintClass��Ϣ*/
    public String FrontQueryPaintClass() {
        if(currentPage == 0) currentPage = 1;
        List<PaintClass> paintClassList = paintClassDAO.QueryPaintClassInfo(currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        paintClassDAO.CalculateTotalPageAndRecordNumber();
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = paintClassDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = paintClassDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("paintClassList",  paintClassList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�PaintClass��Ϣ*/
    public String ModifyPaintClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������paintClassId��ȡPaintClass����*/
        PaintClass paintClass = paintClassDAO.GetPaintClassByPaintClassId(paintClassId);

        ctx.put("paintClass",  paintClass);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�PaintClass��Ϣ*/
    public String FrontShowPaintClassQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������paintClassId��ȡPaintClass����*/
        PaintClass paintClass = paintClassDAO.GetPaintClassByPaintClassId(paintClassId);

        ctx.put("paintClass",  paintClass);
        return "front_show_view";
    }

    /*�����޸�PaintClass��Ϣ*/
    public String ModifyPaintClass() {
        ActionContext ctx = ActionContext.getContext();
        try {
            paintClassDAO.UpdatePaintClass(paintClass);
            ctx.put("message",  java.net.URLEncoder.encode("PaintClass��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PaintClass��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��PaintClass��Ϣ*/
    public String DeletePaintClass() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            paintClassDAO.DeletePaintClass(paintClassId);
            ctx.put("message",  java.net.URLEncoder.encode("PaintClassɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("PaintClassɾ��ʧ��!"));
            return "error";
        }
    }

}
