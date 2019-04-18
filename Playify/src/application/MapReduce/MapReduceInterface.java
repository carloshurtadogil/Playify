package application.MapReduce;

import java.io.IOException;

import org.json.simple.JSONObject;

import application.DFS.DFS;

public interface MapReduceInterface {

	public void map(String key, JSONObject value, DFS context, String file) throws IOException;
	
	public void reduce(String key, JSONObject values, DFS context, String file) throws IOException;
	
	
}