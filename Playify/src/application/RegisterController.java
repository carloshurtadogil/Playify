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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
	@SuppressWarnings("unchecked")
	public void Register(ActionEvent event) throws FileNotFoundException, IOException, ParseException{
		if(this.checkUsername() && this.checkPasswordsMatch() == true) {
			
			labelStatus.setText("New user created!");
			JSONParser parsing = new JSONParser();
			
			JSONObject mainObject = (JSONObject) parsing.parse(new FileReader("users.json"));
			JSONArray userArray = (JSONArray) mainObject.get("users");
			
			JSONObject newUser = new JSONObject();
			newUser.put("username", usernameField.getText());
			newUser.put("password", passwordField.getText());
			newUser.put("playlists", new JSONArray());
			userArray.add(newUser);
			
			User theUser = new User(usernameField.getText(), passwordField.getText());

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
			
			//Pass the logged in user from this controller to the HomeController 
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("/application/Home.fxml"));
			
			Parent root = fxmlLoader.load();
			Scene homeScene = new Scene(root);
			
			HomeController home = fxmlLoader.getController();
			home.setLoggedUser(theUser);
			
			Stage homeStage = new Stage();
			homeStage.setScene(homeScene);
			homeStage.show();
		}
		else {
			labelStatus.setText("Username already taken or passwords don't match");
		}
	}
	
	//Checks if the following entered username is not taken by another user
	//Return true if user has not been found
	//Return false if user has been found
	public boolean checkUsername() {
		
		try {
			Gson theGson = new Gson();
			boolean userFound = false;
			
			//Reads the entire users.json file
			UserResponse theResponse = theGson.fromJson(new FileReader("users.json"), UserResponse.class);
			
			if(theResponse !=null) {
				//Retrieves all users from the users.json file and traverses the entire list
				List<User> ultimateUserList = theResponse.getUsersList();
				
				for(User theUser: ultimateUserList) {
					if(theUser.getUsername().equals(usernameField.getText()) && theUser.getPassword().equals(passwordField.getText())) {
						userFound= true;
						break;
					}
				}
				//If particular user hasn't been found, return true
				if(!userFound)
					return true;	
				}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		//return false since user has been found
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
