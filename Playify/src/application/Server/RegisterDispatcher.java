package application.Server;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.gson.JsonObject;

import application.DFS.DFS;
import application.DFS.DFS.FileJson;
import application.DFS.DFS.FilesJson;
import application.Models.User;

public class RegisterDispatcher {

	/**
	 * Verifies if the registration information is valid to 
	 * proceed with user creation or not
	 * @param username
	 * @param password
	 * @param confirmPassword
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String verifyRegisterInformation(String username, String password, String confirmPassword ) throws Exception {
		System.out.println("Verifying user information");
		//DFS dfs = new DFS(5010);
		
		System.out.println("Retrieving DFS Metadata");
		//FilesJson files = dfs.readMetaData();
		//List<FileJson> filelist = files.getFiles();
		//filelist.get(0).searchForUserInPage(username);
		//filelist.get(0).searchForPageByUsername(username);
		
		
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
		} else {
			//else, return an error message stating that Registration has failed
			JsonObject errorMessage = new JsonObject();
			errorMessage.addProperty("errorMessage", "Username already in use");
			return errorMessage.toString();
		}
	}

	/**
	 * Checks if the passwords match
	 * @param password (entered password)
	 * @param confirmPassword (entered password again for confirmation
	 * @return boolean
	 */
	public boolean checkIfPasswordsMatch(String password, String confirmPassword) {
		if(password.equals(confirmPassword)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Checks if the following username is taken or not taken by another user
	 * @param username
	 * @return
	 */
	public boolean checkUsername(String username) {

		/*
		try {
			UserDB userDatabase = new UserDB();
			User foundUser = userDatabase.getParticularUser(username);
			//if the user is null, then the user has not been found
			if(foundUser==null) {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/
		// return false since user has been found
		return false;
	}

}
