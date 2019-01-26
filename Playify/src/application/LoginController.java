package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {
 @FXML
 private Label labelStatus;
 @FXML
 private TextField usernameField;
 @FXML
 private TextField passwordField;
 
 @FXML
 private AnchorPane rootPane;
 
 public void Login(ActionEvent event) {
	 if(usernameField.getText().equals("user") && passwordField.getText().equals("password")) {
		 labelStatus.setText("Login success");
	 }
	 else {
		 labelStatus.setText("Login failed");
	 }
 }
 
 //Loads a new Register FXML page in the same scene
 public void SendToRegister(ActionEvent event) throws IOException {
	 AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/Register.fxml"));
	 rootPane.getChildren().setAll(pane);
	 
 }
 
 public void Logout(ActionEvent event) {
	 
 }
}
