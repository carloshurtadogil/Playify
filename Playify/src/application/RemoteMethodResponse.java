package application;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoteMethodResponse{
	@SerializedName("remoteMethods")
	@Expose
	private List<RemoteMethod> remoteMethods;
	
	public void setRemoteMethods(ArrayList<RemoteMethod> remoteMethods) {
		this.remoteMethods = remoteMethods;
	}
	
	public List<RemoteMethod> getRemoteMethods(){
		return this.remoteMethods;
	}
}
