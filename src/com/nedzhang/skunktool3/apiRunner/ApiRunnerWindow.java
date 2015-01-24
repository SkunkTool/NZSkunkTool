/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nedzhang.skunktool3.apiRunner;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.nedzhang.skunktool3.SkunkSetting;
import com.nedzhang.skunktool3.apiRunner.FileStore.MaterialStore;
import com.nedzhang.skunktool3.apiRunner.entity.ApiMaterial;
import com.nedzhang.skunktool3.apiRunner.entity.ApiMaterialType;
import com.nedzhang.skunktool3.control.DraggableTab;
import com.nedzhang.skunktool3.widget.ApiRunner;
import com.nedzhang.skunktool3.widget.ApiTextRunner;
import com.nedzhang.skunktool3.widget.ApiTextRunner.CbxApiNameLostFocusEvent;
import com.nedzhang.skunktool3.widget.ApiUIRunner;
import com.nedzhang.skunktool3.widget.ConnectionSetter;
import com.nedzhang.util.XmlUtil;

//import com.sun.tools.javac.util.Paths;

/**
 * FXML Controller class
 * 
 * @author nzhang
 */
public class ApiRunnerWindow extends Window implements Initializable {

	private static final String HARNESS_FOLDER_NAME = "harness";

	private static final int CONNECTION_MIN_HEIGHT_MINIMIZED = 22;

	private static final int CONNECTION_MIN_HEIGHT_MAXIMIZED = 87;

	@FXML
	private TabPane tabPaneMain;

	@FXML
	private DraggableTab tabFirstTab;

	@FXML
	private DraggableTab tabAddNew;

	@FXML
	private ConnectionSetter connectionPanel;

	@FXML
	private TitledPane tpanConnectionSetting;

	@FXML
	private Button btnLoadApiForm;

	@FXML
	private Button btnLoadApiTester;

	private DraggableTab currentTab = null;

