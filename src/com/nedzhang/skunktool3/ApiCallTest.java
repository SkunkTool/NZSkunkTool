package com.nedzhang.skunktool3;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.nedzhang.sterlingUtil.SterlingApiUtil;
import com.nedzhang.util.XmlUtil;

public class ApiCallTest {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException, Exception {
		// TODO Auto-generated method stub

		String userID = "admin";
		String password = "password";


		final Document outputDoc = SterlingApiUtil.invoke(userID, password,
				"Ned Zhang Skunk tool", "getUserList", false,
				createDocFromString("<User />"),
				null);

		final String output = outputDoc == null ? null : XmlUtil
				.getXmlString(outputDoc);

		System.out.println(output);
		
	}
	
	private static Document createDocFromString(final String xmlString)
			throws IllegalArgumentException, ParserConfigurationException,
			SAXException, IOException {

		if ((xmlString == null) || (xmlString.length() == 0)) {
			return null;
		} else {
			return XmlUtil.getDocument(xmlString);
		}
	}
	

}
