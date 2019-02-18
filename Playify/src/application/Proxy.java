package application;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

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
    Dispatcher dispacher;   // This is only for test. it should use the Communication  Module
    
    ClientCommunicationModule communicationModule;
    public Proxy(Dispatcher dispacher, ClientCommunicationModule communicationModule)
    {
        this.dispacher = dispacher;   
        this.communicationModule = communicationModule ;
    }
    
    /*
    * Executes the  remote method "remoteMethod". The method blocks until
    * it receives the reply of the message. 
    */
    public JsonObject synchExecution(String remoteMethod, String[] param)
    {
        JsonObject jsonRequest = new JsonObject();
        JsonObject jsonParam = new JsonObject();
        
        jsonRequest.addProperty("remoteMethod", remoteMethod);
        jsonRequest.addProperty("objectName", "SongServices");
        // It is hardcoded. Instead it should be dynamic using  RemoteRef
        if (remoteMethod.equals("getSongChunk"))
        {
            
            jsonParam.addProperty("song", param[0]);
            jsonParam.addProperty("fragment", param[1]);       
        
        }
        else if (remoteMethod.equals("getFileSize"))
        {
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
        
        if(proxyReturn.isEmpty() || proxyReturn !=null) {
        	JsonParser parser = new JsonParser();
        	JsonObject gsonProxyReturn = parser.parse(proxyReturn).getAsJsonObject();
        	
        	
        	return gsonProxyReturn;
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

    	String strRet = this.dispacher.dispatch(jsonRequest.toString());
        return;
    }
}



