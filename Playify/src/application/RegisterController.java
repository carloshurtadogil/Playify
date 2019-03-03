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
import com.google.gson.JsonObject;

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

	// Registers a new user into the system
	@SuppressWarnings("unchecked")
	public void Register(ActionEvent event) throws FileNotFoundException, IOException, ParseException {
		
		try {
			ProxyInterface proxy = new Proxy(new ClientCommunicationModule());
			String[] param = new String[3];
			param[0] = usernameField.getText();
			param[1] = passwordField.getText();
			param[2] = confirmPasswordField.getText();
			JsonObject result = proxy.synchExecution("verifyRegisterInformation", param);

			if (result.has("error") || result.has("errorMessage")) {
				labelStatus.setText("User could not be created");
			} else {

				labelStatus.setText("Success");
				User retrieveNewUser = new Gson().fromJson(result, User.class);

				// Pass the logged in user from this controller to the HomeController
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/application/Home.fxml"));

				Parent root = loader.load();
				Scene homeScene = new Scene(root);

				HomeController home = loader.getController();
				home.setLoggedUser(retrieveNewUser);

				Stage homeStage = (Stage) labelStatus.getScene().getWindow();
				homeStage.setScene(homeScene);
				homeStage.show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
