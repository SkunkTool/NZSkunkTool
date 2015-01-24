/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nedzhang.skunktool3.jfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.w3c.dom.Document;

import com.nedzhang.skunktool3.widget.XmlViewer;
import com.nedzhang.util.XmlUtil;

/**
 * 
 * @author nzhang
 */
public class XmlViewerTester extends Application {

	@Override
	public void start(final Stage stage) throws Exception {

		final ScrollPane root = new ScrollPane();

		// XmlViewer viewer = new XmlViewer();

		final Document dataDoc = XmlUtil.getDocument(this.getClass()
				.getResourceAsStream(
						"/com/nedzhang/skunktool3/widget/user_list_2.xml"));

		// XmlViewerSource dataViewerSource =
		// XmlViewerSource.createViewerSource(dataDoc);

		// XmlViewer viwer = new XmlViewer();

		final Scene scene = new Scene(root);

		stage.setScene(scene);

		stage.getIcons().add(
				new Image(this.getClass()
						.getResourceAsStream("/Skunk-icon.png")));

		stage.setTitle("NZ Skunktool");

		stage.show();

		final AnchorPane aPane = new AnchorPane();

		final XmlViewer viewer = new XmlViewer();

		viewer.loadXmlData(dataDoc);

		AnchorPane.setLeftAnchor(viewer, 0.0);
		AnchorPane.setRightAnchor(viewer, 0.0);
		AnchorPane.setTopAnchor(viewer, 0.0);
		AnchorPane.setBottomAnchor(viewer, 0.0);

		aPane.getChildren().add(viewer);

		root.setContent(aPane);

		// There might be some thread leak from controls that prevents the
		// application
		// from closing. So I have to call a System.exit(0) at the end
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(final WindowEvent t) {
				Platform.exit();

				try {
					Thread.sleep(1000);
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.exit(0);
			}

		});
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String[] args) {
		launch(args);
	}
}