/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nedzhang.skunktool3.widget;

import java.io.IOException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;

import com.nedzhang.util.XmlUtil;

/**
 * FXML Controller class
 * 
 * @author nzhang
 */
public class MaterialPanel3 extends VBox {

	// The layout of the material panel is
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// + ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ +
	// + s s +
	// + s txtContext (TextArea) s +
	// + s s +
	// + s s +
	// + ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ +
	// + Button Panel ~~~~~~~~ ~~~~~~~~ +
	// + | Load | | Save | +
	// + ~~~~~~~~ ~~~~~~~~ +
	// +----------------------------------------------------+
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++
	private static final int BUTTON_PANEL_SPACING = 3;
	private static final int BUTTON_PANEL_PADDING = 4;
	// private final TextArea txtContent;
	private final HTMLEditor txtContent;

	// private final EventHandler<? super MouseEvent> onBtnLoadClicked;
	// private EventHandler<? super MouseEvent> loadContentEventHandler;
	//
	// private EventHandler<? super MouseEvent> saveContentEventHandler;
	public MaterialPanel3() throws IOException {

		final Button btnFormat = new Button("Format Xml");
		final Button btnLoad = new Button("Load...");
		final Button btnSave = new Button("Save...");

		final HBox buttonPanel = new HBox();
		buttonPanel.setSpacing(BUTTON_PANEL_SPACING);
		buttonPanel.setPadding(new Insets(BUTTON_PANEL_PADDING,
				BUTTON_PANEL_PADDING, BUTTON_PANEL_PADDING,
				BUTTON_PANEL_PADDING));
		buttonPanel.setAlignment(Pos.CENTER_RIGHT);
		buttonPanel.setFillHeight(false);
		buttonPanel.getChildren().addAll(btnFormat, btnLoad, btnSave);

		txtContent = new HTMLEditor();
		// txtContent.setWrapText(true);

		VBox.setVgrow(txtContent, Priority.ALWAYS);
		getChildren().addAll(txtContent, buttonPanel);

		btnLoad.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent t) {
				onBtnLoadClicked(t);
			}
		});

		btnSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(final MouseEvent t) {
				onBtnSaveClicked(t);
			}
		});

		btnFormat.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(final MouseEvent t) {
				onBtnFormatClicked(t);
			}
		});
	}

	protected void onBtnFormatClicked(final MouseEvent t) {
		final String formattedXml = XmlUtil.prettyPrintNoDoc(getContent(),
				System.lineSeparator(), "\t");
		setContent(formattedXml);
	}

	private void onBtnLoadClicked(final MouseEvent t) {

		// if (loadContentEventHandler != null) {
		// loadContentEventHandler.handle(t);
		// }

		if (getOnLoadContent() != null) {
			getOnLoadContent().handle(t);
		}
	}

	private void onBtnSaveClicked(final MouseEvent t) {
		// if (saveContentEventHandler != null) {
		// saveContentEventHandler.handle(t);
		// }
		if (getOnSaveContent() != null) {
			getOnSaveContent().handle(t);
		}
	}

	private final ObjectProperty<EventHandler<? super MouseEvent>> onLoadContentProperty = new SimpleObjectProperty<EventHandler<? super MouseEvent>>();

	public final ObjectProperty<EventHandler<? super MouseEvent>> onLoadContentProperty() {
		return onLoadContentProperty;
	}

	/**
	 * @return the loadContentEventHandler
	 */
	public EventHandler<? super MouseEvent> getOnLoadContent() {
		return onLoadContentProperty.get();
	}

	/**
	 * @param loadContentEventHandler
	 *            the loadContentEventHandler to set
	 */
	public void setOnLoadContent(
			final EventHandler<? super MouseEvent> loadContentEventHandler) {
		// this.loadContentEventHandler = loadContentEventHandler;
		onLoadContentProperty.set(loadContentEventHandler);
	}

	private final ObjectProperty<EventHandler<? super MouseEvent>> onSaveContentProperty = new SimpleObjectProperty<EventHandler<? super MouseEvent>>();

	public final ObjectProperty<EventHandler<? super MouseEvent>> onSaveContentProperty() {
		return onSaveContentProperty;
	}

	/**
	 * @return the loadContentEventHandler
	 */
	public EventHandler<? super MouseEvent> getOnSaveContent() {
		return onSaveContentProperty.get();
	}

	/**
	 * @param loadContentEventHandler
	 *            the loadContentEventHandler to set
	 */
	public void setOnSaveContent(
			final EventHandler<? super MouseEvent> saveContentEventHandler) {
		// this.loadContentEventHandler = loadContentEventHandler;
		onSaveContentProperty.set(saveContentEventHandler);
	}

	// // notice we use MouseEvent here only because you call from onMouseEvent,
	// you can substitute any type you need
	// private ObjectProperty<EventHandler<MouseEvent>> propertyOnAction = new
	// SimpleObjectProperty<EventHandler<MouseEvent>>();
	//
	// public final ObjectProperty<EventHandler<MouseEvent>> onActionProperty()
	// {
	// return propertyOnAction;
	// }
	//
	// public final void setOnAction(EventHandler<MouseEvent> handler) {
	// propertyOnAction.set(handler);
	// }
	//
	// public final EventHandler<MouseEvent> getOnAction() {
	// return propertyOnAction.get();
	//
	// }
	/**
	 * @return the content
	 */
	public String getContent() {
		// return txtContent.getText();
		return txtContent.getHtmlText();
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(final String content) {
		// txtContent.setText(content);
		txtContent.setHtmlText(content);
	}

	// /**
	// * @param content the content to set
	// */
	// public void setContent(String content, boolean formatXml) {
	// if (formatXml) {
	// // try {
	// txtContent.setText(XmlUtil.prettyPrintNoDoc(content, "\n", "\t"));
	// // } catch (IllegalArgumentException | TransformerException
	// // | ParserConfigurationException | SAXException | IOException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // txtContent.setText(content);
	// // }
	// } else {
	// txtContent.setText(content);
	// }
	// }

	// public StringProperty textProperty() {
	// return txtContent.textProperty();
	// }
}
