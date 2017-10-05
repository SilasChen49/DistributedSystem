package rmi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class DebugClient {
    public static void main(String[] args) throws Exception {
        try {
            boolean flag = true;
            String number = "0";
            ServiceInterface service1 = (ServiceInterface) Naming.lookup("rmi://localhost:6000/DVL");
            ServiceInterface service2 = (ServiceInterface) Naming.lookup("rmi://localhost:7000/KKL");
            ServiceInterface service3 = (ServiceInterface) Naming.lookup("rmi://localhost:8000/WST");
            String id = "DVLA1";

            UserLog userLog = new UserLog(id);
            String date = "1";
            String room_Number = "1";
            String timeslot = "08:00-10:00";
            String[] s = new String[4];
            s[0] = timeslot;
            s[1] = "10:00-10:20";
            s[2] = "10:20-10:40";
            s[3] = "10:40-11:00";
            userLog.writeFile("ggg", "gg");
//            System.out.println(service1.createRoom(date,room_Number,s));
//            System.out.println(service2.createRoom(date,room_Number,s));
//            System.out.println(service3.createRoom(date, room_Number, s));
//            service1.bookRoom(id, "WST", date, room_Number,s[3]);
            System.out.println(service1.cancelBooking(id,"KKLBNO1"));
            System.out.println(service2.deleteRoom(date, room_Number, s));
            System.out.println(service1.getAvailableTimeSlot(date));
//            System.out.println(bookingid);
//            String bookingID = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
