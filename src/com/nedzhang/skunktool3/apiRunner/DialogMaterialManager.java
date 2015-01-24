package com.nedzhang.skunktool3.apiRunner;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.nedzhang.skunktool3.apiRunner.entity.ApiMaterialType;

public class DialogMaterialManager {

	final private Stage dialogStage;

	final private DialogMaterialController controller;

	// final private static DialogMaterialManager instance = new
	// DialogMaterialManager();

	// final private MaterialStore materialStorage;

	private DialogMaterialManager() {

		final FXMLLoader fxmlLoader = new FXMLLoader(
				this.getClass()
						.getResource(
								"/com/nedzhang/skunktool3/apiRunner/DialogMaterial.fxml"));

		Scene scene = null;

		try {
			scene = new Scene((Parent) fxmlLoader.load());
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		controller = fxmlLoader.getController();

		dialogStage = new Stage();
		// dialogStage.setTitle("Material");

		dialogStage.initModality(Modality.WINDOW_MODAL);

		dialogStage.setScene(scene);

		// materialStorage = new MaterialStore();
	}

	public static boolean showSaveMaterialDialog(final String title,
			final String caption, final String apiName,
			final ApiMaterialType materialType, final String materialContent) {

		final DialogMaterialManager instance = new DialogMaterialManager();

		instance.dialogStage.setTitle(title);

		// instance.controller.setCaption(caption);
		//
		// instance.controller.setApiName(apiName);
		//
		// instance.controller.setMaterialContent(materialContent);
		//
		// instance.controller.setMaterialType(materialType);

		instance.controller.prepareToSave(caption, apiName, materialType,
				materialContent);

		instance.dialogStage.showAndWait();

		return instance.controller.isOkClicked();
	}

	public static String showLoadMaterialDialog(final String title,
			final String caption, final String apiName,
			final ApiMaterialType materialType) {

		final DialogMaterialManager instance = new DialogMaterialManager();

		instance.dialogStage.setTitle(title);

		// instance.controller.setCaption(caption);
		//
		// instance.controller.setApiName(apiName);
		//
		// instance.controller.setMaterialType(materialType);

		instance.controller.prepareToLoad(caption, apiName, materialType);

		instance.dialogStage.showAndWait();

		if (instance.controller.isOkClicked()) {
			return instance.controller.getMaterialContent();
		} else {
			return null;
		}

	}

}
