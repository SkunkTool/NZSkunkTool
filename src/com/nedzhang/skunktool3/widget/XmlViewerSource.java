//package com.nedzhang.skunktool3.widget;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//public class XmlViewerSource {
//
//	private XmlViewerSource() {
//
//	}
//
//	private XmlViewerSource(final Document dataDoc) {
//
//		processNode("/", dataDoc);
//
//	}
//
//	private void processNode(final String path, final Node dataNode) {
//
//		if (dataNode instanceof Element && dataNode.hasAttributes()) {
//			final NamedNodeMap attributeMap = dataNode.getAttributes();
//
//			if (attributeMap != null && attributeMap.getLength() > 0) {
//				for (int i = 0; i < attributeMap.getLength(); i++) {
//
//					final Node attributeNode = attributeMap.item(i);
//
////					final String attributName = attributeNode.getNodeName();
////
////					final String attributValue = attributeNode.getNodeValue();
//
//				}
//			}
//		}
//
//		if (dataNode.hasChildNodes()) {
//			final NodeList childNodes = dataNode.getChildNodes();
//
//			if (childNodes != null && childNodes.getLength() > 0) {
//				for (int i = 0; i < childNodes.getLength(); i++) {
//					processNode(path + "/" + dataNode.getNodeName(),
//							childNodes.item(i));
//				}
//			}
//		}
//	}
//
//	public static XmlViewerSource createViewerSource(final Document dataDoc) {
//
//		final XmlViewerSource vsource = new XmlViewerSource(dataDoc);
//
//		return vsource;
//
//	}
//
//}
