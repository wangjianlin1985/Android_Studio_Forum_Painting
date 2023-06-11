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

	/*图片或文件字段paintPhoto参数接收*/
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
	/*图片或文件字段paintFile参数接收*/
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
    /*界面层需要查询的属性: 作品名称*/
    private String paintName;
    public void setPaintName(String paintName) {
        this.paintName = paintName;
    }
    public String getPaintName() {
        return this.paintName;
    }

    /*界面层需要查询的属性: 绘画分类*/
    private PaintClass paintClassObj;
    public void setPaintClassObj(PaintClass paintClassObj) {
        this.paintClassObj = paintClassObj;
    }
    public PaintClass getPaintClassObj() {
        return this.paintClassObj;
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

    private int paintId;
    public void setPaintId(int paintId) {
        this.paintId = paintId;
    }
    public int getPaintId() {
        return paintId;
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
    @Resource UserInfoDAO userInfoDAO;
    @Resource PaintDAO paintDAO;

    /*待操作的Paint对象*/
    private Paint paint;
    public void setPaint(Paint paint) {
        this.paint = paint;
    }
    public Paint getPaint() {
        return this.paint;
    }

    /*跳转到添加Paint视图*/
    public String AddView() {
        ActionContext ctx = ActionContext.getContext();
        /*查询所有的PaintClass信息*/
        List<PaintClass> paintClassList = paintClassDAO.QueryAllPaintClassInfo();
        ctx.put("paintClassList", paintClassList);
        /*查询所有的UserInfo信息*/
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        return "add_view";
    }

    /*添加Paint信息*/
    @SuppressWarnings("deprecation")
    public String AddPaint() {
        ActionContext ctx = ActionContext.getContext();
        try {
            PaintClass paintClassObj = paintClassDAO.GetPaintClassByPaintClassId(paint.getPaintClassObj().getPaintClassId());
            paint.setPaintClassObj(paintClassObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(paint.getUserObj().getUser_name());
            paint.setUserObj(userObj);
            /*处理作品图片上传*/
            String paintPhotoPath = "upload/noimage.jpg"; 
       	 	if(paintPhotoFile != null)
       	 		paintPhotoPath = photoUpload(paintPhotoFile,paintPhotoFileContentType);
       	 	paint.setPaintPhoto(paintPhotoPath);
            /*处理作品文件上传*/
            String paintFilePath = ""; 
       	 	if(paintFileFile != null)
       	 		paintFilePath = fileUpload(paintFileFile, paintFileFileFileName);
       	 	paint.setPaintFile(paintFilePath);
            paintDAO.AddPaint(paint);
            ctx.put("message",  java.net.URLEncoder.encode("Paint添加成功!"));
            return "add_success";
        } catch(FileTypeException ex) {
        	ctx.put("error",  java.net.URLEncoder.encode("图片文件格式不对!"));
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Paint添加失败!"));
            return "error";
        }
    }

    /*查询Paint信息*/
    public String QueryPaint() {
        if(currentPage == 0) currentPage = 1;
        if(paintName == null) paintName = "";
        if(addTime == null) addTime = "";
        List<Paint> paintList = paintDAO.QueryPaintInfo(paintName, paintClassObj, userObj, addTime, currentPage);
        /*计算总的页数和总的记录数*/
        paintDAO.CalculateTotalPageAndRecordNumber(paintName, paintClassObj, userObj, addTime);
        /*获取到总的页码数目*/
        totalPage = paintDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*后台导出到excel*/
    public String QueryPaintOutputToExcel() { 
        if(paintName == null) paintName = "";
        if(addTime == null) addTime = "";
        List<Paint> paintList = paintDAO.QueryPaintInfo(paintName,paintClassObj,userObj,addTime);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Paint信息记录"; 
        String[] headers = { "作品id","作品名称","绘画分类","作品图片","点击率","发布用户","发布时间"};
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
		HttpServletResponse response = null;//创建一个HttpServletResponse对象 
		OutputStream out = null;//创建一个输出流对象 
		try { 
			response = ServletActionContext.getResponse();//初始化HttpServletResponse对象 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Paint.xls");//filename是下载的xls的名，建议最好用英文 
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
    /*前台查询Paint信息*/
    public String FrontQueryPaint() {
        if(currentPage == 0) currentPage = 1;
        if(paintName == null) paintName = "";
        if(addTime == null) addTime = "";
        List<Paint> paintList = paintDAO.QueryPaintInfo(paintName, paintClassObj, userObj, addTime, currentPage);
        /*计算总的页数和总的记录数*/
        paintDAO.CalculateTotalPageAndRecordNumber(paintName, paintClassObj, userObj, addTime);
        /*获取到总的页码数目*/
        totalPage = paintDAO.getTotalPage();
        /*当前查询条件下总记录数*/
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

    /*查询要修改的Paint信息*/
    public String ModifyPaintQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键paintId获取Paint对象*/
        Paint paint = paintDAO.GetPaintByPaintId(paintId);

        List<PaintClass> paintClassList = paintClassDAO.QueryAllPaintClassInfo();
        ctx.put("paintClassList", paintClassList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("paint",  paint);
        return "modify_view";
    }

    /*查询要修改的Paint信息*/
    public String FrontShowPaintQuery() {
        ActionContext ctx = ActionContext.getContext();
        /*根据主键paintId获取Paint对象*/
        Paint paint = paintDAO.GetPaintByPaintId(paintId);

        List<PaintClass> paintClassList = paintClassDAO.QueryAllPaintClassInfo();
        ctx.put("paintClassList", paintClassList);
        List<UserInfo> userInfoList = userInfoDAO.QueryAllUserInfoInfo();
        ctx.put("userInfoList", userInfoList);
        ctx.put("paint",  paint);
        return "front_show_view";
    }

    /*更新修改Paint信息*/
    public String ModifyPaint() {
        ActionContext ctx = ActionContext.getContext();
        try {
            PaintClass paintClassObj = paintClassDAO.GetPaintClassByPaintClassId(paint.getPaintClassObj().getPaintClassId());
            paint.setPaintClassObj(paintClassObj);
            UserInfo userObj = userInfoDAO.GetUserInfoByUser_name(paint.getUserObj().getUser_name());
            paint.setUserObj(userObj);
            /*处理作品图片上传*/
            if(paintPhotoFile != null) {
            	String paintPhotoPath = photoUpload(paintPhotoFile,paintPhotoFileContentType);
            	paint.setPaintPhoto(paintPhotoPath);
            }
            String path = ServletActionContext.getServletContext().getRealPath("/upload"); 
            /*处理作品文件上传*/
            if(paintFileFile != null)
       	 		paint.setPaintFile(fileUpload(paintFileFile, paintFileFileFileName));
            paintDAO.UpdatePaint(paint);
            ctx.put("message",  java.net.URLEncoder.encode("Paint信息更新成功!"));
            return "modify_success";
        } catch (Exception e) {
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Paint信息更新失败!"));
            return "error";
       }
   }

    /*删除Paint信息*/
    public String DeletePaint() {
        ActionContext ctx = ActionContext.getContext();
        try { 
            paintDAO.DeletePaint(paintId);
            ctx.put("message",  java.net.URLEncoder.encode("Paint删除成功!"));
            return "delete_success";
        } catch (Exception e) { 
            e.printStackTrace();
            ctx.put("error",  java.net.URLEncoder.encode("Paint删除失败!"));
            return "error";
        }
    }

}
