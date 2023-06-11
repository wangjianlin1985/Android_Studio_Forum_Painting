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
import com.chengxusheji.dao.PaintDAO;
import com.chengxusheji.domain.Paint;
import com.chengxusheji.dao.PaintClassDAO;
import com.chengxusheji.domain.PaintClass;
import com.chengxusheji.dao.UserInfoDAO;
import com.chengxusheji.domain.UserInfo;
import com.chengxusheji.utils.FileTypeException;
import com.chengxusheji.utils.ExportExcelUtil;

@Controller @Scope("prototype")
public class PaintAction extends BaseAction {

	/*ͼƬ���ļ��ֶ�paintPhoto��������*/
	private File paintPhotoFile;
	private String paintPhotoFileFileName;
	private String paintPhotoFileContentType;
	public File getPaintPhotoFile() {
		return paintPhotoFile;
	}
	public void setPaintPhotoFile(File paintPhotoFile) {
		this.paintPhotoFile = paintPhotoFile;
	}
	public String getPaintPhotoFileFileName() {
		return paintPhotoFileFileName;
	}
	public void setPaintPhotoFileFileName(String paintPhotoFileFileName) {
		this.paintPhotoFileFileName = paintPhotoFileFileName;
	}
	public String getPaintPhotoFileContentType() {
		return paintPhotoFileContentType;
	}
	public void setPaintPhotoFileContentType(String paintPhotoFileContentType) {
		this.paintPhotoFileContentType = paintPhotoFileContentType;
	}
	/*ͼƬ���ļ��ֶ�paintFile��������*/
	private File paintFileFile;
	private String paintFileFileFileName;
	private String paintFileFileContentType;
	public File getPaintFileFile() {
		return paintFileFile;
	}
	public void setPaintFileFile(File paintFileFile) {
		this.paintFileFile = paintFileFile;
	}
	public String getPaintFileFileFileName() {
		return paintFileFileFileName;
	}
	public void setPaintFileFileFileName(String paintFileFileFileName) {
		this.paintFileFileFileName = paintFileFileFileName;
	}
	public String getPaintFileFileContentType() {
		return paintFileFileContentType;
	}
	public void setPaintFileFileContentType(String paintFileFileContentType) {
		this.paintFileFileContentType = paintFileFileContentType;
	}
    /*�������Ҫ��ѯ������: ��Ʒ����*/
    private String paintName;
    public void setPaintName(String paintName) {
        this.paintName = paintName;
    }
    public String getPaintName() {
        return this.paintName;
    }

    /*�������Ҫ��ѯ������: �滭����*/
    private PaintClass paintClassObj;
    public void setPaintClassObj(PaintClass paintClassObj) {
        this.paintClassObj = paintClassObj;
    }
    public PaintClass getPaintClassObj() {
        return this.paintClassObj;
    }

