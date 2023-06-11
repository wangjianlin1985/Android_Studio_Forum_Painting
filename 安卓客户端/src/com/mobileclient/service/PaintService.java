package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Paint;
import com.mobileclient.util.HttpUtil;

/*绘画管理业务逻辑层*/
public class PaintService {
	/* 添加绘画 */
	public String AddPaint(Paint paint) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paintId", paint.getPaintId() + "");
		params.put("paintName", paint.getPaintName());
		params.put("paintClassObj", paint.getPaintClassObj() + "");
		params.put("paintPhoto", paint.getPaintPhoto());
		params.put("paintDesc", paint.getPaintDesc());
		params.put("paintFile", paint.getPaintFile());
		params.put("hitNum", paint.getHitNum() + "");
		params.put("userObj", paint.getUserObj());
		params.put("addTime", paint.getAddTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PaintServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询绘画 */
	public List<Paint> QueryPaint(Paint queryConditionPaint) throws Exception {
		String urlString = HttpUtil.BASE_URL + "PaintServlet?action=query";
		if(queryConditionPaint != null) {
			urlString += "&paintName=" + URLEncoder.encode(queryConditionPaint.getPaintName(), "UTF-8") + "";
			urlString += "&paintClassObj=" + queryConditionPaint.getPaintClassObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionPaint.getUserObj(), "UTF-8") + "";
			urlString += "&addTime=" + URLEncoder.encode(queryConditionPaint.getAddTime(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		PaintListHandler paintListHander = new PaintListHandler();
		xr.setContentHandler(paintListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Paint> paintList = paintListHander.getPaintList();
		return paintList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Paint> paintList = new ArrayList<Paint>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Paint paint = new Paint();
				paint.setPaintId(object.getInt("paintId"));
				paint.setPaintName(object.getString("paintName"));
				paint.setPaintClassObj(object.getInt("paintClassObj"));
				paint.setPaintPhoto(object.getString("paintPhoto"));
				paint.setPaintDesc(object.getString("paintDesc"));
				paint.setPaintFile(object.getString("paintFile"));
				paint.setHitNum(object.getInt("hitNum"));
				paint.setUserObj(object.getString("userObj"));
				paint.setAddTime(object.getString("addTime"));
				paintList.add(paint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paintList;
	}

	/* 更新绘画 */
	public String UpdatePaint(Paint paint) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paintId", paint.getPaintId() + "");
		params.put("paintName", paint.getPaintName());
		params.put("paintClassObj", paint.getPaintClassObj() + "");
		params.put("paintPhoto", paint.getPaintPhoto());
		params.put("paintDesc", paint.getPaintDesc());
		params.put("paintFile", paint.getPaintFile());
		params.put("hitNum", paint.getHitNum() + "");
		params.put("userObj", paint.getUserObj());
		params.put("addTime", paint.getAddTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PaintServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除绘画 */
	public String DeletePaint(int paintId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paintId", paintId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PaintServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "绘画信息删除失败!";
		}
	}

	/* 根据作品id获取绘画对象 */
	public Paint GetPaint(int paintId)  {
		List<Paint> paintList = new ArrayList<Paint>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paintId", paintId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PaintServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Paint paint = new Paint();
				paint.setPaintId(object.getInt("paintId"));
				paint.setPaintName(object.getString("paintName"));
				paint.setPaintClassObj(object.getInt("paintClassObj"));
				paint.setPaintPhoto(object.getString("paintPhoto"));
				paint.setPaintDesc(object.getString("paintDesc"));
				paint.setPaintFile(object.getString("paintFile"));
				paint.setHitNum(object.getInt("hitNum"));
				paint.setUserObj(object.getString("userObj"));
				paint.setAddTime(object.getString("addTime"));
				paintList.add(paint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = paintList.size();
		if(size>0) return paintList.get(0); 
		else return null; 
	}
}
