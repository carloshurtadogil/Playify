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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import application.Models.User;

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

			ProxyInterface proxy = new Proxy(new ClientCommunicationModule());
			String[] param = new String[2];
			param[0] = usernameField.getText();
			param[1] = passwordField.getText();

			JsonObject result = proxy.synchExecution("verifyLoginInformation", param);

			// Checks if there are any error messages in the JSON to see if user has been
			// found or not
			if (result.has("error") || result.has("errorMessage")) {
				labelStatus.setText("Unsuccessful login attempt, incorrect username or password");
			}

			// otherwise, proceed to obtain user information and generate it to a User object
			else {

				User retrievedUser = new Gson().fromJson(result, User.class);
				labelStatus.setText("Success");

				// Pass the logged in user from this controller to the HomeController
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/application/Home.fxml"));

				Parent root = loader.load();
				Scene homeScene = new Scene(root);

				HomeController home = loader.getController();
				home.setLoggedUser(retrievedUser);

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

}
