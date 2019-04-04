package application.Server;

import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;

import application.Models.User;
import application.Models.UserResponse;

public class UserDB {

	//Fetches a user from users.json based on username
	public User getParticularUser(String username) throws Exception {
		System.out.println("User DB Called");
		Gson theGson = new Gson();
		/*
		DFS dfs = new DFS(5000);
		
		FilesJson files = dfs.readMetaData();
		List<FileJson> filelist = files.getFiles();
		for(FileJson file : filelist) {
			System.out.println(file.toString());
		}
		
		return null;
		*/
		UserResponse theResponse = theGson.fromJson(new FileReader("users.json"), UserResponse.class);
		List<User> ultimateUserList = theResponse.getUsersList();
		System.out.println("USERDBDBDBDBDBDBDBDB: SIZE: " + ultimateUserList.size());
		User foundUser = null;
		//traverses the entire list of users, and attempts to find if a particular user exists
		for (User u : ultimateUserList) {
			System.out.println("User Found Name: " + u.getUsername());
			if (u.getUsername().equals(username)) {
				foundUser = u;
				break;
			}
		}
		
		return foundUser;
	}
}
