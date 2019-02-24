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

public class RegisterDispatcher {

	public String verifyRegisterInformation(String username, String password, String confirmPassword ) throws FileNotFoundException, IOException, ParseException {
		if(this.checkUsername(username) && this.checkIfPasswordsMatch(password, confirmPassword)) {
			JSONObject newUser = new JSONObject();
			newUser.put("username", username);
			newUser.put("password", password);
			newUser.put("playlists", new JSONArray());
			
			JSONParser parsing = new JSONParser();
			JSONObject mainObject = (JSONObject) parsing.parse(new FileReader("users.json"));
			JSONArray userArray = (JSONArray) mainObject.get("users");
			
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
			
			
			return newUser.toString();
		}
		else {
			//else, return an error message stating that Registration has failed
			JsonObject errorMessage = new JsonObject();
			errorMessage.addProperty("errorMessage", "Username already in use");
			return errorMessage.getAsString();
		}
	}

	public boolean checkIfPasswordsMatch(String password, String confirmPassword) {
		if(password.equals(confirmPassword)) {
			return true;
		}
		else {
			return false;
		}
	}

	// Checks if the following entered username is not taken by another user
	// Return true if user has not been found
	// Return false if user has been found
	public boolean checkUsername(String username) {

		try {
			Gson theGson = new Gson();
			boolean userFound = false;

			// Reads the entire users.json file
			UserResponse theResponse = theGson.fromJson(new FileReader("users.json"), UserResponse.class);

			if (theResponse != null) {
				// Retrieves all users from the users.json file and traverses the entire list
				List<User> ultimateUserList = theResponse.getUsersList();

				for (User theUser : ultimateUserList) {
					if (theUser.getUsername().equals(username)) {
						userFound = true;
						break;
					}
				}
				// If particular user hasn't been found, return true
				if (!userFound)
					return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// return false since user has been found
		return false;
	}

}
