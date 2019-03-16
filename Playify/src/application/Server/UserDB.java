package application.Server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import application.Models.User;
import application.Models.UserResponse;

public class UserDB {

	//Fetches a user from users.json based on username
	public User getParticularUser(String username) throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson theGson = new Gson();
		
		UserResponse theResponse = theGson.fromJson(new FileReader("users.json"), UserResponse.class);
		List<User> ultimateUserList = theResponse.getUsersList();
		
		User foundUser = null;
		//traverses the entire list of users, and attempts to find if a particular user exists
		for (User u : ultimateUserList) {
			if (u.getUsername().equals(username)) {
				foundUser = u;
				break;
			}
		}
		
		return foundUser;
	}
}
