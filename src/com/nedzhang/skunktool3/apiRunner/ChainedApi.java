package com.nedzhang.skunktool3.apiRunner;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.nedzhang.util.NumberUtil;
import com.nedzhang.util.XPathUtil;

public class ChainedApi implements Comparable<ChainedApi> {

	public final String name;
	public final String flowName;
	public final int seq;

	public final Node inputUI;
	public final Node inputTransformation;
	public final Node outputTemplate;
	public final Node outputTransformation;

	public ChainedApi(final Element apiDefintion)
			throws XPathExpressionException {

		name = apiDefintion.getAttribute("Name");
		flowName = apiDefintion.getAttribute("FlowName");
		seq = NumberUtil.parseInt(apiDefintion.getAttribute("Seq"));

		inputUI = XPathUtil.selectSingleNode("ChainedApi", "Input/InputUI", apiDefintion);
		
		final Node inputTransformNode = XPathUtil.selectSingleNode(
				"ChainedApi", "Input/InputTransform/*", apiDefintion);

		if (inputTransformNode != null
				&& inputTransformNode.getNodeName() != null
				&& inputTransformNode.getNodeName().contains("stylesheet")) {
			// We have an input transformation stylesheet
			inputTransformation = inputTransformNode;
		} else {
			inputTransformation = null;
		}

		outputTemplate = XPathUtil.selectSingleNode("ChainedApi",
				"Output/Template/*", apiDefintion);

		final Node outputTransformNode = XPathUtil.selectSingleNode(
				"ChainedApi", "Output/OutputTransform/*", apiDefintion);

		if (outputTransformNode != null
				&& outputTransformNode.getNodeName() != null
				&& outputTransformNode.getNodeName().contains("stylesheet")) {
			// We have an input transformation stylesheet
			outputTransformation = outputTransformNode;
		} else {
			outputTransformation = null;
		}
	}

	@Override
	public int compareTo(final ChainedApi o) {
		// TODO Auto-generated method stub
		return Integer.compare(o.seq, seq);
	}

}
