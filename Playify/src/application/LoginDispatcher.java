package application;

import java.io.FileNotFoundException;
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
		
		Gson theGson = new Gson();
		User theUser=null;
		
		//Attempt to retrieve the user from the users.json file
		UserDB userDatabase = new UserDB();
		theUser = userDatabase.getParticularUser(username);

		//If user has been found, then return the user processed as JSON data
		if(theUser!=null) {
			String userCredentials = theGson.toJson(theUser);
			System.out.println("FOUND! " + userCredentials);
			return userCredentials;
		}
		
		//else, return an error message stating that Login has failed
		JsonObject errorMessage = new JsonObject();
		System.out.println("not found");
		errorMessage.addProperty("errorMessage", "Incorrect username or password");
		return errorMessage.toString();
	}
	
}
