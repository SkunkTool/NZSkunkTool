/*
 * and open the template in the editor.
 */
package com.nedzhang.skunktool3.widget;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nedzhang.skunktool3.SkunkSetting;
import com.nedzhang.skunktool3.apiRunner.DialogMaterialManager;
import com.nedzhang.skunktool3.apiRunner.entity.ApiMaterialType;
import com.nedzhang.skunktool3.apiRunner.entity.MultiApiElement;
import com.nedzhang.skunktool3.jfx.AutoCompleteComboBoxListener;
import com.nedzhang.skunktool3.jfx.SterApiDesc;
import com.nedzhang.util.DesktopApi;
import com.nedzhang.util.TextFileUtil;
import com.nedzhang.util.XPathUtil;
import com.nedzhang.util.XmlUtil;

/**
 * FXML Controller class
 * 
 * @author nzhang
 */
public class ApiTextRunner extends ApiRunner {

	// private static enum RunnerMode {
	// ApiTester,UserForm
	// }
	// @FXML
	// private ComboBox lstApiName;

	// @FXML
	// private TextField txtUserName;
	//
	// @FXML
	// private PasswordField txtPassword;
	//
	// @FXML
	// private CheckBox chkHttpClient;
	//
	// @FXML
	// private ComboBox<String> cbxHttpTesterUrl;

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

	// @FXML
	// private TitledPane accordInputForm;

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

	// private final String apiJavaDocBasePath;

	private EventHandler<CbxApiNameLostFocusEvent> cbxApiLostEventHandler;

	// private final Document testHarnessDefinition;

	// private final ArrayList<Control> userInputControls;

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

		public void setText(final String text) {
			this.text = text;
		}

		public CbxApiNameLostFocusEvent(
				final EventType<? extends Event> paramEventType) {
			this(paramEventType, null);
		}

