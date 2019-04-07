package application;

import com.google.gson.JsonObject;

public interface ProxyInterface {
	
    public JsonObject synchExecution(String remoteMethod, String[] param);
    public void asynchExecution(String remoteMethod, String[] param);
    
}


