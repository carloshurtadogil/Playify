package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

	 try {
		 Gson theGson = new Gson();
		 boolean userFound = false;

		 UserResponse theResponse = theGson.fromJson(new FileReader("users.json"), UserResponse.class);
		 
		 List<User> ultimateUserList = theResponse.getUsersList();
		 for(User u : ultimateUserList) {
			 if(u.getUsername().equals(usernameField.getText()) && u.getPassword().equals(passwordField.getText())) {
				 userFound = true;
			 }
		 }
		 if(!userFound) {
			 labelStatus.setText("Unsuccessful login attempt, incorrect username or password");
		 }
		 else {
			 labelStatus.setText("Success");
		 }
		 //List<User> videos = theGson.fromJson(new FileReader("users.json"), new TypeToken<List<User>>(){}.getType());
	 }
	 catch(Exception e) {
		 System.out.println("Exception thrown");
		 e.printStackTrace();
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
