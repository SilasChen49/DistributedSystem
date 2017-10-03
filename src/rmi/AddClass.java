package rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class AddClass extends UnicastRemoteObject implements AddInterface {
    public String x = "";
    public state state;
    public AddClass(state state) throws Exception{
        super();
        this.state = state;
    }

    public AddClass(int port) throws RemoteException {
        super(port);
    }


    @Override
    public String get() throws RemoteException {
        System.out.println("www.baidu.com");
        state.x = state.x + "123";
        return "www.baidu.com"+ state.x;
    }

    @Override
    public void add(String webAddress) throws RemoteException {}

}
