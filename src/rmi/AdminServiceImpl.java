package rmi;

import javax.xml.crypto.Data;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AdminServiceImpl extends UnicastRemoteObject implements AdminServiceInterface{

    private Database database;
    private String campusName;

    public AdminServiceImpl() throws RemoteException{
        super();
    }

    public AdminServiceImpl(Database database) throws RemoteException{
        super();
        this.database = database;
    }

    @Override
    public int createRoom(String date, String room_Number, String[] list_Of_Time_Slots) throws RemoteException {
        return database.createRoom(date, room_Number, list_Of_Time_Slots);
    }

    @Override
    public int deleteRoom(String date, String room_Number, String[] list_Of_Time_Slots) throws RemoteException {
        return database.deleteRoom(date, room_Number, list_Of_Time_Slots);
    }
}
