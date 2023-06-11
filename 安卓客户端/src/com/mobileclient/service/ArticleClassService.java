package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.ArticleClass;
import com.mobileclient.util.HttpUtil;

/*文章分类管理业务逻辑层*/
public class ArticleClassService {
	/* 添加文章分类 */
	public String AddArticleClass(ArticleClass articleClass) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("articleClassId", articleClass.getArticleClassId() + "");
		params.put("articleClassName", articleClass.getArticleClassName());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ArticleClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询文章分类 */
	public List<ArticleClass> QueryArticleClass(ArticleClass queryConditionArticleClass) throws Exception {
		String urlString = HttpUtil.BASE_URL + "ArticleClassServlet?action=query";
		if(queryConditionArticleClass != null) {
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		ArticleClassListHandler articleClassListHander = new ArticleClassListHandler();
		xr.setContentHandler(articleClassListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<ArticleClass> articleClassList = articleClassListHander.getArticleClassList();
		return articleClassList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<ArticleClass> articleClassList = new ArrayList<ArticleClass>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				ArticleClass articleClass = new ArticleClass();
				articleClass.setArticleClassId(object.getInt("articleClassId"));
				articleClass.setArticleClassName(object.getString("articleClassName"));
				articleClassList.add(articleClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleClassList;
	}

	/* 更新文章分类 */
	public String UpdateArticleClass(ArticleClass articleClass) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("articleClassId", articleClass.getArticleClassId() + "");
		params.put("articleClassName", articleClass.getArticleClassName());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ArticleClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除文章分类 */
	public String DeleteArticleClass(int articleClassId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("articleClassId", articleClassId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ArticleClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "文章分类信息删除失败!";
		}
	}

	/* 根据文章分类id获取文章分类对象 */
	public ArticleClass GetArticleClass(int articleClassId)  {
		List<ArticleClass> articleClassList = new ArrayList<ArticleClass>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("articleClassId", articleClassId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ArticleClassServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				ArticleClass articleClass = new ArticleClass();
				articleClass.setArticleClassId(object.getInt("articleClassId"));
				articleClass.setArticleClassName(object.getString("articleClassName"));
				articleClassList.add(articleClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = articleClassList.size();
		if(size>0) return articleClassList.get(0); 
		else return null; 
	}
}
