package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.PostInfo;
import com.mobileclient.util.HttpUtil;

/*帖子管理业务逻辑层*/
public class PostInfoService {
	/* 添加帖子 */
	public String AddPostInfo(PostInfo postInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("postInfoId", postInfo.getPostInfoId() + "");
		params.put("title", postInfo.getTitle());
		params.put("content", postInfo.getContent());
		params.put("hitNum", postInfo.getHitNum() + "");
		params.put("userObj", postInfo.getUserObj());
		params.put("addTime", postInfo.getAddTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PostInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询帖子 */
	public List<PostInfo> QueryPostInfo(PostInfo queryConditionPostInfo) throws Exception {
		String urlString = HttpUtil.BASE_URL + "PostInfoServlet?action=query";
		if(queryConditionPostInfo != null) {
			urlString += "&title=" + URLEncoder.encode(queryConditionPostInfo.getTitle(), "UTF-8") + "";
			urlString += "&userObj=" + URLEncoder.encode(queryConditionPostInfo.getUserObj(), "UTF-8") + "";
			urlString += "&addTime=" + URLEncoder.encode(queryConditionPostInfo.getAddTime(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		PostInfoListHandler postInfoListHander = new PostInfoListHandler();
		xr.setContentHandler(postInfoListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<PostInfo> postInfoList = postInfoListHander.getPostInfoList();
		return postInfoList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<PostInfo> postInfoList = new ArrayList<PostInfo>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				PostInfo postInfo = new PostInfo();
				postInfo.setPostInfoId(object.getInt("postInfoId"));
				postInfo.setTitle(object.getString("title"));
				postInfo.setContent(object.getString("content"));
				postInfo.setHitNum(object.getInt("hitNum"));
				postInfo.setUserObj(object.getString("userObj"));
				postInfo.setAddTime(object.getString("addTime"));
				postInfoList.add(postInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postInfoList;
	}

	/* 更新帖子 */
	public String UpdatePostInfo(PostInfo postInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("postInfoId", postInfo.getPostInfoId() + "");
		params.put("title", postInfo.getTitle());
		params.put("content", postInfo.getContent());
		params.put("hitNum", postInfo.getHitNum() + "");
		params.put("userObj", postInfo.getUserObj());
		params.put("addTime", postInfo.getAddTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PostInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除帖子 */
	public String DeletePostInfo(int postInfoId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("postInfoId", postInfoId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PostInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "帖子信息删除失败!";
		}
	}

	/* 根据帖子id获取帖子对象 */
	public PostInfo GetPostInfo(int postInfoId)  {
		List<PostInfo> postInfoList = new ArrayList<PostInfo>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("postInfoId", postInfoId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "PostInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				PostInfo postInfo = new PostInfo();
				postInfo.setPostInfoId(object.getInt("postInfoId"));
				postInfo.setTitle(object.getString("title"));
				postInfo.setContent(object.getString("content"));
				postInfo.setHitNum(object.getInt("hitNum"));
				postInfo.setUserObj(object.getString("userObj"));
				postInfo.setAddTime(object.getString("addTime"));
				postInfoList.add(postInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = postInfoList.size();
		if(size>0) return postInfoList.get(0); 
		else return null; 
	}
}
