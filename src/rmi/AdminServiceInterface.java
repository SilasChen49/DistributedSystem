package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdminServiceInterface extends Remote {

    int createRoom(String date, String room_Number, String[] list_Of_Time_Slots) throws RemoteException;

    int deleteRoom(String date, String room_Number, String[] list_Of_Time_Slots) throws RemoteException;
}