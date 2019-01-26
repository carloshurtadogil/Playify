package application;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class UserResponse {
	
	@SerializedName("users")
	@Expose
	private List<User> usersList = new ArrayList<User>();
	
	public List<User> getUsersList(){
		return usersList;
	}
	
	public void setUsersList(List<User> list) {
		this.usersList = list;
	}
}
