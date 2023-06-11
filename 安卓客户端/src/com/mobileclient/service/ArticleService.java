package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Article;
import com.mobileclient.util.HttpUtil;

/*文章管理业务逻辑层*/
public class ArticleService {
	/* 添加文章 */
	public String AddArticle(Article article) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("articleId", article.getArticleId() + "");
		params.put("title", article.getTitle());
		params.put("articleClassObj", article.getArticleClassObj() + "");
		params.put("content", article.getContent());
		params.put("hitNum", article.getHitNum() + "");
		params.put("userObj", article.getUserObj());
		params.put("addTime", article.getAddTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ArticleServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询文章 */
	public List<Article> QueryArticle(Article queryConditionArticle) throws Exception {
		String urlString = HttpUtil.BASE_URL + "ArticleServlet?action=query";
		if(queryConditionArticle != null) {
			urlString += "&title=" + URLEncoder.encode(queryConditionArticle.getTitle(), "UTF-8") + "";
			urlString += "&articleClassObj=" + queryConditionArticle.getArticleClassObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionArticle.getUserObj(), "UTF-8") + "";
			urlString += "&addTime=" + URLEncoder.encode(queryConditionArticle.getAddTime(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		ArticleListHandler articleListHander = new ArticleListHandler();
		xr.setContentHandler(articleListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Article> articleList = articleListHander.getArticleList();
		return articleList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Article> articleList = new ArrayList<Article>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Article article = new Article();
				article.setArticleId(object.getInt("articleId"));
				article.setTitle(object.getString("title"));
				article.setArticleClassObj(object.getInt("articleClassObj"));
				article.setContent(object.getString("content"));
				article.setHitNum(object.getInt("hitNum"));
				article.setUserObj(object.getString("userObj"));
				article.setAddTime(object.getString("addTime"));
				articleList.add(article);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleList;
	}

	/* 更新文章 */
	public String UpdateArticle(Article article) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("articleId", article.getArticleId() + "");
		params.put("title", article.getTitle());
		params.put("articleClassObj", article.getArticleClassObj() + "");
		params.put("content", article.getContent());
		params.put("hitNum", article.getHitNum() + "");
		params.put("userObj", article.getUserObj());
		params.put("addTime", article.getAddTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ArticleServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除文章 */
	public String DeleteArticle(int articleId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("articleId", articleId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ArticleServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "文章信息删除失败!";
		}
	}

	/* 根据文章id获取文章对象 */
	public Article GetArticle(int articleId)  {
		List<Article> articleList = new ArrayList<Article>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("articleId", articleId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ArticleServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Article article = new Article();
				article.setArticleId(object.getInt("articleId"));
				article.setTitle(object.getString("title"));
				article.setArticleClassObj(object.getInt("articleClassObj"));
				article.setContent(object.getString("content"));
				article.setHitNum(object.getInt("hitNum"));
				article.setUserObj(object.getString("userObj"));
				article.setAddTime(object.getString("addTime"));
				articleList.add(article);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = articleList.size();
		if(size>0) return articleList.get(0); 
		else return null; 
	}
}
