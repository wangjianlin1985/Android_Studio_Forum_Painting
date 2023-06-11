package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.PostInfo;
public class PostInfoListHandler extends DefaultHandler {
	private List<PostInfo> postInfoList = null;
	private PostInfo postInfo;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (postInfo != null) { 
            String valueString = new String(ch, start, length); 
            if ("postInfoId".equals(tempString)) 
            	postInfo.setPostInfoId(new Integer(valueString).intValue());
            else if ("title".equals(tempString)) 
            	postInfo.setTitle(valueString); 
            else if ("content".equals(tempString)) 
            	postInfo.setContent(valueString); 
            else if ("hitNum".equals(tempString)) 
            	postInfo.setHitNum(new Integer(valueString).intValue());
            else if ("userObj".equals(tempString)) 
            	postInfo.setUserObj(valueString); 
            else if ("addTime".equals(tempString)) 
            	postInfo.setAddTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("PostInfo".equals(localName)&&postInfo!=null){
			postInfoList.add(postInfo);
			postInfo = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		postInfoList = new ArrayList<PostInfo>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("PostInfo".equals(localName)) {
            postInfo = new PostInfo(); 
        }
        tempString = localName; 
	}

	public List<PostInfo> getPostInfoList() {
		return this.postInfoList;
	}
}
