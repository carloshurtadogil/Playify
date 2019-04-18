package application.Server;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import application.DFS.DFS;
import application.DFS.DFS.FileJson;
import application.DFS.DFS.FilesJson;
import application.Models.DateTime;
import application.Models.Playlist;
import application.Models.User;

public class RegisterDispatcher {

	public static DFS dfs;

	/**
	 * Verifies if the registration information is valid to proceed with user
	 * creation or not
	 * 
	 * @param username
	 * @param password
	 * @param confirmPassword
	 * @return
	 * @throws Exception
	 */

	public String verifyRegisterInformation(String username, String password, String confirmPassword) throws Exception {
		dfs = Dispatcher.dfsInstance;
		if (this.checkIfUsernameExists(username) && this.checkIfPasswordsMatch(password, confirmPassword)) {
			
			List<Playlist> playlists = new ArrayList<Playlist>();
			User registeredUser = new User(username, password, playlists);
			
			String userInJsonFormat = new Gson().toJson(registeredUser);
			
			String [] components = new String[2];
			components[0] = "chordusers.json";
			components[1] = userInJsonFormat;
			
			dfs.appendComponent(components);
			return userInJsonFormat.toString();
			
		} 
		else {
			// else, return an error message stating that Registration has failed
			JsonObject errorMessage = new JsonObject();
			errorMessage.addProperty("errorMessage", "Username already in use");
			return errorMessage.toString();
		}
	}

	/**
	 * Checks if the passwords match
	 * 
	 * @param password
	 *            (entered password)
	 * @param confirmPassword
	 *            (entered password again for confirmation
	 * @return boolean
	 */
	public boolean checkIfPasswordsMatch(String password, String confirmPassword) {
		if (password.equals(confirmPassword)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if the following username is taken or not taken by another user
	 * 
	 * @param username
	 * @returns
	 * @throws Exception
	 */
	public boolean checkIfUsernameExists(String username) throws Exception {

		String formattedTimeStamp = DateTime.retrieveCurrentDate();
		
		FilesJson metaData = dfs.readMetaData();
		FileJson chordUsersJsonFile = metaData.getFiles().get(0);
		chordUsersJsonFile.setReadTimeStamp(formattedTimeStamp);
		User foundUser = chordUsersJsonFile.searchForUserInPage(username, dfs);

		if (foundUser != null) {
			System.out.println("User in use");
			return false;
		}
		System.out.println("User not in use");
		return true;
	}

}
