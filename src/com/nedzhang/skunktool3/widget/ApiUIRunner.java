/*
 * and open the template in the editor.
 */
package com.nedzhang.skunktool3.widget;

import java.awt.TextArea;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nedzhang.skunktool3.SkunkUtil;
import com.nedzhang.skunktool3.apiRunner.ChainedApi;
import com.nedzhang.skunktool3.apiRunner.DialogMaterialManager;
import com.nedzhang.skunktool3.apiRunner.entity.ApiMaterialType;
import com.nedzhang.skunktool3.apiRunner.entity.MultiApiElement;
import com.nedzhang.util.KeyValueSet;
import com.nedzhang.util.NumberUtil;
import com.nedzhang.util.TextFileUtil;
import com.nedzhang.util.XPathUtil;
import com.nedzhang.util.XmlUtil;

import eu.schudt.javafx.controls.calendar.DatePicker;

/**
 * Api UI Form Runner. UI to execute the UI Form of Api.
 * 
 * @author nzhang
 */
public class ApiUIRunner extends ApiRunner {

	@FXML
	private TableView<MultiApiElement> tblMultiApiRun;

	@FXML
	private TextArea txtMultiApi;

	@FXML
	private MaterialPanel mpnlResult;

	@FXML
	private MaterialPanel mpnlTransform;

	@FXML
	private MaterialPanel mpnlMultipApiInput;

	@FXML
	private MaterialPanel mpnlMultipApiResult;

	@FXML
	private TitledPane accordInputForm;

	@FXML
	private TitledPane accordResult;

	@FXML
	private TitledPane accordTransform;

	@FXML
	private TitledPane accordMultiApiRun;

	@FXML
	private Accordion accordionMain;

	@FXML
	private TabPane tabOverall;

	@FXML
	private TabPane tabMultiApi;

	@FXML
	private XmlViewer xmlViewerResult;

	@FXML
	private GridPane gridHarnessInput;

//	private final Document testHarnessDefinition;

	private final ArrayList<Node> userInputControls;

	private StringProperty userIDProperty;
	private StringProperty passwordProperty;
	private SingleSelectionModel<String> interopUrlProperty;
	private BooleanProperty isHttpClientProperty;

	private final String harnessName;
	
	private List<ChainedApi> apiList;

	private static SimpleDateFormat yyyymmdd_HHMMSS_formater = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss");

	public ApiUIRunner() {
		this(null, null);
	}

