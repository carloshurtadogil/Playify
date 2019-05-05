package application.MapReduce;

import com.google.gson.JsonObject;
import application.DFS.DFS;

public interface MapReduceInterface {

	public void map(JsonObject value, DFS context, String file) throws Exception;
	
	public void reduce(String key, JsonObject values, DFS context, String file) throws Exception;
	
}