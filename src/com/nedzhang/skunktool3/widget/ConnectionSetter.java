package com.nedzhang.skunktool3.widget;

import java.io.IOException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ConnectionSetter extends VBox {

	@FXML
	private CheckBox chkHttpClient;

	@FXML
	private ComboBox<String> cbxHttpTesterUrl;

	@FXML
	private PasswordField txtPassword;

	@FXML
	private TextField txtUserName;

	public ConnectionSetter() {

		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"ConnectionSetter.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}

		cbxHttpTesterUrl.getItems().clear();
	}

	@FXML
	private void onChkHttpClientClicked(final MouseEvent event) {
		final boolean isHttpClient = chkHttpClient.isSelected();
		cbxHttpTesterUrl.setDisable(!isHttpClient);
	}

	public StringProperty getUserIDProperty() {
		return txtUserName.textProperty();
	}

	public StringProperty getPasswordProperty() {
		return txtPassword.textProperty();
	}

	public SingleSelectionModel<String> getInteropUrlProperty() {
		return cbxHttpTesterUrl.selectionModelProperty().get();

	}

	public BooleanProperty getIsHttpClientProperty() {
		return chkHttpClient.selectedProperty();
	}

	public boolean addConnectionUrl(final String... items) {
		return cbxHttpTesterUrl.getItems().addAll(items);
	}

}
