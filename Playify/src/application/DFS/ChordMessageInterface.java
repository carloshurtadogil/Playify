package application.DFS;

import java.rmi.*;
import java.util.TreeMap;

import com.google.gson.JsonObject;

import application.DFS.DFS.PagesJson;
import application.MapReduce.Mapper;

import java.io.*;

public interface ChordMessageInterface extends Remote
{
    public ChordMessageInterface getPredecessor()  throws RemoteException;
    ChordMessageInterface locateSuccessor(long key) throws RemoteException;
    ChordMessageInterface closestPrecedingNode(long key) throws RemoteException;
    public void joinRing(String Ip, int port)  throws RemoteException;
    public void joinRing(ChordMessageInterface successor)  throws RemoteException;
    public void notify(ChordMessageInterface j) throws RemoteException;
    public boolean isAlive() throws RemoteException;
    public long getId() throws RemoteException;
    public int onNetworkSize(long source, int n) throws Exception;
    
    public void put(long guidObject, RemoteInputFileStream inputStream) throws IOException, RemoteException;
    public void put(long guidObject, String text) throws IOException, RemoteException;
    public RemoteInputFileStream get(long guidObject) throws IOException, RemoteException;   
    public byte[] get(long guidObject, long offset, int len) throws IOException, RemoteException;  
    public void delete(long guidObject) throws IOException, RemoteException;
	public void bulk(long page, DFS dfsInstance) throws Exception;
	public void mapContext(PagesJson pagesJson, Mapper mapper, DFS dfs, String file) throws Exception;
	public void reduceContext(PagesJson pagesJson, Mapper reducer, DFS dfs, String fileOutput) throws Exception;
    
}
