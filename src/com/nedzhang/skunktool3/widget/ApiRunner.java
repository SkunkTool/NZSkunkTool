package com.nedzhang.skunktool3.widget;

import com.nedzhang.skunktool3.ApplicationProperty;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class ApiRunner extends VBox {

	protected static String programID;

	protected static boolean verboseMode;

	static {
		try {
			programID = ApplicationProperty.get("STERLING_INTEROP_PROG_ID");
			String verboseModeString = ApplicationProperty.get("VERBOSE_MODE");
			verboseMode = Boolean.valueOf(verboseModeString);
		} catch (Throwable e) {
			e.printStackTrace();
			programID = "SKUNKTOOL3";
			verboseMode = true;
		}
	}
	
	public abstract void setUserIDProperty(StringProperty userIDProperty);
	
	public abstract void setPasswordProperty(StringProperty passwordProperty);
	
	public abstract void setInteropUrlProperty(SingleSelectionModel<String> singleSelectionModel);
	
	public abstract void setIsHttpClientProperty(BooleanProperty isHttpClientProperty);
}
