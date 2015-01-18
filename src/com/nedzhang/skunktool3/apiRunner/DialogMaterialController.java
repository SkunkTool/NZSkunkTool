package com.nedzhang.skunktool3.apiRunner;

import java.io.IOException;

import com.nedzhang.skunktool3.apiRunner.FileStore.MaterialStore;
import com.nedzhang.skunktool3.apiRunner.entity.ApiMaterial;
import com.nedzhang.skunktool3.apiRunner.entity.ApiMaterialType;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

public class DialogMaterialController extends Window {

	private enum MaterialDialogMode {
		Unknown, LoadMode, SaveMode
	}

	private MaterialDialogMode mode = MaterialDialogMode.Unknown;

	private boolean okClicked = false;

	private ApiMaterialType materialType;

	private String apiName;

	private MaterialStore materialStorage;

	private String materialContent;

	private String materialToSave;

	@FXML
	private Label lblTitle;

	@FXML
	private ListView<String> lstMaterialName;

	@FXML
	private TextField txtMaterialName;

	@FXML
	private TextArea txtMaterialDescription;

	@FXML
	private TextArea txtMaterialContent;

	public String getCaption() {
		return lblTitle.getText();
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	public String getMaterialContent() {
		return materialContent;
	}

	// public void setMaterialContent(String materialContent) {
	// this.materialContent = materialContent;
	// }

	private void closeWindow() {
		Scene scene = lblTitle.getScene();
		Stage stage = (Stage) scene.getWindow();
		stage.close();
	}

	public String getApiName() {
		return apiName;
	}

	// public void setApiName(String apiName) {
	// this.apiName = apiName;
	// }

	public ApiMaterialType getMaterialType() {
		return materialType;
	}

	// public void setMaterialType(ApiMaterialType materialType) {
	// this.materialType = materialType;
	// }

	public DialogMaterialController() {
		super();

		materialStorage = new MaterialStore();

	}

	protected void onLstMaterialNameSelectionChange(
			ObservableValue<? extends String> observableValue, String oldValue,
			String newValue) {

		try {
			ApiMaterial material = materialStorage.getMaterial(apiName,
					materialType, newValue);

			txtMaterialName.setText(material.getName());

			txtMaterialDescription.setText(material.getDescription());

			txtMaterialContent.setText(material.getContent());

			this.materialContent = material.getContent();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void prepareToLoad(String caption, String apiName,
			ApiMaterialType materialType) {

		mode = MaterialDialogMode.LoadMode;

		init(caption, apiName, materialType, null);
	}

	public void prepareToSave(String caption, String apiName,
			ApiMaterialType materialType, String materialToSave) {

		mode = MaterialDialogMode.SaveMode;

		init(caption, apiName, materialType, materialToSave);
	}

	private void init(String caption, String apiName,
			ApiMaterialType materialType, String materialToSave) {
		// Reset the okCicked to false
		okClicked = false;

		lblTitle.setText(caption);

		this.apiName = apiName;

		this.materialType = materialType;

		this.materialToSave = materialToSave;

		txtMaterialName.setEditable(mode == MaterialDialogMode.SaveMode);

		txtMaterialDescription.setEditable(mode == MaterialDialogMode.SaveMode);

		txtMaterialContent.setVisible(mode == MaterialDialogMode.LoadMode);

		String[] materilaNameList = materialStorage.getMaterialList(apiName,
				materialType);

		lstMaterialName.getItems().clear();

		if (materilaNameList != null && materilaNameList.length > 0) {
			lstMaterialName.getItems().addAll(materilaNameList);

			lstMaterialName.getSelectionModel().setSelectionMode(
					SelectionMode.SINGLE);

			lstMaterialName.getSelectionModel().selectedItemProperty()
					.addListener(new ChangeListener<String>() {

						@Override
						public void changed(
								ObservableValue<? extends String> observableValue,
								String oldValue, String newValue) {
//							System.out
//									.println("((((((((lstMaterialName change!!!!)))))))))))");
							onLstMaterialNameSelectionChange(observableValue,
									oldValue, newValue);
						}
					});
		}

	}

	@FXML
	public void onBtnOkClicked(MouseEvent event) throws IOException {
		okClicked = true;

		if (mode == MaterialDialogMode.SaveMode) {
			materialStorage.saveMaterial(apiName, materialType,
					txtMaterialName.getText(),
					txtMaterialDescription.getText(), materialToSave);
		}
		closeWindow();
	}

	@FXML
	public void onBtnCancelClicked(MouseEvent event) {
		okClicked = false;
		closeWindow();
	}

}
