package application;

public class OsUtils
{
   private static String OS = null;
   //gets the name of the OS that they are using
   public static String getOsName()
   {
      if(OS == null) { OS = System.getProperty("os.name"); }
      return OS;
   }
   //returns window as the os
   public static boolean isWindows()
   {
      return getOsName().startsWith("Windows");
   }
   //returns unix as the os
   public static boolean isUnix() {
	   return getOsName().startsWith("Unix");
   } 
   //returns mac as the os
   public static boolean isMac() {
	   return getOsName().startsWith("Mac");
   }
}
