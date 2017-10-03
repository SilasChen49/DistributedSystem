package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdminInterface extends Remote{

    boolean createRoom(String room_Number, String date, String list_Of_Time_Slots) throws RemoteException;

    boolean deleteRoom(String room_number, String date, String list_Of_Time_Slots) throws RemoteException;
}