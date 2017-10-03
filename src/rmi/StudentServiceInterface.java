package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StudentServiceInterface extends Remote {

    String bookRoom(String studentID, String campusName, String date, String room_Number, String timeslot) throws RemoteException;

    String getAvailableTimeSlot(String date) throws RemoteException;

    boolean cancelBooking(String studentID, String bookingID) throws RemoteException;
}
