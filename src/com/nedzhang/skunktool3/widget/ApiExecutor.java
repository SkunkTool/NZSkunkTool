package com.nedzhang.skunktool3.widget;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.nedzhang.sterlingUtil.SterlingApiUtil;
import com.nedzhang.sterlingUtil.SterlingHttpTesterUtil;
import com.nedzhang.util.XPathUtil;
import com.nedzhang.util.XmlUtil;
import com.nedzhang.util.XsltTransformerManager;

public class ApiExecutor {

	private ApiExecutor() {

	}

//	public static String executeApi(final String interopUrl,
//			final boolean useHttp, final String userID, final String password,
//			final String progID, final Document testHarnessDefinition,
//			final Document userInpDocument,
//			final boolean verboseMode)
//			throws ParserConfigurationException, SAXException, IOException,
//			Exception {
//
//		final Node inputTransformNode = XPathUtil.selectSingleNode("ApiExecutor.executeApi",
//				"/TestHarness/Api/Input/InputTransform/*", testHarnessDefinition);
//
//		final Element apiNode = (Element) XPathUtil.selectSingleNode("ApiExecutor.executeApi",
//				"/TestHarness/Api", testHarnessDefinition);
//
//		final String serviceName = apiNode.getAttribute("FlowName");
//		final String apiName = apiNode.getAttribute("Name");
//
//
//		final Node outputTemplateNode = XPathUtil.selectSingleNode("ApiExecutor.executeApi",
//				"/TestHarness/Api/Output/Template/*", testHarnessDefinition);
//		
//		return executeApi(interopUrl, useHttp, userID, password, progID, apiName, serviceName, 
//				userInpDocument, inputTransformNode, outputTemplateNode, verboseMode); 
//
//	}

	public static String executeApi(final String interopUrl,
			final boolean useHttp, final String userID, final String password,
			final String progID, final String apiName, final String serviceName,
			final Document inputDocument, final Node inputTransformNode,
			final Node outputTemplateNode, final Node outputTransformNode, final boolean verboseMode)
			throws TransformerException, Exception,
			ParserConfigurationException, SAXException, IOException {
		
		boolean isFlow;
		String apiToCall;
		
		if (serviceName != null && serviceName.length() > 0) {
			isFlow = true;
			apiToCall = serviceName;
		} else {
			isFlow = false;
			apiToCall = apiName;
			
		}
		
		String apiInputTransformerString = XmlUtil.getXmlString(inputTransformNode);
		
		final Source xsltSource = 
				new StreamSource(new StringReader(apiInputTransformerString));
		
		final Source xmlSource = new DOMSource(inputDocument);

		// TODO: see if we can cache the transformer
		final String apiInput = XsltTransformerManager.transformToString(
				xsltSource, xmlSource, null);
		
		final String outputTemplate = XmlUtil.getXmlString(outputTemplateNode);

		String apiResult = executeApi(interopUrl, useHttp, userID, password, progID,
				apiToCall, isFlow, apiInput, outputTemplate, verboseMode);
		
		if (outputTransformNode != null) {
			
			String outInputTransformerString = XmlUtil.getXmlString(outputTransformNode);
			
			final Source outXsltSource = 
					new StreamSource(new StringReader(outInputTransformerString));
			
			final Source apiResultSource = new StreamSource(new StringReader(apiResult));

			// TODO: see if we can cache the transformer
			final String transformedOutput = XsltTransformerManager.transformToString(
					outXsltSource, apiResultSource, null);
			
			return transformedOutput;
		} else {
			return apiResult;
		}
	}
	

	public static String executeApi(final String interopUrl,
			final boolean useHttp, final String userID, final String password,
			final String progID, final String apiName, final boolean isFlow,
			final String input, final String outputTemplate,
			final boolean verboseMode) throws Exception,
			ParserConfigurationException, SAXException, IOException {

		if (verboseMode) {
			System.out
					.println(String
							.format("http: %s\nuserID: %s\npassword: %s\napiName: %s\nisFlow: %b\ninput: %s\noutputTemplate: %s",
									useHttp ? "Yes" : "No", userID,
									displayAsterix(password), apiName, isFlow,
									input, outputTemplate));
		}

		String output = null;

		if (useHttp) {
			output = SterlingHttpTesterUtil.invoke(interopUrl, userID,
					password, null, progID, apiName, isFlow, input,
					outputTemplate);

		} else {

			final Document outputDoc = SterlingApiUtil.invoke(userID, password,
					progID, apiName, isFlow, createDocFromString(input),
					createDocFromString(outputTemplate));

			output = outputDoc == null ? null : XmlUtil.getXmlString(outputDoc);

		}

		if (verboseMode) {
			System.out.println("response for " + apiName);
			System.out.println(output);
		}

		return output;

	}

//	private static Node selectNode(final Node node, final String xpath)
//			throws XPathExpressionException {
//		final XPathExpression apiExpression = XPathUtil.getXPathExpression(
//				"ApiExector.selectNode", xpath);
//
//		final Node target = (Node) apiExpression.evaluate(node,
//				XPathConstants.NODE);
//
//		return target;
//	}
	
	private static String displayAsterix(final String stringToHide) {

		if (stringToHide == null || stringToHide.length() == 0) {
			return stringToHide;
		} else {
			final StringBuilder outputBuilder = new StringBuilder(
					stringToHide.length());
			for (int i = 0; i < stringToHide.length(); i++) {
				outputBuilder.append('*');
			}
			return outputBuilder.toString();
		}
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
