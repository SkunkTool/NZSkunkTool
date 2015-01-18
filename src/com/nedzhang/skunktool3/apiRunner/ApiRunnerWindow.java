/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nedzhang.skunktool3.apiRunner;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.nedzhang.skunktool3.ApplicationProperty;
import com.nedzhang.skunktool3.control.DraggableTab;
import com.nedzhang.skunktool3.widget.ApiRunner;
import com.nedzhang.skunktool3.widget.ApiRunner.CbxApiNameLostFocusEvent;
import com.nedzhang.skunktool3.widget.ConnectionSetter;
import com.nedzhang.util.ResourceUtil;
import com.nedzhang.util.XmlUtil;
//import com.sun.tools.javac.util.Paths;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.stage.Window;

/**
 * FXML Controller class
 * 
 * @author nzhang
 */
public class ApiRunnerWindow extends Window implements Initializable {

	private static final int CONNECTION_MIN_HEIGHT_MINIMIZED = 22;

	private static final int CONNECTION_MIN_HEIGHT_MAXIMIZED = 87;

	private static final String PATH_TEST_HARNESS = "/testHarness";

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

	// private Tab previousTab = null;

	private DraggableTab currentTab = null;

	@Override
	public void initialize(URL paramURL, ResourceBundle paramResourceBundle) {

		tabPaneMain.getTabs().remove(tabFirstTab);
		 
		String[] httpTesterUrlList = getHttpTesterUrl();
		
		if (httpTesterUrlList != null && httpTesterUrlList.length > 0) {
//			ObservableList<String> urlListData = FXCollections
//					.observableArrayList();
//			urlListData.addAll(httpTesterUrlList);

			connectionPanel.addConnectionUrl(httpTesterUrlList);
//			cbxHttpTesterUrl.setItems(urlListData);
		}
		
		tpanConnectionSetting.expandedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				tpanConnectionSetting.setMinHeight(newValue ? CONNECTION_MIN_HEIGHT_MAXIMIZED : CONNECTION_MIN_HEIGHT_MINIMIZED);
			}
		});
		
		 try {
			loadTestHarness();
		} catch (URISyntaxException | IOException | SAXException | ParserConfigurationException e) {
			System.out.println("Failed while loading Test Harness descriptions from resource.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String[] getHttpTesterUrl() {
		String urlsInOneString = ApplicationProperty
				.get("STERLING_INTEROP_URL");

		if (urlsInOneString != null && urlsInOneString.length() > 0) {
			return urlsInOneString.split("\\s");
		} else {
			return null;
		}

	}
	
	
	private void loadTestHarness() throws URISyntaxException, IOException, SAXException, ParserConfigurationException {
		
		String[] harnessFiles = ResourceUtil.getResourceListing(this.getClass(), PATH_TEST_HARNESS);
		
		if (harnessFiles != null && harnessFiles.length > 0) {
			
			Arrays.sort(harnessFiles);
			
			for (String harnessFilePath : harnessFiles) {
				
				String resourcePath = PATH_TEST_HARNESS + "/" + harnessFilePath;
				
				loadTestHarness(resourcePath);
				
			}
			
		}
	}

	private void loadTestHarness(String resourcePath) throws SAXException, IOException, ParserConfigurationException {
		
		Document harnessDefinitionDocument = XmlUtil.getDocument(this.getClass().getResourceAsStream(resourcePath));
		
		System.out.print(XmlUtil.getXmlString(harnessDefinitionDocument));
		
		crateNewApiTab(harnessDefinitionDocument);
		
	}

	@FXML
	private void onTabAddNewSelectChanged(Event e) {

		// System.out.println("~~~~ tabPaneMain selected index: " +
		// tabPaneMain.getSelectionModel().getSelectedIndex());

		boolean isAddNewSelected = tabAddNew.isSelected();

		if (isAddNewSelected) {
			crateNewApiTab(null);
		}
	}

	private void onTabSelectChanged(Event paramT) {

		DraggableTab tabNow = (DraggableTab) tabPaneMain.getSelectionModel()
				.getSelectedItem();

		if (tabNow != currentTab) {
			// previousTab = currentTab;
			currentTab = tabNow;

		}
	}

	private void crateNewApiTab(Document harnessDefinition) {
		final DraggableTab newTabToAdd = new DraggableTab();
		newTabToAdd.setLabelText("Api");

		ApiRunner newRunner = new ApiRunner(harnessDefinition);

		newRunner
				.setTxtApiNameLostFocusEventHandler(new EventHandler<ApiRunner.CbxApiNameLostFocusEvent>() {

					@Override
					public void handle(CbxApiNameLostFocusEvent paramT) {

						String apiName = paramT.getText();
						if (apiName != null && apiName.length() > 0) {
							newTabToAdd.setLabelText(apiName);
						} else {
							newTabToAdd.setLabelText("Api");
						}
					}
				});

		newRunner.setUserIDProperty(connectionPanel.getUserIDProperty());
		newRunner.setPasswordProperty(connectionPanel.getPasswordProperty());
		newRunner.setIsHttpClientProperty(connectionPanel.getIsHttpClientProperty());
		newRunner.setInteropUrlProperty(connectionPanel.getInteropUrlProperty());
		
//		if (currentTab != null) {
//			ApiRunner currentRunner = (ApiRunner) currentTab.getUserData();
//
//			newRunner.setUserID(currentRunner.getUserID());
//			newRunner.setPassword(currentRunner.getPassword());
//			newRunner.setUseHttp(currentRunner.isUseHttp());
//			newRunner.setInterOpUrl(currentRunner.getInterOpUrl());
//
//		}

		newTabToAdd.setContent(newRunner);

		newTabToAdd.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event paramT) {
				onTabSelectChanged(paramT);
			}
		});

		newTabToAdd.setUserData(newRunner);

		tabPaneMain.getTabs()
				.add(tabPaneMain.getTabs().size() - 1, newTabToAdd);

		tabPaneMain.getSelectionModel().select(newTabToAdd);
	}
}
