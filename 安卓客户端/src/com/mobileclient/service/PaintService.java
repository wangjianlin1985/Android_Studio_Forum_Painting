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

/*�滭����ҵ���߼���*/
public class PaintService {
	/* ��ӻ滭 */
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

	/* ��ѯ�滭 */
	public List<Paint> QueryPaint(Paint queryConditionPaint) throws Exception {
		String urlString = HttpUtil.BASE_URL + "PaintServlet?action=query";
		if(queryConditionPaint != null) {
			urlString += "&paintName=" + URLEncoder.encode(queryConditionPaint.getPaintName(), "UTF-8") + "";
			urlString += "&paintClassObj=" + queryConditionPaint.getPaintClassObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionPaint.getUserObj(), "UTF-8") + "";
			urlString += "&addTime=" + URLEncoder.encode(queryConditionPaint.getAddTime(), "UTF-8") + "";
		}

		/* 2�����ݽ�����������һ������SAXParser����xml�ļ���ʽ
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
		//��2���ǻ���json���ݸ�ʽ���������ǲ��õ��ǵ�2��
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

	/* ���»滭 */
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

	/* ɾ���滭 */
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
			return "�滭��Ϣɾ��ʧ��!";
		}
	}

	/* ������Ʒid��ȡ�滭���� */
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
