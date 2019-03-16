package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import application.Models.RemoteMethod;
import application.Models.RemoteMethodResponse;

public class RemoteRef implements RemoteRefInterface{

	//Fetches the remote reference from a json file containing all remote methods
	@Override
	public String getRemoteReference(String remoteMethod) {
		
		Gson theGson = new Gson();
		
		try {
			//Fetch all remote methods from the json file
			String jsonString = "";
			
			RemoteMethodResponse remoteMethodResponse = theGson.fromJson(new FileReader("remoteMethods.json"), RemoteMethodResponse.class);
			List<RemoteMethod> allRemoteMethods = remoteMethodResponse.getRemoteMethods();
			
			//Traverses the entire list of remote methods in order to fetch that particular method
			if(allRemoteMethods.size() != 0 || allRemoteMethods !=null) {
				
				for(int i=0; i<allRemoteMethods.size(); i++) {
					if(allRemoteMethods.get(i).getName().equals(remoteMethod)){
						 jsonString= theGson.toJson(allRemoteMethods.get(i));
						 break;
					}
				}
				return jsonString;
			}
			
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
		
	}
	
}
