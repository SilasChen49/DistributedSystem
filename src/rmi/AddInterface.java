package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AddInterface extends Remote {

    String get()throws java.rmi.RemoteException;
    void add(String webAddress)throws java.rmi.RemoteException;;
}
