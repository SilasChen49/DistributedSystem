package rmi;

import javax.xml.crypto.Data;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StudentServiceImpl extends UnicastRemoteObject implements StudentServiceInterface{

    private Database database;
    private String campusName;

    public StudentServiceImpl() throws RemoteException{
        super();
    }

    public StudentServiceImpl(Database database) throws RemoteException {
        super();
        this.database = database;
    }

    @Override
    public String bookRoom(String studentID, String campusName, String date, String room_Number, String timeslot) throws RemoteException {
        return database.bookRoom(studentID, date, room_Number, timeslot);
    }

    @Override
    public String getAvailableTimeSlot(String date) throws RemoteException {
        /*
        * also get from other servers
        * */
        int availableNumber = database.getAvailableTimeSlot(date);
        String availableTimeSlots = new String();
        availableTimeSlots += campusName + " " + availableNumber;
        return availableTimeSlots;
    }

    @Override
    public boolean cancelBooking(String studentID, String bookingID) throws RemoteException {
        return database.cancelBooking(studentID, bookingID);
    }
}
