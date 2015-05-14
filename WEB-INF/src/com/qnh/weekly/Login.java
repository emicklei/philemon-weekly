package com.qnh.weekly;

import java.util.HashMap;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.StyleSheet;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TextField;

/**
 * @author E.M.Micklei
 *
 */
public class Login extends ApplicationModel {
	private static int FIELDCOL = 14;
	private static int USERROW = 6;
	private static int LABELCOL = 4;

	private int screenWidth = 20;
	private int screenHeight = 40;
	private TextField userField;
	private TextField passwordField;

	public Login(ApplicationModel myParent) {
		super(myParent);
	}
	public static void main(String[] args) {
		Login tool = new Login(new WeekTool(null));
		tool.test("");
	}
	public void main(HashMap args){
		view.show(this.mainScreen());
	}
	public Screen mainScreen() {
		Screen main = new Screen("main", this, screenWidth, screenHeight);
		main.setTitle("QNH - Urenregistratie Login");
		main.setStyleSheet(this.getStyleSheet());
		main.setStyle("default");
		main.add(this.getUserField());
		main.add(this.getPasswordField());
		main.add(this.getUserLabel());
		main.add(this.getPasswordLabel());
		main.add(this.getLoginButton());
		return main;
	}
	public StyleSheet getStyleSheet() {
		return ((WeekTool) parent).createStyleSheet();
	}
	public Label getUserLabel() {
		Label lab = new Label("userL", USERROW, LABELCOL, 1, FIELDCOL - LABELCOL-1);
		lab.setString("User:");
		lab.setStyle("label");
		return lab;
	}
	public Label getPasswordLabel() {
		Label lab = new Label("passL", USERROW + 2, LABELCOL, 1, FIELDCOL - LABELCOL-1);
		lab.setString("Password:");
		lab.setStyle("label");
		return lab;
	}
	public TextField getUserField() {
		if (userField == null) {
			userField = new TextField("userF", USERROW, FIELDCOL, 1, 20);
			userField.setStyle("field");
		}
		return userField;
	}
	public TextField getPasswordField() {
		if (passwordField == null) {
			passwordField = new TextField("passwordF", USERROW + 2, FIELDCOL, 1, 20);
			passwordField.setStyle("field");
		}
		return passwordField;
	}
	public Button getLoginButton(){
		Button button = new Button("login",USERROW+4,FIELDCOL,1,12);
		button.setString("Login");
		button.setStyle("button");
		button.isDefault(true);
		button.onSendTo(EVENT_CLICKED,"tryLogin",this);
		return button;
	}
	public void tryLogin() {
		view.show(((WeekTool) parent).mainScreen());
	}
}