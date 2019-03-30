package application.Server;



public interface DispatcherInterface {

    public String dispatch(String request);

    public void registerObject(Object remoteMethod, String objectName);
}
