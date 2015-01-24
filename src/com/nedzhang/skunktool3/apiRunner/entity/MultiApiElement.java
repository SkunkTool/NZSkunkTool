package com.nedzhang.skunktool3.apiRunner.entity;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nedzhang.util.XPathUtil;
import com.nedzhang.util.XmlUtil;

public class MultiApiElement {

	private final Element multiApiRunElement;

	// private String flowName;
	// private boolean isExtendedDbApi;
	// private String apiName;
	// private String version;
	// private String apiInput;

	public MultiApiElement(final Element multipApiRunElement) {
		multiApiRunElement = multipApiRunElement;
	}

	public String getFlowName() {
		return getAttribute("FlowName");
	}

	public void setFlowName(final String flowName) {
		setAttribute("FlowName", flowName);
	}

	public boolean isExtendedDbApi() {
		return getAttribute("IsExtendedDbApi") == "Y";
	}

	public void setExtendedDbApi(final boolean isExtendedDbApi) {
		setAttribute("IsExtendedDbApi", isExtendedDbApi ? "Y" : "N");
	}

	public String getApiName() {
		return getAttribute("Name");
	}

	public void setApiName(final String apiName) {
		setAttribute("Name", apiName);
	}

	public String getVersion() {
		return getAttribute("Version");
	}

	public void setVersion(final String version) {
		setAttribute("Version", version);
	}

	public String getApiInput() throws XPathExpressionException {
		final Node inputNode = getApiInputElement();
		if (inputNode != null) {
			return XmlUtil.getXmlString(inputNode);
		} else {
			return null;
		}
	}

	private Node getApiInputElement() throws XPathExpressionException {
		final XPathExpression inputNodeExpression = XPathUtil
				.getXPathExpression("MultiApiElement->GetMultiApiInput",
						"Input/*");
		final Node inputNode = (Node) inputNodeExpression.evaluate(
				multiApiRunElement, XPathConstants.NODE);
		return inputNode;
	}

	// public void setApiInput(String apiInput) {
	// setAttribute("ApiInput", apiInput);
	// }

	private String getAttribute(final String attributeName) {
		return multiApiRunElement.getAttribute(attributeName);
	}

	private void setAttribute(final String attributeName, final String value) {
		multiApiRunElement.setAttribute(attributeName, value);
	}

	public static MultiApiElement[] createMultiApiElmentList(
			final NodeList multiApiNodes) {

		if (multiApiNodes == null) {
			return null;
		} else {
			final int size = multiApiNodes.getLength();

			final MultiApiElement[] elements = new MultiApiElement[size];

			for (int i = 0; i < size; i++) {
				elements[i] = new MultiApiElement(
						(Element) multiApiNodes.item(i));
			}

			return elements;
		}

	}
}
