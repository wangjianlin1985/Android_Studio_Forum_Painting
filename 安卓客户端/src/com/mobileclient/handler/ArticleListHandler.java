package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Article;
public class ArticleListHandler extends DefaultHandler {
	private List<Article> articleList = null;
	private Article article;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (article != null) { 
            String valueString = new String(ch, start, length); 
            if ("articleId".equals(tempString)) 
            	article.setArticleId(new Integer(valueString).intValue());
            else if ("title".equals(tempString)) 
            	article.setTitle(valueString); 
            else if ("articleClassObj".equals(tempString)) 
            	article.setArticleClassObj(new Integer(valueString).intValue());
            else if ("content".equals(tempString)) 
            	article.setContent(valueString); 
            else if ("hitNum".equals(tempString)) 
            	article.setHitNum(new Integer(valueString).intValue());
            else if ("userObj".equals(tempString)) 
            	article.setUserObj(valueString); 
            else if ("addTime".equals(tempString)) 
            	article.setAddTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Article".equals(localName)&&article!=null){
			articleList.add(article);
			article = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		articleList = new ArrayList<Article>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Article".equals(localName)) {
            article = new Article(); 
        }
        tempString = localName; 
	}

	public List<Article> getArticleList() {
		return this.articleList;
	}
}
