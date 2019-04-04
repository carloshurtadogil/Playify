package application.Server;

import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import application.DFS.DFS;
import application.DFS.DFS.FileJson;
import application.DFS.DFS.FilesJson;
import application.Models.Playlist;
import application.Models.User;

public class LoginDispatcher {
	
	public static DFS dfs;
	
	/* 
	 * verifyLoginInformation: Verifies if a user has entered correct login credentials
	 * @param username: Submitted username
	 * @param password: Submitted password 
	 * @return JSON message that contains an error message or username/password credentials
	 */
	public String verifyLoginInformation(String username, String password) throws Exception {
		dfs = Dispatcher.dfsInstance;
		
		System.out.println("Called LoginDispatcher.Carlos");
		Gson theGson = new Gson();
		
		FilesJson metaData = dfs.readMetaData();
		FileJson chordUsersJsonFile = metaData.getFiles().get(0);
		User foundUser = chordUsersJsonFile.searchForUserInPage(username, dfs);

		//If the page has been found, then return the user processed as JSON data
		if(foundUser != null) {
			List<Playlist> userPlaylists = foundUser.getPlaylists();
			User loggedUser = new User(username, password, userPlaylists);
			String userCredentials = theGson.toJson(loggedUser);
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
