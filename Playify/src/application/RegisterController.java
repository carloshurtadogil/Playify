package application;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegisterController {
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private TextField confirmPassword;
	
	@FXML
	private Label labelStatus;
	
	
	public void Register(ActionEvent event) throws IOException{
		if(this.checkUsername() && this.checkPasswordsMatch() == true) {
			
		}
	}
	
	//Checks if the following entered username is not taken by another user
	public boolean checkUsername() {
		//traverse the entire users.json file and determine
		//if that username exists or not
		
		return false;
	}
	
	
	//Checks if the entered password and the confirmation password are equivalent
	public boolean checkPasswordsMatch() {
		if(password.getText().equals(confirmPassword.getText())) {
			return true;
		}
		else {
			return false;
		}
	}
}
