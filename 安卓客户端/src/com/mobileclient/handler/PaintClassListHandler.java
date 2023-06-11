package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.PaintClass;
public class PaintClassListHandler extends DefaultHandler {
	private List<PaintClass> paintClassList = null;
	private PaintClass paintClass;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (paintClass != null) { 
            String valueString = new String(ch, start, length); 
            if ("paintClassId".equals(tempString)) 
            	paintClass.setPaintClassId(new Integer(valueString).intValue());
            else if ("paintClassName".equals(tempString)) 
            	paintClass.setPaintClassName(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("PaintClass".equals(localName)&&paintClass!=null){
			paintClassList.add(paintClass);
			paintClass = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		paintClassList = new ArrayList<PaintClass>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("PaintClass".equals(localName)) {
            paintClass = new PaintClass(); 
        }
        tempString = localName; 
	}

	public List<PaintClass> getPaintClassList() {
		return this.paintClassList;
	}
}
