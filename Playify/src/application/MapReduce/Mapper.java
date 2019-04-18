package application.MapReduce;

import java.io.IOException;

import org.json.simple.JSONObject;

import application.DFS.DFS;

public class Mapper implements MapReduceInterface{
	
	@Override
	public void map(String key, JSONObject value, DFS context, String file) throws IOException {
		// TODO Auto-generated method stub
		
		//newKey = songtitle in value
		//newValue = subset of value. Can have items like songTitle, year of release, 
		//artist, duration etc. 
		
		//context.emit(newKey, newValue, file);
	}

	@Override
	public void reduce(String key, JSONObject values, DFS context, String file) throws IOException {
		// TODO Auto-generated method stub
		
	//	sort(values);
	//	context.emit(key, values, file);
		
	} 
}
