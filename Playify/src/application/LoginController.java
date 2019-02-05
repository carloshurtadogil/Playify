package application;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import com.google.gson.Gson;

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
			User theUser = new User(null, null);
			
			//Reads the entire users.json file
			UserResponse theResponse = theGson.fromJson(new FileReader("users.json"), UserResponse.class);
			List<User> ultimateUserList = theResponse.getUsersList();
			
			//traverses the entire list of users, and attempts to find if a particular user exists
			for (User u : ultimateUserList) {
				if (u.getUsername().equals(usernameField.getText())
						&& u.getPassword().equals(passwordField.getText())) {

					if (u.getPlaylists() == null) {
						System.out.println("no playlist found");
					}
					// for(int i=0;i<u.getPlaylists().size(); i++) {
					// for(int j=0; j< u.getPlaylists().get(i).getSongs().size(); j++) {
					// System.out.println(u.getPlaylists().get(i).getSongs().get(j).getSongName());
					// }
					// }
					
					//mark as found, and assign the recently found user
					userFound = true;
					theUser = u;
					break;
				}
			}
			if (!userFound) {
				labelStatus.setText("Unsuccessful login attempt, incorrect username or password");
			} else {
				labelStatus.setText("Success");

				// Pass the logged in user from this controller to the HomeController
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/application/Home.fxml"));

				Parent root = loader.load();
				Scene homeScene = new Scene(root);

				HomeController home = loader.getController();
				home.setLoggedUser(theUser);

				Stage homeStage = (Stage) labelStatus.getScene().getWindow();
				homeStage.setScene(homeScene);
				homeStage.show();

			}

		} catch (Exception e) {
			System.out.println("Exception thrown");
			e.printStackTrace();
		}
	}

	// Loads a new Register FXML page in the same scene
	public void SendToRegister(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/Register.fxml"));
		rootPane.getChildren().setAll(pane);

	}

	public void Logout(ActionEvent event) {

	}
}