	public ApiUIRunner(final String harnessName, final Document testHarnessDoc) {

		this.harnessName = harnessName;

//		testHarnessDefinition = testHarnessDoc;

		userInputControls = new ArrayList<Node>();

		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"ApiUIRunner.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}

		try {

			initializeFormData(testHarnessDoc);

		} catch (final Exception ex) {
			Logger.getLogger(ApiUIRunner.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	@Override
	public void setUserIDProperty(final StringProperty userIDProperty) {
		this.userIDProperty = userIDProperty;
	}

	@Override
	public void setPasswordProperty(final StringProperty passwordProperty) {
		this.passwordProperty = passwordProperty;
	}

	@Override
	public void setInteropUrlProperty(
			final SingleSelectionModel<String> singleSelectionModel) {
		interopUrlProperty = singleSelectionModel;
	}

	@Override
	public void setIsHttpClientProperty(
			final BooleanProperty isHttpClientProperty) {
		this.isHttpClientProperty = isHttpClientProperty;
	}

	private void initializeFormData(final Document testHarnessDoc)
			throws XPathExpressionException, SAXException, IOException,
			ParserConfigurationException, ParseException {

		if (testHarnessDoc != null) {
			final NodeList chainedApiNodes = XPathUtil.selectNode("ApiUIRunner.initializeFormData",
					"/TestHarness/Api", testHarnessDoc);
	
			apiList = new ArrayList<>();
			
			if (chainedApiNodes != null && chainedApiNodes.getLength() > 0) {
				
				for (int i=0; i<chainedApiNodes.getLength(); i++) {
					org.w3c.dom.Node apiNode = chainedApiNodes.item(i);
					
					if (apiNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
						ChainedApi api = new ChainedApi((Element) apiNode);
						if (api.name != null && api.name.length() > 0) {
							apiList.add(api);
						}
					}
				}
				
				if (apiList.size() > 0) {
					Collections.sort(apiList);
					createInputForm(gridHarnessInput, apiList.get(0), userInputControls);
				}
			}
		}

		accordionMain.setExpandedPane(accordInputForm);

	}

	private static void createInputForm(final GridPane panel,
			final ChainedApi apiDefintion, final List<Node> inputControlList)
			throws XPathExpressionException, ParseException {

		panel.setPadding(new Insets(8));

//		final XPathExpression inputFieldExpression = XPathUtil
//				.getXPathExpression("ApiRunner.creaeInputForm",
//						"/TestHarness/Api/Input/InputForm/Field");
//
		final XPathExpression itemExpress = XPathUtil.getXPathExpression(
				"ApiRunner.creaeInputForm", "Item");

		final NodeList inputFieldNodes = XPathUtil.selectNode("ApiRunner.createInputForm", "Field", apiDefintion.inputUI);

		final Insets cellMargin = new Insets(4);

		if (inputFieldNodes != null && inputFieldNodes.getLength() > 0) {

			for (int i = 0; i < inputFieldNodes.getLength(); i++) {

				// System.out.println(XmlUtil.getXmlString(inputFieldNodes.item(i)));

				final Element fieldElement = (Element) inputFieldNodes.item(i);

				// Create label
				final Label label = new Label(
						fieldElement.getAttribute("Label"));

				final Label hint = new Label(fieldElement.getAttribute("Hint"));

				final String fieldType = fieldElement.getAttribute("Type");

				final String defaultValue = fieldElement
						.getAttribute("Default");

				Node inputControl;

				if ("List".equalsIgnoreCase(fieldType)) {

					KeyValueSet<String, String> defaultItem = null;

					final NodeList selectionNodes = (NodeList) itemExpress
							.evaluate(fieldElement, XPathConstants.NODESET);

					if (selectionNodes != null
							&& selectionNodes.getLength() > 0) {

						final ObservableList<KeyValueSet<String, String>> options = FXCollections
								.observableArrayList();

						for (int j = 0; j < selectionNodes.getLength(); j++) {

							final Element selectionElement = (Element) selectionNodes
									.item(j);

							final String itemValue = selectionElement
									.getAttribute("Value");
							final String itemLabel = selectionElement
									.getAttribute("Label");

							final KeyValueSet<String, String> selectionItem = new KeyValueSet<String, String>(
									itemValue, itemLabel);
							options.add(selectionItem);

							if (defaultValue != null && itemValue != null
									&& defaultValue.equals(itemValue)) {
								defaultItem = selectionItem;
							}

						}

						final ComboBox<KeyValueSet<String, String>> selectionBox = new ComboBox<KeyValueSet<String, String>>(
								options);

						if (defaultItem != null) {
							selectionBox.setValue(defaultItem);
						}

						selectionBox.setEditable(false);
						inputControl = selectionBox;

					} else {
						inputControl = new ComboBox<String>();
					}

				} else if ("Boolean".equalsIgnoreCase(fieldType)) {
					final CheckBox inputCheckBox = new CheckBox();

					inputCheckBox.setSelected(SkunkUtil
							.parseBoolean(defaultValue));

					inputControl = inputCheckBox;
				} else if ("Date".equalsIgnoreCase(fieldType)) {
					final DatePicker datePicker = new DatePicker();

					if (defaultValue != null && defaultValue.length() > 0) {

						if (NumberUtil.isNumeric(defaultValue)) {

							final int offset = NumberUtil
									.parseInt(defaultValue);
							final Calendar now = Calendar.getInstance();

							now.add(Calendar.DAY_OF_MONTH, offset);
							datePicker.setSelectedDate(now.getTime());
						} else {
							
							final DateFormat dateFormatter = new SimpleDateFormat("mm/dd/yy");
							
							datePicker.setSelectedDate(dateFormatter
									.parse(defaultValue));
						}

					}

					inputControl = datePicker;

				} else {
					inputControl = new TextField(defaultValue);
				}

				inputControl.setUserData(fieldElement.getAttribute("Name"));

				GridPane.setConstraints(label, 1, i + 1, 1, 1, HPos.RIGHT,
						VPos.CENTER, Priority.NEVER, Priority.NEVER, cellMargin);
				GridPane.setConstraints(inputControl, 2, i + 1, 1, 1,
						HPos.LEFT, VPos.CENTER, Priority.ALWAYS,
						Priority.NEVER, cellMargin);
				GridPane.setConstraints(hint, 3, i + 1, 1, 1, HPos.LEFT,
						VPos.CENTER, Priority.NEVER, Priority.NEVER, cellMargin);

				panel.getChildren().addAll(label, inputControl, hint);

				inputControlList.add(inputControl);
			}
		}

	}

	@FXML
	private void onResultLoadContent(final MouseEvent event)
			throws ParserConfigurationException, SAXException, IOException {

		final String apiOutput = DialogMaterialManager.showLoadMaterialDialog(
				"Load Result",
				new StringBuilder("Load Result for ").append(harnessName)
						.toString(), harnessName, ApiMaterialType.ApiOutput);

		if (apiOutput != null) {
			setResult(apiOutput);
		}
	}

	@FXML
	private void onResultSaveContent(final MouseEvent event) {

		DialogMaterialManager.showSaveMaterialDialog("Save Result",
				new StringBuilder("Save Result for ").append(harnessName)
						.toString(), harnessName, ApiMaterialType.ApiOutput,
				mpnlResult.getContent());
	}

	@FXML
	private void onTransformLoadContent(final MouseEvent event) {

		final String outputTransformation = DialogMaterialManager
				.showLoadMaterialDialog(
						"Load Transformation",
						new StringBuilder("Load Transformation for ").append(
								harnessName).toString(), harnessName,
						ApiMaterialType.ApiOutputTransformation);

		if (outputTransformation != null) {
			mpnlTransform.setContent(outputTransformation);
		}
	}

	@FXML
	private void onTransformSaveContent(final MouseEvent event) {

		DialogMaterialManager.showSaveMaterialDialog(
				"Save Transformation",
				new StringBuilder("Save Transformation for ").append(
						harnessName).toString(), harnessName,
				ApiMaterialType.ApiOutputTransformation,
				mpnlTransform.getContent());
	}

	private void populateMultiApiTable() throws Exception {

		final Document multipApiDoc = XmlUtil.getDocument(mpnlMultipApiInput
				.getContent());

		final XPathExpression xpath = XPathUtil.getXPathExpression(
				"ApiRunnerWindow.runTest.GetApi", "/MultiApi/API");

		final NodeList apiNodes = (NodeList) xpath.evaluate(multipApiDoc,
				XPathConstants.NODESET);

		final ObservableList<MultiApiElement> rowToAdd = FXCollections
				.observableArrayList(MultiApiElement
						.createMultiApiElmentList(apiNodes));

		// data.addAll(rowToAdd);

		tblMultiApiRun.getItems().clear();
		tblMultiApiRun.getItems().addAll(rowToAdd);

		// System.out.println("Set item to the table");
	}

	@FXML
	public void onBtnRunApiClicked(final MouseEvent event)
			throws IllegalArgumentException, ParserConfigurationException,
			SAXException, IOException, Exception {


		final String output = executeChainedApi();

		setResult(output);

		accordionMain.setExpandedPane(accordResult);

	}

//	private String runApi() throws Exception,
//			ParserConfigurationException, SAXException, IOException {
//
//		final Document userInpDocument = createUserInputForm(userInputControls);
//
//		String output = ApiExecutor.executeApi(
//				interopUrlProperty.getSelectedItem(),
//				isHttpClientProperty.get(), userIDProperty.get(),
//				passwordProperty.get(), programID, testHarnessDefinition,
//				userInpDocument, verboseMode);
//
//		output = executeChainedApi(testHarnessDefinition, output);
//		
//		final org.w3c.dom.Node outputXslNode = XPathUtil.selectSingleNode("ApiUIRunner.runApi",
//						"/TestHarness/Output/Transformation/*", testHarnessDefinition);
//
//		if (outputXslNode != null && outputXslNode.getNodeName() != null
//				&& outputXslNode.getNodeName().contains("stylesheet")) {
//			// We have an output transformation stylesheet
//			output = transformImp(output, XmlUtil.getXmlString(outputXslNode));
//
//		}
//
//		return output;
//
//	}

	private String executeChainedApi() throws IllegalArgumentException, TransformerException, ParserConfigurationException, SAXException, IOException, Exception {
		

		
		if (apiList != null && apiList.size() > 0) {
			
//			Collections.sort(apiList);
			
			final Document userInpDocument = createUserInputForm(userInputControls);
			
			// The user input is the first input
			String output = XmlUtil.getXmlString(userInpDocument);
			
			for (ChainedApi chainedApi : apiList) {
				output = ApiExecutor.executeApi(
						interopUrlProperty.getSelectedItem(),
						isHttpClientProperty.get(), userIDProperty.get(),
						passwordProperty.get(), programID, chainedApi.name, chainedApi.flowName, XmlUtil.getDocument(output), chainedApi.inputTransformation, 
						chainedApi.outputTemplate, chainedApi.outputTransformation, verboseMode);
			}
			
			
			return output;
		} else {
			return null;
		}
		
	}

	private void setResult(final String output)
			throws ParserConfigurationException, SAXException, IOException {
		mpnlResult.setContent(output);

		if (output != null && output.length() > 0) {
			final Document outputDoc = XmlUtil.getDocument(output);
			xmlViewerResult.loadXmlData(outputDoc);
		}
	}

	@SuppressWarnings("unchecked")
	private Document createUserInputForm(final List<Node> userInputs)
			throws IllegalArgumentException, ParserConfigurationException,
			SAXException, IOException {

		final Document userInpDocument = XmlUtil.getDocument("<InputForm />");

		final org.w3c.dom.Node rootNode = userInpDocument.getFirstChild();

		for (final Node input : userInputs) {
			String value;
			if (input instanceof CheckBox) {
				value = ((CheckBox) input).isSelected() ? "Y" : "N";
			} else if (input instanceof ComboBox<?>) {
				value = ((ComboBox<KeyValueSet<String, String>>) input)
						.getValue().key;
			} else if (input instanceof DatePicker) {
				value = yyyymmdd_HHMMSS_formater.format(((DatePicker) input)
						.getSelectedDate());
			} else if (input instanceof TextField) {
				value = ((TextField) input).getText();
			} else {

				value = null;
			}

			final String inputId = (String) input.getUserData();

			XmlUtil.createChildTextNode(rootNode, inputId, value);

		}

		return userInpDocument;
	}

	@FXML
	public void onBtnResultTransformClicked(final MouseEvent event) {
		try {
			final String apiRunXml = transformImp(mpnlResult.getContent(),
					mpnlTransform.getContent());
			mpnlMultipApiInput.setContent(apiRunXml);

			tabMultiApi.getSelectionModel().select(2);

			populateMultiApiTable();
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void onBtnNewResultTransformClicked(final MouseEvent event) {
		mpnlTransform.setContent(null);

		try {
			final String template = TextFileUtil.readResourceTextFile(
					"/MultiApiMakerTemplate.xslt", "UTF-8");
			mpnlTransform.setContent(template);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void onBtnRunMultiApiClicked(final MouseEvent event)
			throws ParserConfigurationException, SAXException, IOException,
			Exception {

		final String multiApiOutput = ApiExecutor.executeApi(
				interopUrlProperty.getSelectedItem(),
				isHttpClientProperty.get(), userIDProperty.get(),
				passwordProperty.get(), programID, "multiApi", false,
				mpnlMultipApiInput.getContent(), null, verboseMode);

		mpnlMultipApiResult.setContent(multiApiOutput);

		tabMultiApi.getSelectionModel().select(3);
	}

}