	@Override
	public void initialize(final URL paramURL,
			final ResourceBundle paramResourceBundle) {

		final String[] httpTesterUrlList = getHttpTesterUrl();

		if (httpTesterUrlList != null && httpTesterUrlList.length > 0) {
			// ObservableList<String> urlListData = FXCollections
			// .observableArrayList();
			// urlListData.addAll(httpTesterUrlList);

			connectionPanel.addConnectionUrl(httpTesterUrlList);
			// cbxHttpTesterUrl.setItems(urlListData);
		}

		tpanConnectionSetting.expandedProperty().addListener(
				new ChangeListener<Boolean>() {

					@Override
					public void changed(
							final ObservableValue<? extends Boolean> observable,
							final Boolean oldValue, final Boolean newValue) {
						tpanConnectionSetting
								.setMinHeight(newValue ? CONNECTION_MIN_HEIGHT_MAXIMIZED
										: CONNECTION_MIN_HEIGHT_MINIMIZED);
					}
				});

		if (SkunkSetting.getInstance().TESTER_MODE) {
			btnLoadApiTester.setDisable(false);
		}

		try {
			loadApiUIForm();
		} catch (IllegalArgumentException | IOException
				| ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tabPaneMain.getTabs().remove(tabFirstTab);

		tabPaneMain.getSelectionModel().select(0);

	}

	private static String[] getHttpTesterUrl() {
		final String urlsInOneString = SkunkSetting.getInstance().STERLING_INTEROP_URL;

		if (urlsInOneString != null && urlsInOneString.length() > 0) {
			return urlsInOneString.split("\\s");
		} else {
			return null;
		}

	}

	private void loadApiUIForm() throws IOException, IllegalArgumentException,
			ParserConfigurationException, SAXException {

		// String[] harnessFiles =
		// ResourceUtil.getResourceListing(this.getClass(), PATH_TEST_HARNESS);

		final String harnessNamesInOnString = SkunkSetting.getInstance().HARNESS_TO_LOAD;

		if (harnessNamesInOnString != null
				&& harnessNamesInOnString.length() > 0) {

			final String[] harnessNames = harnessNamesInOnString.split(",");

			if (harnessNames != null && harnessNames.length > 0) {

				final MaterialStore materialStore = new MaterialStore();

				for (final String harnessName : harnessNames) {

					final ApiMaterial material = materialStore.getMaterial(
							HARNESS_FOLDER_NAME, ApiMaterialType.TestHarness,
							harnessName);

					if (material != null && material.getContent() != null
							&& material.getContent().length() > 0) {
						createApiUIForm(material.getContent());
					} else {
						System.out
								.println("***** FAILED TO LOAD TEST HARNESS: ["
										+ harnessName + "]");
					}
				}
			}
		}
	}

	// private void loadApiUIForm(ApiMaterial harnessMaterial) throws
	// IllegalArgumentException, ParserConfigurationException, SAXException,
	// IOException {
	//
	// String harnessXmlString = harnessMaterial.getContent();
	//
	// Document harnessDefinitionDocument =
	// XmlUtil.getDocument(harnessXmlString);
	//
	// crateNewApiTab(harnessDefinitionDocument);
	//
	// }

	@FXML
	private void onTabAddNewSelectChanged(final Event e)
			throws IllegalArgumentException, ParserConfigurationException,
			SAXException, IOException {

		// System.out.println("~~~~ tabPaneMain selected index: " +
		// tabPaneMain.getSelectionModel().getSelectedIndex());

		final boolean isAddNewSelected = tabAddNew.isSelected();

		if (isAddNewSelected) {
			createNewApiUiForm();
			// if (SkunkSetting.getInstance().TESTER_MODE) {
			// crateNewApiTab();
			// } else {
			// createNewApiUiForm();
			// }
		}
	}

	@FXML
	/***
	 * Load API GUI Form
	 */
	private void onBtnLoadFormClicked(final MouseEvent event)
			throws IllegalArgumentException, IOException,
			ParserConfigurationException, SAXException {
		createNewApiUiForm();
	}

	@FXML
	/***
	 * Load API Tester
	 */
	private void onBtnLoadTesterClicked(final MouseEvent event) {
		crateNewApiTab();

	}

	private void createNewApiUiForm() throws IllegalArgumentException,
			ParserConfigurationException, SAXException, IOException {

		final String input = DialogMaterialManager.showLoadMaterialDialog(
				"Open Test Form", "Select the Api Test Form to open",
				HARNESS_FOLDER_NAME, ApiMaterialType.TestHarness);

		createApiUIForm(input);
	}

	private void createApiUIForm(final String input)
			throws ParserConfigurationException, SAXException, IOException {

		if (input != null && input.length() > 0) {

			final DraggableTab newTabToAdd = new DraggableTab();

			final Document harnessDefinition = XmlUtil.getDocument(input);

			final Element testHarnessElement = (Element) XmlUtil
					.getChildNodeByName(harnessDefinition, "TestHarness", false);

			String tabLabel = testHarnessElement.getAttribute("Label");

			tabLabel = (tabLabel == null || tabLabel.length() == 0) ? "Api Input Form"
					: tabLabel;

			newTabToAdd.setLabelText(tabLabel);

			String tabName = testHarnessElement.getAttribute("Name");

			tabName = (tabName == null || tabName.length() == 0) ? "api_form"
					: tabName;

			final ApiRunner newRunner = new ApiUIRunner(tabName,
					harnessDefinition);

			// newRunner
			// .setTxtApiNameLostFocusEventHandler(new
			// EventHandler<ApiUIForm.CbxApiNameLostFocusEvent>() {
			//
			// @Override
			// public void handle(CbxApiNameLostFocusEvent paramT) {
			//
			// String apiName = paramT.getText();
			// if (apiName != null && apiName.length() > 0) {
			// newTabToAdd.setLabelText(apiName);
			// } else {
			// newTabToAdd.setLabelText("Api");
			// }
			// }
			//
			// });

			newRunner.setUserIDProperty(connectionPanel.getUserIDProperty());
			newRunner
					.setPasswordProperty(connectionPanel.getPasswordProperty());
			newRunner.setIsHttpClientProperty(connectionPanel
					.getIsHttpClientProperty());
			newRunner.setInteropUrlProperty(connectionPanel
					.getInteropUrlProperty());

			// if (currentTab != null) {
			// ApiRunner currentRunner = (ApiRunner) currentTab.getUserData();
			//
			// newRunner.setUserID(currentRunner.getUserID());
			// newRunner.setPassword(currentRunner.getPassword());
			// newRunner.setUseHttp(currentRunner.isUseHttp());
			// newRunner.setInterOpUrl(currentRunner.getInterOpUrl());
			//
			// }

			newTabToAdd.setContent(newRunner);

			newTabToAdd.setOnSelectionChanged(new EventHandler<Event>() {

				@Override
				public void handle(final Event paramT) {
					onTabSelectChanged(paramT);
				}
			});

			newTabToAdd.setUserData(newRunner);

			tabPaneMain.getTabs().add(tabPaneMain.getTabs().size() - 1,
					newTabToAdd);

			tabPaneMain.getSelectionModel().select(newTabToAdd);

		}
	}

	private void onTabSelectChanged(final Event paramT) {

		final DraggableTab tabNow = (DraggableTab) tabPaneMain
				.getSelectionModel().getSelectedItem();

		if (tabNow != currentTab) {
			// previousTab = currentTab;
			currentTab = tabNow;

		}
	}

	private void crateNewApiTab() {
		final DraggableTab newTabToAdd = new DraggableTab();
		newTabToAdd.setLabelText("Api");

		final ApiTextRunner newRunner = new ApiTextRunner();

		newRunner
				.setTxtApiNameLostFocusEventHandler(new EventHandler<ApiTextRunner.CbxApiNameLostFocusEvent>() {

					@Override
					public void handle(final CbxApiNameLostFocusEvent paramT) {

						final String apiName = paramT.getText();
						if (apiName != null && apiName.length() > 0) {
							newTabToAdd.setLabelText(apiName);
						} else {
							newTabToAdd.setLabelText("Api");
						}
					}
				});

		newRunner.setUserIDProperty(connectionPanel.getUserIDProperty());
		newRunner.setPasswordProperty(connectionPanel.getPasswordProperty());
		newRunner.setIsHttpClientProperty(connectionPanel
				.getIsHttpClientProperty());
		newRunner
				.setInteropUrlProperty(connectionPanel.getInteropUrlProperty());

		// if (currentTab != null) {
		// ApiRunner currentRunner = (ApiRunner) currentTab.getUserData();
		//
		// newRunner.setUserID(currentRunner.getUserID());
		// newRunner.setPassword(currentRunner.getPassword());
		// newRunner.setUseHttp(currentRunner.isUseHttp());
		// newRunner.setInterOpUrl(currentRunner.getInterOpUrl());
		//
		// }

		newTabToAdd.setContent(newRunner);

		newTabToAdd.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(final Event paramT) {
				onTabSelectChanged(paramT);
			}
		});

		newTabToAdd.setUserData(newRunner);

		tabPaneMain.getTabs()
				.add(tabPaneMain.getTabs().size() - 1, newTabToAdd);

		tabPaneMain.getSelectionModel().select(newTabToAdd);
	}
}
