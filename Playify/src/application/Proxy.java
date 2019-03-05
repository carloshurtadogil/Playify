package application;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
* The Proxy implements ProxyInterface class. The class is incomplete 
* 
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   2019-01-24 
*/

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class Proxy implements ProxyInterface {
	
    ClientCommunicationModule communicationModule;
    
    public Proxy(ClientCommunicationModule communicationModule)
    {  
        this.communicationModule = communicationModule ;
    }
    
    /*
    * Executes the  remote method "remoteMethod". The method blocks until
    * it receives the reply of the message. 
    */
    public JsonObject synchExecution(String remoteMethod, String[] param)
    {
    	System.out.println("RM: "  + remoteMethod);
    	String proxyReturn ="";
        JsonObject jsonRequest = new JsonObject();
        JsonObject jsonParam = new JsonObject();
        
        
        RemoteRefInterface remoteReference =  new RemoteRef();
        String remoteReferenceResult = remoteReference.getRemoteReference(remoteMethod);
        
  
        if(remoteReferenceResult.length() > 0) {
        	System.out.println("RRR" + remoteReferenceResult);
        	JsonObject remoteReferenceContents = new Gson().fromJson(remoteReferenceResult, JsonObject.class);
        	jsonRequest.addProperty("remoteMethod", remoteReferenceContents.get("name").getAsString());
        	jsonRequest.addProperty("objectName", remoteReferenceContents.get("object").getAsString());
        
        	
        	JsonElement element = remoteReferenceContents.get("param");
        	
        	//Obtain the parameter contents while removing the exterior brackets
        	String paramsContent = (element.toString()).substring(1, element.toString().length()-1);
        	
        	
        	//Checks if there is a comma in the parameter contents string, if there isn't a comma in the string
        	//then there is only one parameter mapping
        	if(!paramsContent.contains(",")) {

            	String parameterName = paramsContent.substring(0,paramsContent.indexOf(':'));
            	jsonParam.addProperty(parameterName.substring(1, parameterName.length()-1), param[0] );
        	}
        	else {
        		//Split to retrieve different parameters e.g. "John : Snow" and "Bob : Stone"
            	String[] splitContent = paramsContent.split(",");
            	
            	//Traverse the array of parameters, then erase other nonimportant half of parameter component
            	//then add important component (parameter name) to the jsonParam object
            	for(int i=0; i<splitContent.length; i++) {
            		
            		String parameterName = splitContent[i].substring(0, splitContent[i].indexOf(':'));

            		jsonParam.addProperty(parameterName.substring(1, parameterName.length()-1), param[i] );
            		System.out.println("parameter to be added " + param[i]);
            		
            	}
        	}
        	
        	jsonRequest.add("param", jsonParam);
        	
        	System.out.println(jsonRequest.toString());
        	try {
        		jsonRequest = this.communicationModule.addRequestIdToRequest(jsonRequest);
        		proxyReturn = this.communicationModule.send(jsonRequest);
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}
        
        }
        
        if(!proxyReturn.isEmpty()) {
        	JsonElement trimmedElement = new JsonParser().parse(proxyReturn.trim());
        	JsonObject proxyReturnAsJson = trimmedElement.getAsJsonObject();
        	System.out.println(proxyReturnAsJson.toString() + " rocky balboa");
        	return proxyReturnAsJson;
        }

        
        return null;

    }

    /*
    * Executes the  remote method remoteMethod and returns without waiting
    * for the reply. It does similar to synchExecution but does not 
    * return any value
    * 
    */
    public void asynchExecution(String remoteMethod, String[] param)
    {
    	JsonObject jsonRequest = new JsonObject();
    	JsonObject jsonParam = new JsonObject();
    	
    	jsonRequest.addProperty("remoteMethod", remoteMethod);
    	jsonRequest.addProperty("objectName", "SongServices");
    	
    	if(remoteMethod.equals("getChunk")) {
    		jsonParam.addProperty("song", param[0]);
    		jsonParam.addProperty("fragment", param[1]);
    	}
    	else if(remoteMethod.equals("getFileSize")) {
    		jsonParam.addProperty("song", param[0]);
    	}
    	jsonRequest.add("param", jsonParam);
    	
    	String proxyReturn = "";
    	
    	//Send the marshalled file to the communication module in the client
        try {
        	jsonRequest = this.communicationModule.addRequestIdToRequest(jsonRequest);
			proxyReturn= this.communicationModule.send(jsonRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	//String strRet = this.dispacher.dispatch(jsonRequest.toString());
        return;
    }
}



