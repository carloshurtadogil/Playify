package application;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class RegisterController {
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private TextField confirmPasswordField;
	@FXML
	private Label labelStatus;
	
	@FXML
	 private AnchorPane rootPane;
	
	//Registers a new user into the system
	public void Register(ActionEvent event) throws FileNotFoundException, IOException, ParseException{
		if(this.checkUsername() && this.checkPasswordsMatch() == true) {
			JSONParser parsing = new JSONParser();
			
			JSONObject mainObject = (JSONObject) parsing.parse(new FileReader("users.json"));
			JSONArray userArray = (JSONArray) mainObject.get("users");
			
			JSONObject newUser = new JSONObject();
			newUser.put("username", usernameField.getText());
			newUser.put("password", passwordField.getText());
			userArray.add(newUser);
			
			//Writes the new user object into the users.json file
			try {
				BufferedWriter fileWrite  = new BufferedWriter(new FileWriter("users.json"));

				fileWrite.write(mainObject.toString());
				fileWrite.flush();
				fileWrite.close();
			}
			catch(IOException ioException) {
				ioException.printStackTrace();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			/*Gson otherGson = new Gson();
			JsonObject mainObject = otherGson.fromJson(new FileReader("users.json"), JsonObject.class);
			JsonObject userObject = new JsonObject();
			
			userObject.addProperty("username", usernameField.getText());
			userObject.addProperty("password", passwordField.getText());
			mainObject.get("users").getAsJsonArray().add(userObject);
			
			
			*/
			
		}
	}
	
	//Checks if the following entered username is not taken by another user
	public boolean checkUsername() {
		
		try {
			Gson theGson = new Gson();
			boolean userFound = false;

			UserResponse theResponse = theGson.fromJson(new FileReader("users.json"), UserResponse.class);
			if(theResponse !=null) {
				List<User> ultimateUserList = theResponse.getUsersList();
				
				for(User theUser: ultimateUserList) {
					System.out.println(theUser.getUsername() + " " + theUser.getPassword() + " ");

					System.out.println(usernameField + " " + passwordField + " ");
					if(theUser.getUsername().equals(usernameField.getText()) && theUser.getPassword().equals(passwordField.getText())) {
						userFound= true;
					}
				}
				
				if(userFound == true) {
					labelStatus.setText("User has already been found");
				}
				else {
					labelStatus.setText("User has not been taken");
					return true;
					
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	//Checks if the entered password and the confirmation password are equivalent
	public boolean checkPasswordsMatch() {
		if(passwordField.getText().equals(confirmPasswordField.getText())) {
			return true;
		}
		else {
			return false;
		}
	}
}
