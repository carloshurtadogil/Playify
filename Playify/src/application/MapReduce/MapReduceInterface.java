package application.MapReduce;

import java.util.*;
import com.google.gson.JsonObject;
import application.DFS.DFS;

public interface MapReduceInterface {

	public void map(JsonObject value, DFS context, String file) throws Exception;
	
	public void reduce(String key, List<JsonObject> values, DFS context, String file) throws Exception;
	
}