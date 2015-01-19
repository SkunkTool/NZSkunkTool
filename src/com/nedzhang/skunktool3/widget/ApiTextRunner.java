/*
 * and open the template in the editor.
 */
package com.nedzhang.skunktool3.widget;


import com.nedzhang.skunktool3.ApplicationProperty;
import com.nedzhang.skunktool3.apiRunner.DialogMaterialManager;
import com.nedzhang.skunktool3.apiRunner.entity.ApiMaterialType;
import com.nedzhang.skunktool3.apiRunner.entity.MultiApiElement;
import com.nedzhang.skunktool3.control.ValidatedTextField;
import com.nedzhang.skunktool3.jfx.AutoCompleteComboBoxListener;
import com.nedzhang.skunktool3.jfx.SterApiDesc;
import com.nedzhang.skunktool3.widget.MaterialPanel;
import com.nedzhang.sterlingUtil.SterlingApiUtil;
import com.nedzhang.sterlingUtil.SterlingHttpTesterUtil;
import com.nedzhang.util.DesktopApi;
import com.nedzhang.util.KeyValueSet;
import com.nedzhang.util.Pair;
import com.nedzhang.util.TextFileUtil;
import com.nedzhang.util.XPathUtil;
import com.nedzhang.util.XmlUtil;

import java.awt.TextArea;
import java.awt.im.InputContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.KeyValue;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javax.swing.plaf.RootPaneUI;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * FXML Controller class
 * 
 * @author nzhang
 */
public class ApiTextRunner extends ApiRunner{


//	private static enum RunnerMode {
//		ApiTester,UserForm
//	}
	// @FXML
	// private ComboBox lstApiName;

//	@FXML
//	private TextField txtUserName;
//
//	@FXML
//	private PasswordField txtPassword;
//
//	@FXML
//	private CheckBox chkHttpClient;
//	
//	@FXML
//	private ComboBox<String> cbxHttpTesterUrl;
	
	@FXML
	private ComboBox<String> cbxApiName;

	// private AutoFillTextBox<String> txtApiName;

	@FXML
	private CheckBox chkIsService;
	
	@FXML
	private Hyperlink lnkApiDoc;



	// @FXML
	// private MaterialPanel materialPanelInput;
	@FXML
	private TableView<MultiApiElement> tblMultiApiRun;
	// @FXML
	// private TextArea txtMultiApi;

	@FXML
	private MaterialPanel mpnlInput;
	
	@FXML
	private Region tpnlInput;

	@FXML
	private MaterialPanel mpnlTemplate;

	@FXML
	private MaterialPanel mpnlResult;

	@FXML
	private MaterialPanel mpnlTransform;

	@FXML
	private MaterialPanel mpnlMultipApiInput;

	@FXML
	private MaterialPanel mpnlMultipApiResult;

//	@FXML
//	private TitledPane accordInputForm;
	
	@FXML
	private TitledPane accordInput;

	@FXML
	private TitledPane accordTemplate;

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
	private Map<String, SterApiDesc> apiList;

	@FXML
	private GridPane gridHarnessInput;
	
	// private final ApiRunnerData dataModel;

	private final String apiJavaDocBasePath;

	private EventHandler<CbxApiNameLostFocusEvent> cbxApiLostEventHandler;
	
//	private final Document testHarnessDefinition;
	
//	private final ArrayList<Control> userInputControls;

	@SuppressWarnings("unused")
	private AutoCompleteComboBoxListener<String> cbxApiNameAutoCompleteListener;

	private StringProperty userIDProperty;
	private StringProperty passwordProperty;
	private SingleSelectionModel<String> interopUrlProperty;
	private BooleanProperty isHttpClientProperty;

	
	public class CbxApiNameLostFocusEvent extends Event {

		private static final long serialVersionUID = 5517979107723107384L;

		private String text;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public CbxApiNameLostFocusEvent(
				EventType<? extends Event> paramEventType) {
			this(paramEventType, null);
		}

		public CbxApiNameLostFocusEvent(EventType<? extends Event> eventType,
				String text) {
			super(eventType);
			this.text = text;
		}

	}

//	public ApiTextRunner() {
//		this(null);
//	}
	
