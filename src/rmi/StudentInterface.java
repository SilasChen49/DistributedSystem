package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StudentInterface extends Remote{

    boolean bookRoom(String campusName, String studentName, String date, String timeslot) throws RemoteException;

    String getAvailableTimeSlot(String date) throws RemoteException;

    boolean cancelBooking(String BookingID) throws RemoteException;
}
