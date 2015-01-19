package com.nedzhang.skunktool3.widget;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nedzhang.util.XmlUtil;

public class XmlViewerSource {

	private XmlViewerSource() {
		
	}
	
	private XmlViewerSource(Document dataDoc) {
		
		processNode("/", dataDoc);
		
	}
	
	private void processNode(String path, Node dataNode) {
		
		if (dataNode instanceof Element && dataNode.hasAttributes()) {
			NamedNodeMap attributeMap = dataNode.getAttributes();
			
			
			if (attributeMap != null && attributeMap.getLength() > 0) {
				for (int i=0; i<attributeMap.getLength(); i++) {
										
					Node attributeNode = attributeMap.item(i);
					
					String attributName = attributeNode.getNodeName();
					
					String attributValue = attributeNode.getNodeValue();
					
					
					
				}
			}
		}
		
		if (dataNode.hasChildNodes()) {
			NodeList childNodes = dataNode.getChildNodes();
			
			if (childNodes != null && childNodes.getLength() > 0) {
				for (int i=0 ; i<childNodes.getLength(); i++) {
					processNode(path + "/" + dataNode.getNodeName(), childNodes.item(i));
				}
			}
		}
	}

	public static XmlViewerSource createViewerSource(Document dataDoc) {
		
		XmlViewerSource vsource = new XmlViewerSource(dataDoc);
		
		
		
		return vsource;
		
	}

}