	public ApiTextRunner() {
		
//		testHarnessDefinition = testHarnessDoc;

		apiJavaDocBasePath = ApplicationProperty
				.get("STERLING_API_JAVADOC_URL");

//		userInputControls = new ArrayList<Control>();
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"ApiTextRunner.fxml"));
		
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		try {	
			initializeFormData();

		} catch (Exception ex) {
			Logger.getLogger(ApiTextRunner.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	
	public void setUserIDProperty(StringProperty userIDProperty) {
		this.userIDProperty = userIDProperty;
	}
	
	public void setPasswordProperty(StringProperty passwordProperty) {
		this.passwordProperty = passwordProperty;
	}
	
	public void setInteropUrlProperty(SingleSelectionModel<String> singleSelectionModel) {
		this.interopUrlProperty = singleSelectionModel;
	}
	
	public void setIsHttpClientProperty(BooleanProperty isHttpClientProperty) {
		this.isHttpClientProperty = isHttpClientProperty;
	}
//	public String getUserID() {
//		return txtUserName.getText();
//	}
//
//	public void setUserID(String userID) {
//		txtUserName.setText(userID);
//	}
//
//	public String getPassword() {
//		return txtPassword.getText();
//	}
//
//	public void setPassword(String password) {
//		txtPassword.setText(password);
//	}
//
//	public boolean isUseHttp() {
//		return chkHttpClient.isSelected();
//	}
//
//	public void setUseHttp(boolean useHttp) {
//		chkHttpClient.setSelected(useHttp);
//	}
//
//	public String getInterOpUrl() {
//		return cbxHttpTesterUrl.getValue();
//	}
//
//	public void setInterOpUrl(String interOpUrl) {
//		cbxHttpTesterUrl.setValue(interOpUrl);
//	}

	public String getApiName() {
		return cbxApiName.getValue();
	}

	private void initializeFormData() throws XPathExpressionException,
			SAXException, IOException, ParserConfigurationException {
		
//		if (testHarnessDoc != null ) {
//			
//			accordInput.setVisible(false);
//			accordInput.resize(0, 0);
//			
//			accordTemplate.setVisible(false);
//			accordTemplate.resize(0, 0);
//			
//			cbxApiName.setVisible(false);
//			chkIsService.setVisible(false);
//			lnkApiDoc.setVisible(false);
//			
//			createInputForm(gridHarnessInput, testHarnessDoc, userInputControls);
//			
//			accordionMain.setExpandedPane(accordInputForm);
//			
//		} else {
			
//			accordInputForm.setVisible(false);
	
			apiList = getApiListImp();
	
			ObservableList<String> data = FXCollections.observableArrayList();
			data.addAll(apiList.keySet());
	
			// txtApiName.setData(data);
			// txtApiName.setFilterMode(true);
	
			cbxApiName.setItems(data);
	
			cbxApiNameAutoCompleteListener = new AutoCompleteComboBoxListener<String>(
					cbxApiName);
	
			cbxApiName.getEditor().focusedProperty()
					.addListener(new ChangeListener<Boolean>() {
						@Override
						public void changed(ObservableValue<? extends Boolean> ov,
								Boolean t, Boolean t1) {
							onTxtApiNameFocusChange(ov, t, t1);
						}
					});
			
			accordionMain.setExpandedPane(accordInput);
//		}
	}


	public void setTxtApiNameLostFocusEventHandler(
			EventHandler<CbxApiNameLostFocusEvent> eventHandler) {
		this.cbxApiLostEventHandler = eventHandler;
	}

	private Map<String, SterApiDesc> getApiListImp()
			throws XPathExpressionException, SAXException, IOException,
			ParserConfigurationException {
		final InputStream apiListXmlStream = this.getClass().getClassLoader()
				.getResourceAsStream("api_list.xml");

		final Document apiListDoc = XmlUtil.getDocument(apiListXmlStream);

		Map<String, SterApiDesc> apiList = null;

		if (apiListDoc != null) {

			XPathExpression xpathExApi = XPathUtil.getXPathExpression(
					"ApiRunnerWindow->getApiListImp->GetApi", "/apiList/api");

			final NodeList apiNodes = (NodeList) xpathExApi.evaluate(
					apiListDoc, XPathConstants.NODESET);
			// final NodeList apiNodes = XPathAPI.selectNodeList(apiListDoc,
			// "/apiList/api");

			if ((apiNodes != null) && (apiNodes.getLength() > 0)) {

				apiList = new LinkedHashMap<String, SterApiDesc>(
						apiNodes.getLength());

				for (int i = 0; i < apiNodes.getLength(); i++) {

					final SterApiDesc apiDesc = new SterApiDesc();
					apiDesc.setName(XmlUtil.getAttribute(apiNodes.item(i),
							"name"));
					apiDesc.setTitle(XmlUtil.getAttribute(apiNodes.item(i),
							"title"));
					apiDesc.setDocPage(XmlUtil.getAttribute(apiNodes.item(i),
							"docPage"));

					apiList.put(apiDesc.getName(), apiDesc);
				}
			}
		}

		return apiList;

	}

	@FXML
	private void onLnkApiDocClicked(MouseEvent event) throws IOException,
			URISyntaxException {
		System.out.println("API doc clicked");

		String apiName = cbxApiName.getValue();

		if (apiList.containsKey(apiName)) {

			String docPath = apiJavaDocBasePath
					+ apiList.get(apiName).getDocPage();

			if (docPath.toLowerCase().startsWith("http://")) {
				DesktopApi.browse(new URI(docPath));
			} else {
				File htmlFile = new File(docPath);

				DesktopApi.browse(htmlFile.toURI());
			}

		}

	}

	@FXML
	private void onBtnLoginOkClicked(MouseEvent event) {
		tabOverall.getSelectionModel().selectNext();
	}

	// private void onTxtApiNameFocusChange(ObservableValue<? extends String>
	// ov, String t, String t1) {
	// String alert = "ov: " + ov + " | t: " + t + "| t1: " + t1 +
	// " | txtApiNameFocue: " + txtApiName.isFocused();
	// System.out.println("88888888888888888888888888888888");
	// System.out.println(alert);
	// System.out.println("*************93240409474094*************");
	// materialPanelInput.setContent(alert);
	// }
	private void onTxtApiNameFocusChange(ObservableValue<? extends Boolean> ov,
			Boolean focusBefore, Boolean focusNow) {

		// String alert = "focusBefore: " + focusBefore + "| focusNow: "
		// + focusNow + " | txtApiNameFocue: "
		// + cbxApiName.isFocused() + " | Windows: "
		// + this.isFocused();
		// System.out.println("88888888888888888888888888888888");
		// System.out.println(alert);
		// System.out.println("*************93240409474094*************");

		if (focusBefore && !focusNow) {
			// txtApiName just lost focus
			if (this.cbxApiLostEventHandler != null) {

				cbxApiLostEventHandler.handle(new CbxApiNameLostFocusEvent(
						new EventType<CbxApiNameLostFocusEvent>(), cbxApiName
								.getValue()));
			}
		}
	}

	@FXML
	private void onInputLoadContent(MouseEvent event) {

		String apiInput = DialogMaterialManager.showLoadMaterialDialog(
				"Load Material", new StringBuilder("Load Input Material for ")
						.append(cbxApiName.getValue()).toString(), cbxApiName
						.getValue(), ApiMaterialType.ApiInput);

		if (apiInput != null) {
			mpnlInput.setContent(apiInput);
		}
	}

	@FXML
	private void onInputSaveContent(MouseEvent event) {

		DialogMaterialManager.showSaveMaterialDialog(
				"Save Material",
				new StringBuilder("Save Input Material for ").append(
						cbxApiName.getValue()).toString(),
				cbxApiName.getValue(), ApiMaterialType.ApiInput,
				mpnlInput.getContent());
	}

	@FXML
	private void onTemplateLoadContent(MouseEvent event) {

		String outputTemplate = DialogMaterialManager.showLoadMaterialDialog(
				"Load Output Template",
				new StringBuilder("Load Output Template for ").append(
						cbxApiName.getValue()).toString(), cbxApiName
						.getValue(), ApiMaterialType.ApiOutputTemplate);

		if (outputTemplate != null) {
			mpnlTemplate.setContent(outputTemplate);
		}
	}

	@FXML
	private void onTemplateSaveContent(MouseEvent event) {

		DialogMaterialManager.showSaveMaterialDialog(
				"Save Output Template",
				new StringBuilder("Save Output Template for ").append(
						cbxApiName.getValue()).toString(),
				cbxApiName.getValue(), ApiMaterialType.ApiOutputTemplate,
				mpnlTemplate.getContent());
	}

	@FXML
	private void onResultLoadContent(MouseEvent event) {

		String apiOutput = DialogMaterialManager.showLoadMaterialDialog(
				"Load Result",
				new StringBuilder("Load Result for ").append(
						cbxApiName.getValue()).toString(),
				cbxApiName.getValue(), ApiMaterialType.ApiOutput);

		if (apiOutput != null) {
			mpnlResult.setContent(apiOutput);
		}
	}

	@FXML
	private void onResultSaveContent(MouseEvent event) {

		DialogMaterialManager.showSaveMaterialDialog(
				"Save Result",
				new StringBuilder("Save Result for ").append(
						cbxApiName.getValue()).toString(),
				cbxApiName.getValue(), ApiMaterialType.ApiOutput,
				mpnlResult.getContent());
	}

	@FXML
	private void onTransformLoadContent(MouseEvent event) {

		String outputTransformation = DialogMaterialManager
				.showLoadMaterialDialog(
						"Load Transformation",
						new StringBuilder("Load Transformation for ").append(
								cbxApiName.getValue()).toString(),
						cbxApiName.getValue(),
						ApiMaterialType.ApiOutputTransformation);

		if (outputTransformation != null) {
			mpnlTransform.setContent(outputTransformation);
		}
	}

	@FXML
	private void onTransformSaveContent(MouseEvent event) {

		DialogMaterialManager.showSaveMaterialDialog(
				"Save Transformation",
				new StringBuilder("Save Transformation for ").append(
						cbxApiName.getValue()).toString(),
				cbxApiName.getValue(), ApiMaterialType.ApiOutputTransformation,
				mpnlTransform.getContent());
	}


	private void populateMultiApiTable() throws Exception {

		// ObservableList<ObservableMap<String, String>> data =
		// FXCollections.observableArrayList();
		// Map<String, String> rowToAdd = new HashMap<>();
		// rowToAdd.put("Column X", "Column X Value 1");
		// rowToAdd.put("Column Y", "Column Y Value 2");
		//
		// ObservableMap<String, String> observableRowToAdd =
		// FXCollections.observableMap(rowToAdd);
		//
		// data.add(observableRowToAdd);

		Document multipApiDoc = XmlUtil.getDocument(mpnlMultipApiInput
				.getContent());

		XPathExpression xpath = XPathUtil.getXPathExpression(
				"ApiRunnerWindow.runTest.GetApi", "/MultiApi/API");

		NodeList apiNodes = (NodeList) xpath.evaluate(multipApiDoc,
				XPathConstants.NODESET);

		ObservableList<MultiApiElement> rowToAdd = FXCollections
				.observableArrayList(MultiApiElement
						.createMultiApiElmentList(apiNodes));

		// data.addAll(rowToAdd);

		tblMultiApiRun.getItems().clear();
		tblMultiApiRun.getItems().addAll(rowToAdd);

		// System.out.println("Set item to the table");
	}

	@FXML
	public void onBtnRunApiClicked(MouseEvent event)
			throws IllegalArgumentException, ParserConfigurationException,
			SAXException, IOException, Exception {

		String progID = programID;
		
		final String output = runApi(progID);

		mpnlResult.setContent(output);

		accordionMain.setExpandedPane(accordResult);
	}

	private String runApi(String progID) throws Exception,
			ParserConfigurationException, SAXException, IOException {
		
		String apiName = cbxApiName.getValue();
		boolean isFlow = chkIsService.isSelected();
		String input = mpnlInput.getContent();
		String outputTemplate = mpnlTemplate.getContent();

		// pnlWait.setVisible(true);
		// pnlWait.toFront();

		final String output = ApiExecutor.executeApi(interopUrlProperty.getSelectedItem(), isHttpClientProperty.get(), userIDProperty.get(), passwordProperty.get(),
				progID, apiName, isFlow, input, outputTemplate, verboseMode);
				
		return output;
	}

//	@SuppressWarnings("unchecked")
//	private Document createUserInputForm(List<Control> userInputs) throws IllegalArgumentException, ParserConfigurationException, SAXException, IOException {
//		
//		Document userInpDocument = XmlUtil.getDocument("<InputForm />");
//		
//		Node rootNode = userInpDocument.getFirstChild();
//		
//		for (Control input : userInputs) {
//			String value;
//			if (input instanceof CheckBox) {
//				value = ((CheckBox) input).isSelected() ? "Y" : "N";
//			} else if (input instanceof ComboBox<?>) {
//				
//				value = ((ComboBox<KeyValueSet<String, String>>) input).getValue().key;
//				
//			} else if (input instanceof TextField){
//				value = ((TextField)input).getText();
//			} else {
//				value = null;
//			}
//			
//			String inputId = (String) input.getUserData();
//			
//			XmlUtil.createChildTextNode(rootNode, inputId, value);
//			
//		}
//		
//		
//		return userInpDocument;
//	}

	@FXML
	public void onBtnResultTransformClicked(MouseEvent event) {
		try {
			String apiRunXml = transformImp(mpnlResult.getContent(),
					mpnlTransform.getContent());
			mpnlMultipApiInput.setContent(apiRunXml);

			tabMultiApi.getSelectionModel().select(2);

			populateMultiApiTable();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void onBtnNewResultTransformClicked(MouseEvent event) {
		mpnlTransform.setContent(null);

		try {
			String template = TextFileUtil.readResourceTextFile(
					"/MultiApiMakerTemplate.xslt", "UTF-8");
			mpnlTransform.setContent(template);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void onBtnRunMultiApiClicked(MouseEvent event)
			throws ParserConfigurationException, SAXException, IOException,
			Exception {

		String multiApiOutput = ApiExecutor.executeApi(interopUrlProperty.getSelectedItem(), isHttpClientProperty.get(), userIDProperty.get(), passwordProperty.get(),
				programID, "multiApi", false, mpnlMultipApiInput.getContent(),
				null, verboseMode);

		mpnlMultipApiResult.setContent(multiApiOutput);

		tabMultiApi.getSelectionModel().select(3);
	}


	private String transformImp(final String xmlString, final String xsltString)
			throws TransformerException {

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
