package com.mobileclient.service;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mobileclient.domain.Reply;
import com.mobileclient.util.HttpUtil;

/*帖子回复管理业务逻辑层*/
public class ReplyService {
	/* 添加帖子回复 */
	public String AddReply(Reply reply) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("replyId", reply.getReplyId() + "");
		params.put("postInfoObj", reply.getPostInfoObj() + "");
		params.put("content", reply.getContent());
		params.put("userObj", reply.getUserObj());
		params.put("replyTime", reply.getReplyTime());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ReplyServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 查询帖子回复 */
	public List<Reply> QueryReply(Reply queryConditionReply) throws Exception {
		String urlString = HttpUtil.BASE_URL + "ReplyServlet?action=query";
		if(queryConditionReply != null) {
			urlString += "&postInfoObj=" + queryConditionReply.getPostInfoObj();
			urlString += "&userObj=" + URLEncoder.encode(queryConditionReply.getUserObj(), "UTF-8") + "";
			urlString += "&replyTime=" + URLEncoder.encode(queryConditionReply.getReplyTime(), "UTF-8") + "";
		}

		/* 2种数据解析方法，第一种是用SAXParser解析xml文件格式
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		ReplyListHandler replyListHander = new ReplyListHandler();
		xr.setContentHandler(replyListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Reply> replyList = replyListHander.getReplyList();
		return replyList;*/
		//第2种是基于json数据格式解析，我们采用的是第2种
		List<Reply> replyList = new ArrayList<Reply>();
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(urlString, null, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Reply reply = new Reply();
				reply.setReplyId(object.getInt("replyId"));
				reply.setPostInfoObj(object.getInt("postInfoObj"));
				reply.setContent(object.getString("content"));
				reply.setUserObj(object.getString("userObj"));
				reply.setReplyTime(object.getString("replyTime"));
				replyList.add(reply);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return replyList;
	}

	/* 更新帖子回复 */
	public String UpdateReply(Reply reply) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("replyId", reply.getReplyId() + "");
		params.put("postInfoObj", reply.getPostInfoObj() + "");
		params.put("content", reply.getContent());
		params.put("userObj", reply.getUserObj());
		params.put("replyTime", reply.getReplyTime());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ReplyServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/* 删除帖子回复 */
	public String DeleteReply(int replyId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("replyId", replyId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ReplyServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "帖子回复信息删除失败!";
		}
	}

	/* 根据回复id获取帖子回复对象 */
	public Reply GetReply(int replyId)  {
		List<Reply> replyList = new ArrayList<Reply>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("replyId", replyId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "ReplyServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Reply reply = new Reply();
				reply.setReplyId(object.getInt("replyId"));
				reply.setPostInfoObj(object.getInt("postInfoObj"));
				reply.setContent(object.getString("content"));
				reply.setUserObj(object.getString("userObj"));
				reply.setReplyTime(object.getString("replyTime"));
				replyList.add(reply);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = replyList.size();
		if(size>0) return replyList.get(0); 
		else return null; 
	}
}
