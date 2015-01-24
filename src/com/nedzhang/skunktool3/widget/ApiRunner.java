package com.nedzhang.skunktool3.widget;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.VBox;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.nedzhang.skunktool3.SkunkSetting;

public abstract class ApiRunner extends VBox {

	protected static String programID;

	protected static boolean verboseMode;

	static {
		try {

			programID = SkunkSetting.getInstance().STERLING_INTEROP_PROG_ID;

			verboseMode = SkunkSetting.getInstance().VERBOSE_MODE;

		} catch (final Throwable e) {
			e.printStackTrace();
			programID = "SKUNKTOOL3";
			verboseMode = true;
		}
	}

	public abstract void setUserIDProperty(StringProperty userIDProperty);

	public abstract void setPasswordProperty(StringProperty passwordProperty);

	public abstract void setInteropUrlProperty(
			SingleSelectionModel<String> singleSelectionModel);

	public abstract void setIsHttpClientProperty(
			BooleanProperty isHttpClientProperty);

	protected String transformImp(final String xmlString,
			final String xsltString) throws TransformerException {

		final TransformerFactory factory = TransformerFactory.newInstance();

		final Reader xsltReader = new StringReader(xsltString);

		final Source xslt = new StreamSource(xsltReader);
		final Transformer transformer = factory.newTransformer(xslt);

		final Reader xmlReader = new StringReader(xmlString);

		final Source xml = new StreamSource(xmlReader);

		final Writer resultWriter = new StringWriter();

		final Result result = new StreamResult(resultWriter);

		transformer.transform(xml, result);

		final String resultString = resultWriter.toString();

		return resultString;
	}
}
