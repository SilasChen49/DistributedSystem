package rmi;

import java.rmi.Naming;

public class NewThread implements Runnable {

    String campusID;
    ServiceInterface service;
    NewThread(String campus){
        this.campusID = campus;
    }

    public void run(){
        try{
            if (campusID.equals("DVL"))
                service = (ServiceInterface) Naming.lookup("rmi://localhost:6000/DVL");
            else if (campusID.equals("KKL"))
                service = (ServiceInterface) Naming.lookup("rmi://localhost:7000/KKL");
            else if (campusID.equals("WST"))
                service = (ServiceInterface) Naming.lookup("rmi://localhost:8000/WST");
            String id = campusID+"1111";
            UserLog userLog = new UserLog(id);
            String date = "2017-10-31";
            String room_Number = "201";
            String timeslot = "08:00-10:00";
            String[] list_Of_Timeslots = new String[4];
            list_Of_Timeslots[0] = "08:00-10:00";
            list_Of_Timeslots[1] = "10:00-10:20";
            list_Of_Timeslots[2] = "10:20-10:40";
            list_Of_Timeslots[3] = "10:40-11:00";
            service.bookRoom(id, campusID, date, room_Number, list_Of_Timeslots[3]);

        }catch (Exception e){
            System.out.println("child interrupted.");
        }
    }
}
