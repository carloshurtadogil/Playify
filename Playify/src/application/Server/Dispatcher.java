package application.Server;

/**
* The Dispatcher implements DispatcherInterface. 
*
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   02-11-2019 
*/

import java.util.HashMap;
import java.util.*;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import application.DFS.DFS;
import application.DFS.DFS.FileJson;
import application.DFS.DFS.FilesJson;





@SuppressWarnings("unused")
public class Dispatcher implements DispatcherInterface {
    HashMap<String, Object> ListOfObjects;
    

    public Dispatcher()
    {
    	
        ListOfObjects = new HashMap<String, Object>();
        try {
			DFS dfs = new DFS(5000);
			System.out.println("DFS Call Complete");
			FilesJson mfiles = dfs.readMetaData();
			List<FileJson> fileslist = mfiles.getFiles();
			System.out.println(mfiles.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public String dispatch(String request)
    {
        JsonObject jsonReturn = new JsonObject();
        JsonParser parser = new JsonParser();
        JsonObject jsonRequest = parser.parse(request).getAsJsonObject();
		
        try {
     		Object object = new Object();
        	if (jsonRequest.has("objectName")) {

        		String dispatcherName = jsonRequest.get("objectName").getAsString();
        		System.out.println("\n\n\n\nCalling Dispatcher: " + dispatcherName + "\n\n\n");
    			switch(dispatcherName) {
    				case "LoginDispatcher":
    					object = new LoginDispatcher();
    					break;
    				case "SongDispatcher":
    					object = new SongDispatcher();
    					break;
    				case "RegisterDispatcher":
    					object = new RegisterDispatcher();
    					break;
    				case "PlaylistDispatcher":
    					object = new PlaylistDispatcher();
    					break;
    			}
        	}
      
            // Obtains the object pointing to SongServices
            //Object object = ListOfObjects.get(jsonRequest.get("objectName").getAsString());
        	
            
            Method[] methods = object.getClass().getMethods();
            Method method = null;
            // Obtains the method
            for (int i=0; i<methods.length; i++)
            {   
                if (methods[i].getName().equals(jsonRequest.get("remoteMethod").getAsString()))
                    method = methods[i];
            }
            if (method == null)
            {
                jsonReturn.addProperty("error", "Method does not exist");
                return jsonReturn.toString();
            }
            // Prepare the  parameters 
            Class[] types =  method.getParameterTypes();

            Object[] parameter = new Object[types.length];
            String[] strParam = new String[types.length];
            JsonObject jsonParam = jsonRequest.get("param").getAsJsonObject();
            int j = 0;
            for (Map.Entry<String, JsonElement>  entry  :  jsonParam.entrySet())
            {
                strParam[j++] = entry.getValue().getAsString();
            }
            // Prepare parameters
            for (int i=0; i<types.length; i++)
            {
                switch (types[i].getCanonicalName())
                {
                    case "java.lang.Long":
                        parameter[i] =  Long.parseLong(strParam[i]);
                        break;
                    case "java.lang.Integer":
                        parameter[i] =  Integer.parseInt(strParam[i]);
                        break;
                    case "java.lang.String":
                        parameter[i] = new String(strParam[i]);
                        break;
                }
            }
            // Prepare the return
            
            Class returnType = method.getReturnType();
            String ret = "";
            switch (returnType.getCanonicalName())
                {
                    case "java.lang.Long":
                    	System.out.println("Is Long");
                        ret = method.invoke(object, parameter).toString();
                        break;
                    case "java.lang.Integer":
                    	System.out.println("Is Integer");
                        ret = method.invoke(object, parameter).toString();
                        break;
                    case "java.lang.String":
                    	System.out.println("Is String");
                        ret = (String)method.invoke(object, parameter);
                        break;
                }
            	System.out.println("Ret: " + ret);
                jsonReturn = (JsonObject) new JsonParser().parse(ret);
   
        } catch (InvocationTargetException | IllegalAccessException e)
        {
        	e.printStackTrace();
            jsonReturn.addProperty("error", "Error on " + jsonRequest.get("objectName").getAsString() + "." + jsonRequest.get("remoteMethod").getAsString());
        }
     
        //Replaces all backslashes in the json string, then return 
        String finalResult = (jsonReturn.toString()).replace("\\", "");
        System.out.println("At Dispatcher, " + finalResult);
        return finalResult;
}

    /* 
    * registerObject: It register the objects that handle the request
    * @param remoteMethod: It is the name of the method that 
    *  objectName implements. 
    * @objectName: It is the main class that contaions the remote methods
    * each object can contain several remote methods
    */
    public void registerObject(Object remoteMethod, String objectName)
    {
        ListOfObjects.put(objectName, remoteMethod);
    }
    
}

