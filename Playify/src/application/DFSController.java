package application;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import application.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DFSController {
	
	@FXML
	private Label titleLabel;
	
	private User selectedUser;
	
	
	/**
	 * Set the user whose currently using this client
	 * @param theUser The current user 
	 */
	public void setLoggedUser(User theUser) throws FileNotFoundException, IOException, ParseException {
		this.selectedUser = theUser;
		//titleLabel.setText("Username: " + selectedUser.getUsername());
	}

	/**
	 * Return the the home controller
	 * @param event The button-clicked event that will trigger the code
	 */
	public void BackToHome(ActionEvent event) {
		try {
			FXMLLoader homeControllerLoader = new FXMLLoader();
			homeControllerLoader.setLocation(getClass().getResource("/application/Home.fxml"));
			Parent root = homeControllerLoader.load();
			
			
			HomeController homeController = homeControllerLoader.getController();
			homeController.setLoggedUser(selectedUser);
			
			Scene homeScene = new Scene(root);
			Stage homeStage = (Stage) titleLabel.getScene().getWindow();
			homeStage.setScene(homeScene);
			homeStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
