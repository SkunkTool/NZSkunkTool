package com.nedzhang.skunktool3.widget;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class ShipnodeDGManager extends VBox {

	public ShipnodeDGManager() {
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"ShipnodeDGManager.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
	}
}
