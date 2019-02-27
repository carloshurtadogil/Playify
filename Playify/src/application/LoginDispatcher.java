package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class LoginDispatcher {
	
	
	/* 
	 * verifyLoginInformation: Verifies if a user has entered correct login credentials
	 * @param username: Submitted username
	 * @param password: Submitted password 
	 * @return JSON message that contains an error message or username/password credentials
	 */
	public String verifyLoginInformation(String username, String password) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
	
		boolean userFound = false;
		User theUser = new User(null, null);
		Gson theGson = new Gson();
		
		//Reads the entire users.json file
		UserResponse theResponse = theGson.fromJson(new FileReader("users.json"), UserResponse.class);
		List<User> ultimateUserList = theResponse.getUsersList();
		
		//traverses the entire list of users, and attempts to find if a particular user exists
		for (User u : ultimateUserList) {
			if (u.getUsername().equals(username)
					&& u.getPassword().equals(password)) {
				
				theUser = u;
				//mark as found, and assign the recently found user
				userFound = true;
				break;
			}
		}
		
		//If user has been found, then return the user processed as JSON data
		if(userFound) {
			String userCredentials = theGson.toJson(theUser);
			System.out.println("FOUND! " + userCredentials);
			return userCredentials;
		}
		
		//else, return an error message stating that Login has failed
		JsonObject errorMessage = new JsonObject();
		System.out.println("not found");
		errorMessage.addProperty("errorMessage", "Incorrect username or password");
		return errorMessage.getAsString();
	}
	
}
