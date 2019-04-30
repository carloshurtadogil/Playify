package application.MapReduce;

import java.io.IOException;

import org.json.simple.JSONObject;

import com.google.gson.JsonObject;

import application.DFS.DFS;

public interface MapReduceInterface {

	public void map(String key, JsonObject value, DFS context, String file) throws IOException;
	
	public void reduce(Integer key, JsonObject values, DFS context, String file) throws IOException;
	
	
}