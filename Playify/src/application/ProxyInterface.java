package application;


import com.google.gson.JsonObject;

public interface ProxyInterface {
    /*
    * Executes the  remote method "remoteMethod". The method blocks until
    * it receives the reply of the message. 
    * 1) It obtains the Json object of the remote method using the 
    *    method getRemoteReference of the RemoteRefInterface interface. 
    * 2) It prepares the message. In this case another Json object with 
    * the parameters as follows: 
{
   "execute":
   {
        "remoteMethod":"getSongChunk",
        "object":"SongServices",
        "param":
          {
              "song":"songId",
              "fragment":2,
          },
   }
}
    * and sends it using the "send" method of CommunicationModule
    * @param  remoteMethod: Name of the remote method. The remote methods
    * are defined in a JSON file.
    * @param  param: An array of object that define the parameters of the 
    * remote method. 
    * @return It returns a Gson object with the return of the remoteMethod
    *    example:
    *  {
    *       "ret":""
    *  }
    */
    public JsonObject synchExecution(String remoteMethod, String[] param);

    /*
    * Executes the  remote method remoteMethod and returns without waiting
    * for the reply. It does similar to synchExecution but does not 
    * return any value
    * 
    */
    public void asynchExecution(String remoteMethod, String[] param);
}


