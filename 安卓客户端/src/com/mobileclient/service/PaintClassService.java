package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.PaintClass;
import com.mobileclient.util.HttpUtil;

/*�滭�������ҵ���߼���*/
public class PaintClassService {
	/* ��ӻ滭���� */
	public String AddPaintClass(PaintClass paintClass) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paintClassId", paintClass.getPaintClassId() + "");
		params.put("paintClassName", paintClass.getPaintClassName());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PaintClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* ��ѯ�滭���� */
	public List<PaintClass> QueryPaintClass(PaintClass queryConditionPaintClass) throws Exception {
		String urlString = HttpUtil.BASE_URL + "PaintClassServlet?action=query";
		if(queryConditionPaintClass != null) {
		}

		/* 2�����ݽ�����������һ������SAXParser����xml�ļ���ʽ
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		PaintClassListHandler paintClassListHander = new PaintClassListHandler();
		xr.setContentHandler(paintClassListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<PaintClass> paintClassList = paintClassListHander.getPaintClassList();
		return paintClassList;*/
		//��2���ǻ���json���ݸ�ʽ���������ǲ��õ��ǵ�2��
		List<PaintClass> paintClassList = new ArrayList<PaintClass>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				PaintClass paintClass = new PaintClass();
				paintClass.setPaintClassId(object.getInt("paintClassId"));
				paintClass.setPaintClassName(object.getString("paintClassName"));
				paintClassList.add(paintClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paintClassList;
	}

	/* ���»滭���� */
	public String UpdatePaintClass(PaintClass paintClass) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paintClassId", paintClass.getPaintClassId() + "");
		params.put("paintClassName", paintClass.getPaintClassName());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PaintClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* ɾ���滭���� */
	public String DeletePaintClass(int paintClassId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paintClassId", paintClassId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PaintClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "�滭������Ϣɾ��ʧ��!";
		}
	}

	/* ���ݻ滭����id��ȡ�滭������� */
	public PaintClass GetPaintClass(int paintClassId)  {
		List<PaintClass> paintClassList = new ArrayList<PaintClass>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("paintClassId", paintClassId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PaintClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				PaintClass paintClass = new PaintClass();
				paintClass.setPaintClassId(object.getInt("paintClassId"));
				paintClass.setPaintClassName(object.getString("paintClassName"));
				paintClassList.add(paintClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = paintClassList.size();
		if(size>0) return paintClassList.get(0); 
		else return null; 
	}
}
