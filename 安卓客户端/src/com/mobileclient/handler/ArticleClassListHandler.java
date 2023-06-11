package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.ArticleClass;
public class ArticleClassListHandler extends DefaultHandler {
	private List<ArticleClass> articleClassList = null;
	private ArticleClass articleClass;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (articleClass != null) { 
            String valueString = new String(ch, start, length); 
            if ("articleClassId".equals(tempString)) 
            	articleClass.setArticleClassId(new Integer(valueString).intValue());
            else if ("articleClassName".equals(tempString)) 
            	articleClass.setArticleClassName(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("ArticleClass".equals(localName)&&articleClass!=null){
			articleClassList.add(articleClass);
			articleClass = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		articleClassList = new ArrayList<ArticleClass>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("ArticleClass".equals(localName)) {
            articleClass = new ArticleClass(); 
        }
        tempString = localName; 
	}

	public List<ArticleClass> getArticleClassList() {
		return this.articleClassList;
	}
}
