package application;

import com.google.gson.Gson;

interface RemoteRefInterface {
  /*
  * return the Json object defining the remote method
  * The catalog of the remote methods are defined in 
  * a Json file as follows:
  {
   "remoteMethod":
   {
        "name":"login",
        "object":"LoginServices",
        "call-semantics":"at-most-one",
        "param":
          {
              "user":"string",
              "password":"string"
          },
        "return":"String"
   },
   "remoteMethod":
   {
        "name":"getSongChunk",
        "object":"SongServices",
        "call-semantics":"maybe",
        "param":
          {
              "song":"Long",
              "fragment":"Long"
          },
        "return":"Byte[]"
   }
}
  * @param  remoteMethod: Name of the remote method or
  * if the remote method does not exists in the catalog
  */
  String getRemoteReference(String remoteMethod);
}


