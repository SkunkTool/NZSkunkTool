package com.nedzhang.skunktool3.widget;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
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

	public static String executeApi(final String interopUrl,
			final boolean useHttp, final String userID, final String password,
			final String progID, final Document testHarnessDefinition,
			final Document userInput, final boolean verboseMode)
			throws ParserConfigurationException, SAXException, IOException,
			Exception {

		final Node inputTransformNode = selectNode(testHarnessDefinition,
				"/TestHarness/Api/Input/InputTransform/*");

		String apiInputTransformerString = XmlUtil.getXmlString(inputTransformNode);
		
		final Source xsltSource = 
				new StreamSource(new StringReader(apiInputTransformerString));
		
		final Source xmlSource = new DOMSource(userInput);

		// TODO: see if we can cache the transformer
		final String apiInput = XsltTransformerManager.transformToString(
				xsltSource, xmlSource, null);

		final Element apiNode = (Element) selectNode(testHarnessDefinition,
				"/TestHarness/Api");

		final String serviceName = apiNode.getAttribute("FlowName");
		boolean isFlow;
		String apiName;

		if (serviceName != null && serviceName.length() > 0) {
			isFlow = true;
			apiName = serviceName;
		} else {
			isFlow = false;
			apiName = apiNode.getAttribute("Name");
		}

		final Node outputTemplateNode = selectNode(testHarnessDefinition,
				"/TestHarness/Api/Output/Template/*");
		final String outputTemplate = XmlUtil.getXmlString(outputTemplateNode);

		return executeApi(interopUrl, useHttp, userID, password, progID,
				apiName, isFlow, apiInput, outputTemplate, verboseMode);
	}

	private static Node selectNode(final Node node, final String xpath)
			throws XPathExpressionException {
		final XPathExpression apiExpression = XPathUtil.getXPathExpression(
				"ApiExector.selectNode", xpath);

		final Node target = (Node) apiExpression.evaluate(node,
				XPathConstants.NODE);

		return target;
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
