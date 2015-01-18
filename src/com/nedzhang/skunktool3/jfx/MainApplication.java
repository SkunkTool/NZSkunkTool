/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nedzhang.skunktool3.jfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author nzhang
 */
public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

    	Parent root = FXMLLoader.load(getClass().getResource("/com/nedzhang/skunktool3/apiRunner/ApiRunnerWindow.fxml"));

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/Skunk-icon.png")));
        
        stage.setTitle("NZ Skunktool");
        
        stage.show();
        
        // There might be some thread leak from controls that prevents the application 
        // from closing. So I have to call a System.exit(0) at the end
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}