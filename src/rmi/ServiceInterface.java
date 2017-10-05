package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceInterface extends Remote {

    String bookRoom(String studentID, String campusName, String date, String room_Number, String timeslot) throws RemoteException;

    String getAvailableTimeSlot(String date) throws RemoteException;

    boolean cancelBooking(String studentID, String bookingID) throws RemoteException;

    int createRoom(String date, String room_Number, String[] list_Of_Time_Slots) throws RemoteException;

    int deleteRoom(String date, String room_Number, String[] list_Of_Time_Slots) throws RemoteException;
}
