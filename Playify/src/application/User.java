package application;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class User {
	
	@SerializedName("username")
	@Expose
	private String username;
	@SerializedName("password")
	@Expose
	private String password;
	
	public User(String user, String pass) {
		this.username = user;
		this.password = pass;
	}
	
	
	public String getUsername()
	{
		return username;
	}
	public String getPassword() {
		return password;
	}
	
	public void setUsername(String uname) {
		this.username = uname;
	}
	
	public void setPassword(String pass) {
		this.password = pass;
	}
}