    /*�������Ҫ��ѯ������: �����û�*/
    private UserInfo userObj;
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }
    public UserInfo getUserObj() {
        return this.userObj;
    }

    /*�������Ҫ��ѯ������: ����ʱ��*/
    private String addTime;
    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
    public String getAddTime() {
        return this.addTime;
    }

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

    private int paintId;
    public void setPaintId(int paintId) {
        this.paintId = paintId;
    }
    public int getPaintId() {
        return paintId;
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
    @Resource UserInfoDAO userInfoDAO;
    @Resource PaintDAO paintDAO;

    /*��������Paint����*/
    private Paint paint;
    public void setPaint(Paint paint) {
        this.paint = paint;
    }
    public Paint getPaint() {
        return this.paint;
    }

    /*��ת�����Paint��ͼ*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*��ѯ���е�PaintClass��Ϣ*/
        List<PaintClass> paintClassList = paintClassDAO.QueryAllPaintClassInfo();
        ctx.put("paintClassList", paintClassList);
        /*��ѯ���е�UserInfo��Ϣ*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*���Paint��Ϣ*/
    @SuppressWarnings("deprecation")
    public String AddPaint() {
        ActionContext ctx = ActionContext.getContext();
        try {
            PaintClass paintClassObj = paintClassDAO.GetPaintClassByPaintClassId(paint.getPaintClassObj().getPaintClassId());
            paint.setPaintClassObj(paintClassObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(paint.getUserObj().getUser_name());
            paint.setUserObj(userObj);
            /*������ƷͼƬ�ϴ�*/
            String paintPhotoPath = "upload/noimage.jpg"; 
       	 	if(paintPhotoFile != null)
       	 		paintPhotoPath = photoUpload(paintPhotoFile,paintPhotoFileContentType);
       	 	paint.setPaintPhoto(paintPhotoPath);
            /*������Ʒ�ļ��ϴ�*/
            String paintFilePath = ""; 
       	 	if(paintFileFile != null)
       	 		paintFilePath = fileUpload(paintFileFile, paintFileFileFileName);
       	 	paint.setPaintFile(paintFilePath);
            paintDAO.AddPaint(paint);
            ctx.put("message",  java.net.URLEncoder.encode("Paint��ӳɹ�!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("ͼƬ�ļ���ʽ����!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Paint���ʧ��!"));
            return "error";
        }
    }

    /*��ѯPaint��Ϣ*/
    public String QueryPaint() {
        if(currentPage == 0) currentPage = 1;
        if(paintName == null) paintName = "";
        if(addTime == null) addTime = "";
        List<Paint> paintList = paintDAO.QueryPaintInfo(paintName, paintClassObj, userObj, addTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        paintDAO.CalculateTotalPageAndRecordNumber(paintName, paintClassObj, userObj, addTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = paintDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = paintDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("paintList",  paintList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("paintName", paintName);
        ctx.put("paintClassObj", paintClassObj);
        List<PaintClass> paintClassList = paintClassDAO.QueryAllPaintClassInfo();
        ctx.put("paintClassList", paintClassList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("addTime", addTime);
        return "query_view";
    }

    /*��̨������excel*/
    public String QueryPaintOutputToExcel() { 
        if(paintName == null) paintName = "";
        if(addTime == null) addTime = "";
        List<Paint> paintList = paintDAO.QueryPaintInfo(paintName,paintClassObj,userObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Paint��Ϣ��¼"; 
        String[] headers = { "��Ʒid","��Ʒ����","�滭����","��ƷͼƬ","�����","�����û�","����ʱ��"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<paintList.size();i++) {
        	Paint paint = paintList.get(i); 
        	dataset.add(new String[]{paint.getPaintId() + "",paint.getPaintName(),paint.getPaintClassObj().getPaintClassName(),
paint.getPaintPhoto(),paint.getHitNum() + "",paint.getUserObj().getName(),
paint.getAddTime()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Paint.xls");//filename�����ص�xls���������������Ӣ�� 
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
    /*ǰ̨��ѯPaint��Ϣ*/
    public String FrontQueryPaint() {
        if(currentPage == 0) currentPage = 1;
        if(paintName == null) paintName = "";
        if(addTime == null) addTime = "";
        List<Paint> paintList = paintDAO.QueryPaintInfo(paintName, paintClassObj, userObj, addTime, currentPage);
        /*�����ܵ�ҳ�����ܵļ�¼��*/
        paintDAO.CalculateTotalPageAndRecordNumber(paintName, paintClassObj, userObj, addTime);
        /*��ȡ���ܵ�ҳ����Ŀ*/
        totalPage = paintDAO.getTotalPage();
        /*��ǰ��ѯ�������ܼ�¼��*/
        recordNumber = paintDAO.getRecordNumber();
        ActionContext ctx = ActionContext.getContext();
        ctx.put("paintList",  paintList);
        ctx.put("totalPage", totalPage);
        ctx.put("recordNumber", recordNumber);
        ctx.put("currentPage", currentPage);
        ctx.put("paintName", paintName);
        ctx.put("paintClassObj", paintClassObj);
        List<PaintClass> paintClassList = paintClassDAO.QueryAllPaintClassInfo();
        ctx.put("paintClassList", paintClassList);
        ctx.put("userObj", userObj);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("addTime", addTime);
        return "front_query_view";
    }

    /*��ѯҪ�޸ĵ�Paint��Ϣ*/
    public String ModifyPaintQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������paintId��ȡPaint����*/
        Paint paint = paintDAO.GetPaintByPaintId(paintId);

        List<PaintClass> paintClassList = paintClassDAO.QueryAllPaintClassInfo();
        ctx.put("paintClassList", paintClassList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("paint",  paint);
        return "modify_view";
    }

    /*��ѯҪ�޸ĵ�Paint��Ϣ*/
    public String FrontShowPaintQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*��������paintId��ȡPaint����*/
        Paint paint = paintDAO.GetPaintByPaintId(paintId);

        List<PaintClass> paintClassList = paintClassDAO.QueryAllPaintClassInfo();
        ctx.put("paintClassList", paintClassList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("paint",  paint);
        return "front_show_view";
    }

    /*�����޸�Paint��Ϣ*/
    public String ModifyPaint() {
        ActionContext ctx = ActionContext.getContext();
        try {
            PaintClass paintClassObj = paintClassDAO.GetPaintClassByPaintClassId(paint.getPaintClassObj().getPaintClassId());
            paint.setPaintClassObj(paintClassObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(paint.getUserObj().getUser_name());
            paint.setUserObj(userObj);
            /*������ƷͼƬ�ϴ�*/
            if(paintPhotoFile != null) {
            	String paintPhotoPath = photoUpload(paintPhotoFile,paintPhotoFileContentType);
            	paint.setPaintPhoto(paintPhotoPath);
            }
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*������Ʒ�ļ��ϴ�*/
            if(paintFileFile != null)
       	 		paint.setPaintFile(fileUpload(paintFileFile, paintFileFileFileName));
            paintDAO.UpdatePaint(paint);
            ctx.put("message",  java.net.URLEncoder.encode("Paint��Ϣ���³ɹ�!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Paint��Ϣ����ʧ��!"));
            return "error";
       }
   }

    /*ɾ��Paint��Ϣ*/
    public String DeletePaint() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            paintDAO.DeletePaint(paintId);
            ctx.put("message",  java.net.URLEncoder.encode("Paintɾ���ɹ�!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Paintɾ��ʧ��!"));
            return "error";
        }
    }

}
