package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Reply;
public class ReplyListHandler extends DefaultHandler {
	private List<Reply> replyList = null;
	private Reply reply;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (reply != null) { 
            String valueString = new String(ch, start, length); 
            if ("replyId".equals(tempString)) 
            	reply.setReplyId(new Integer(valueString).intValue());
            else if ("postInfoObj".equals(tempString)) 
            	reply.setPostInfoObj(new Integer(valueString).intValue());
            else if ("content".equals(tempString)) 
            	reply.setContent(valueString); 
            else if ("userObj".equals(tempString)) 
            	reply.setUserObj(valueString); 
            else if ("replyTime".equals(tempString)) 
            	reply.setReplyTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Reply".equals(localName)&&reply!=null){
			replyList.add(reply);
			reply = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		replyList = new ArrayList<Reply>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Reply".equals(localName)) {
            reply = new Reply(); 
        }
        tempString = localName; 
	}

	public List<Reply> getReplyList() {
		return this.replyList;
	}
}
