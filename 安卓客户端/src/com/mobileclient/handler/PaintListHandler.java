package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Paint;
public class PaintListHandler extends DefaultHandler {
	private List<Paint> paintList = null;
	private Paint paint;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (paint != null) { 
            String valueString = new String(ch, start, length); 
            if ("paintId".equals(tempString)) 
            	paint.setPaintId(new Integer(valueString).intValue());
            else if ("paintName".equals(tempString)) 
            	paint.setPaintName(valueString); 
            else if ("paintClassObj".equals(tempString)) 
            	paint.setPaintClassObj(new Integer(valueString).intValue());
            else if ("paintPhoto".equals(tempString)) 
            	paint.setPaintPhoto(valueString); 
            else if ("paintDesc".equals(tempString)) 
            	paint.setPaintDesc(valueString); 
            else if ("paintFile".equals(tempString)) 
            	paint.setPaintFile(valueString); 
            else if ("hitNum".equals(tempString)) 
            	paint.setHitNum(new Integer(valueString).intValue());
            else if ("userObj".equals(tempString)) 
            	paint.setUserObj(valueString); 
            else if ("addTime".equals(tempString)) 
            	paint.setAddTime(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Paint".equals(localName)&&paint!=null){
			paintList.add(paint);
			paint = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		paintList = new ArrayList<Paint>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Paint".equals(localName)) {
            paint = new Paint(); 
        }
        tempString = localName; 
	}

	public List<Paint> getPaintList() {
		return this.paintList;
	}
}
