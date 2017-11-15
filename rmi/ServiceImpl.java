package rmi;

import javax.xml.crypto.Data;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServiceImpl extends UnicastRemoteObject implements ServiceInterface {

    private Database database;
    private String campusName;
    private ServerLog serverLog;

    public ServiceImpl() throws RemoteException {
        super();
    }

    public ServiceImpl(Database database) throws RemoteException {
        super();
        this.database = database;
    }

    public ServiceImpl(Database database, String campusName) throws  RemoteException{
        super();
        this.database = database;
        this.campusName = campusName;
        serverLog = new ServerLog(campusName);
    }

    @Override
    public String bookRoom(String studentID, String campusName, String date, String room_Number, String timeslot) throws RemoteException {

        String bookingID = "";
        String data = "";
        String result="";
        String response="";
        if (studentID.substring(0,3).equals(campusName)) {
            return database.bookRoom(studentID, campusName, date, room_Number, timeslot);
        }
        else {

            System.out.println("Book on other campus");
            data = "bookRoom," + studentID + "," + campusName + "," + date + "," + room_Number + "," + timeslot;

            bookingID = UDPTransort(data, campusName);
            if (!bookingID.isEmpty()) {
                data = "modifyCount," + studentID + "," + "+1";
                UDPTransort(data, campusName);
            }
        }
        String parameters = "Campus: "+campusName +", Date: "+date+", Room number: "+room_Number+", Time slot:"+timeslot+".";
        if (bookingID.isEmpty())
            result="failed";
        else {
            result = "success";
            response = "Booking ID: "+bookingID;
        }
        serverLog.writeFile("Book Room", parameters,result,response);
        return bookingID;
    }

    @Override
    public String getAvailableTimeSlot(String date) throws RemoteException {
        /*
        * also get from other servers
        * */
        String availableTimeSlots = new String();
        String data = "getAvailableTimeSlot," + date;
        availableTimeSlots += UDPTransort(data, "DVL");
        availableTimeSlots += UDPTransort(data, "KKL");
        availableTimeSlots += UDPTransort(data, "WST");
        serverLog.writeFile("Get Available Timeslot", "Date: "+date, "success", availableTimeSlots);
        return availableTimeSlots;
    }

    @Override
    public boolean cancelBooking(String studentID, String bookingID) throws RemoteException {
        String campusName = bookingID.substring(0,3);
        String data = "cancelBooking," + studentID + "," + bookingID;
        boolean flag = false;
        String reply = UDPTransort(data, campusName);
        if (reply.equals("true")){
            System.out.println("cancelled success");
            data = "modifyCount," + studentID +"," + "-1";
            UDPTransort(data, studentID.substring(0,3));
            flag = true;
        }
        else{
            flag = false;
        }
        String parameters = "StudentID: "+studentID+", BookingID: "+bookingID+".";
        serverLog.writeFile("Cancel Booking", parameters, ""+flag, "");
        return flag;
    }

    @Override
    public int createRoom(String date, String room_Number, String[] list_Of_Time_Slots) throws RemoteException {

        int createdNumber =  database.createRoom(date, room_Number, list_Of_Time_Slots);
        String parameters = "Date:" + date + " ,Room Number:" + room_Number + ".";

        serverLog.writeFile("Create Room", parameters, "success", "The number of room has been created is:"+createdNumber);
        return createdNumber;
    }

    @Override
    public int deleteRoom(String date, String room_Number, String[] list_Of_Time_Slots) throws RemoteException {

        int deletedNumber = database.deleteRoom(date, room_Number, list_Of_Time_Slots);
        String parameters = "Date:" + date + " ,Room Number:" + room_Number + ".";
        serverLog.writeFile("Delete Room", parameters, "success", "The number of room has been created is:"+deletedNumber);
        return deletedNumber;
    }

    public static String UDPTransort(String data, String campusName) {
        int serverPort = 0;
        if (campusName.equals("DVL"))
            serverPort = 6999;
        else if (campusName.equals("KKL"))
            serverPort = 7999;
        else if (campusName.equals("WST"))
            serverPort = 8999;
        String replyString = "";
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] m = data.getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            DatagramPacket request =
                    new DatagramPacket(m, data.length(), aHost, serverPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            String requestString = new String();
            for (byte b : reply.getData()) {
                if (b != 0)
                    requestString += (char) b;
            }
            replyString += requestString;
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null) aSocket.close();
        }
        return replyString;
    }
}