		public CbxApiNameLostFocusEvent(
				final EventType<? extends Event> eventType, final String text) {
			super(eventType);
			this.text = text;
		}

	}

	// public ApiTextRunner() {
	// this(null);
	// }

	public ApiTextRunner() {

		// testHarnessDefinition = testHarnessDoc;

		// apiJavaDocBasePath =
		// SkunkSetting.getInstance().STERLING_API_JAVADOC_URL;

		// userInputControls = new ArrayList<Control>();

		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"ApiTextRunner.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}

		try {
			initializeFormData();

		} catch (final Exception ex) {
			Logger.getLogger(ApiTextRunner.class.getName()).log(Level.SEVERE,
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

	// public String getUserID() {
	// return txtUserName.getText();
	// }
	//
	// public void setUserID(String userID) {
	// txtUserName.setText(userID);
	// }
	//
	// public String getPassword() {
	// return txtPassword.getText();
	// }
	//
	// public void setPassword(String password) {
	// txtPassword.setText(password);
	// }
	//
	// public boolean isUseHttp() {
	// return chkHttpClient.isSelected();
	// }
	//
	// public void setUseHttp(boolean useHttp) {
	// chkHttpClient.setSelected(useHttp);
	// }
	//
	// public String getInterOpUrl() {
	// return cbxHttpTesterUrl.getValue();
	// }
	//
	// public void setInterOpUrl(String interOpUrl) {
	// cbxHttpTesterUrl.setValue(interOpUrl);
	// }

	public String getApiName() {
		return cbxApiName.getValue();
	}

	private void initializeFormData() throws XPathExpressionException,
			SAXException, IOException, ParserConfigurationException {

		// if (testHarnessDoc != null ) {
		//
		// accordInput.setVisible(false);
		// accordInput.resize(0, 0);
		//
		// accordTemplate.setVisible(false);
		// accordTemplate.resize(0, 0);
		//
		// cbxApiName.setVisible(false);
		// chkIsService.setVisible(false);
		// lnkApiDoc.setVisible(false);
		//
		// createInputForm(gridHarnessInput, testHarnessDoc, userInputControls);
		//
		// accordionMain.setExpandedPane(accordInputForm);
		//
		// } else {

		// accordInputForm.setVisible(false);

		apiList = getApiListImp();

		final ObservableList<String> data = FXCollections.observableArrayList();
		data.addAll(apiList.keySet());

		// txtApiName.setData(data);
		// txtApiName.setFilterMode(true);

		cbxApiName.setItems(data);

		cbxApiNameAutoCompleteListener = new AutoCompleteComboBoxListener<String>(
				cbxApiName);

		cbxApiName.getEditor().focusedProperty()
				.addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(
							final ObservableValue<? extends Boolean> ov,
							final Boolean t, final Boolean t1) {
						onTxtApiNameFocusChange(ov, t, t1);
					}
				});

		accordionMain.setExpandedPane(accordInput);
		// }
	}

	public void setTxtApiNameLostFocusEventHandler(
			final EventHandler<CbxApiNameLostFocusEvent> eventHandler) {
		cbxApiLostEventHandler = eventHandler;
	}

	private Map<String, SterApiDesc> getApiListImp()
			throws XPathExpressionException, SAXException, IOException,
			ParserConfigurationException {
		final InputStream apiListXmlStream = this.getClass().getClassLoader()
				.getResourceAsStream("api_list.xml");

		final Document apiListDoc = XmlUtil.getDocument(apiListXmlStream);

		Map<String, SterApiDesc> apiList = null;

		if (apiListDoc != null) {

			final XPathExpression xpathExApi = XPathUtil.getXPathExpression(
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
	private void onLnkApiDocClicked(final MouseEvent event) throws IOException,
			URISyntaxException {
		System.out.println("API doc clicked");

		final String apiName = cbxApiName.getValue();

		if (apiList.containsKey(apiName)) {

			final String docPath = SkunkSetting.getInstance().STERLING_API_JAVADOC_URL
					+ apiList.get(apiName).getDocPage();

			if (docPath.toLowerCase().startsWith("http://")) {
				DesktopApi.browse(new URI(docPath));
			} else {
				final File htmlFile = new File(docPath);

				DesktopApi.browse(htmlFile.toURI());
			}

		}

	}

	@FXML
	private void onBtnLoginOkClicked(final MouseEvent event) {
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
	private void onTxtApiNameFocusChange(
			final ObservableValue<? extends Boolean> ov,
			final Boolean focusBefore, final Boolean focusNow) {

		// String alert = "focusBefore: " + focusBefore + "| focusNow: "
		// + focusNow + " | txtApiNameFocue: "
		// + cbxApiName.isFocused() + " | Windows: "
		// + this.isFocused();
		// System.out.println("88888888888888888888888888888888");
		// System.out.println(alert);
		// System.out.println("*************93240409474094*************");

		if (focusBefore && !focusNow) {
			// txtApiName just lost focus
			if (cbxApiLostEventHandler != null) {

				cbxApiLostEventHandler.handle(new CbxApiNameLostFocusEvent(
						new EventType<CbxApiNameLostFocusEvent>(), cbxApiName
								.getValue()));
			}
		}
	}

	@FXML
	private void onInputLoadContent(final MouseEvent event) {

		final String apiInput = DialogMaterialManager.showLoadMaterialDialog(
				"Load Material", new StringBuilder("Load Input Material for ")
						.append(cbxApiName.getValue()).toString(), cbxApiName
						.getValue(), ApiMaterialType.ApiInput);

		if (apiInput != null) {
			mpnlInput.setContent(apiInput);
		}
	}

	@FXML
	private void onInputSaveContent(final MouseEvent event) {

		DialogMaterialManager.showSaveMaterialDialog(
				"Save Material",
				new StringBuilder("Save Input Material for ").append(
						cbxApiName.getValue()).toString(),
				cbxApiName.getValue(), ApiMaterialType.ApiInput,
				mpnlInput.getContent());
	}

	@FXML
	private void onTemplateLoadContent(final MouseEvent event) {

		final String outputTemplate = DialogMaterialManager
				.showLoadMaterialDialog(
						"Load Output Template",
						new StringBuilder("Load Output Template for ").append(
								cbxApiName.getValue()).toString(),
						cbxApiName.getValue(),
						ApiMaterialType.ApiOutputTemplate);

		if (outputTemplate != null) {
			mpnlTemplate.setContent(outputTemplate);
		}
	}

	@FXML
	private void onTemplateSaveContent(final MouseEvent event) {

		DialogMaterialManager.showSaveMaterialDialog(
				"Save Output Template",
				new StringBuilder("Save Output Template for ").append(
						cbxApiName.getValue()).toString(),
				cbxApiName.getValue(), ApiMaterialType.ApiOutputTemplate,
				mpnlTemplate.getContent());
	}

	@FXML
	private void onResultLoadContent(final MouseEvent event) {

		final String apiOutput = DialogMaterialManager.showLoadMaterialDialog(
				"Load Result",
				new StringBuilder("Load Result for ").append(
						cbxApiName.getValue()).toString(),
				cbxApiName.getValue(), ApiMaterialType.ApiOutput);

		if (apiOutput != null) {
			mpnlResult.setContent(apiOutput);
		}
	}

	@FXML
	private void onResultSaveContent(final MouseEvent event) {

		DialogMaterialManager.showSaveMaterialDialog(
				"Save Result",
				new StringBuilder("Save Result for ").append(
						cbxApiName.getValue()).toString(),
				cbxApiName.getValue(), ApiMaterialType.ApiOutput,
				mpnlResult.getContent());
	}

	@FXML
	private void onTransformLoadContent(final MouseEvent event) {

		final String outputTransformation = DialogMaterialManager
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
	private void onTransformSaveContent(final MouseEvent event) {

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

		final String progID = programID;

		final String output = runApi(progID);

		mpnlResult.setContent(output);

		accordionMain.setExpandedPane(accordResult);
	}

	private String runApi(final String progID) throws Exception,
			ParserConfigurationException, SAXException, IOException {

		final String apiName = cbxApiName.getValue();
		final boolean isFlow = chkIsService.isSelected();
		final String input = mpnlInput.getContent();
		final String outputTemplate = mpnlTemplate.getContent();

		// pnlWait.setVisible(true);
		// pnlWait.toFront();

		final String output = ApiExecutor.executeApi(
				interopUrlProperty.getSelectedItem(),
				isHttpClientProperty.get(), userIDProperty.get(),
				passwordProperty.get(), progID, apiName, isFlow, input,
				outputTemplate, verboseMode);

		return output;
	}

	// @SuppressWarnings("unchecked")
	// private Document createUserInputForm(List<Control> userInputs) throws
	// IllegalArgumentException, ParserConfigurationException, SAXException,
	// IOException {
	//
	// Document userInpDocument = XmlUtil.getDocument("<InputForm />");
	//
	// Node rootNode = userInpDocument.getFirstChild();
	//
	// for (Control input : userInputs) {
	// String value;
	// if (input instanceof CheckBox) {
	// value = ((CheckBox) input).isSelected() ? "Y" : "N";
	// } else if (input instanceof ComboBox<?>) {
	//
	// value = ((ComboBox<KeyValueSet<String, String>>) input).getValue().key;
	//
	// } else if (input instanceof TextField){
	// value = ((TextField)input).getText();
	// } else {
	// value = null;
	// }
	//
	// String inputId = (String) input.getUserData();
	//
	// XmlUtil.createChildTextNode(rootNode, inputId, value);
	//
	// }
	//
	//
	// return userInpDocument;
	// }

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
