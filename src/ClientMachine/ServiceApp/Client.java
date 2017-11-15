package ClientMachine.ServiceApp;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;

public class Client {
    static Service ImplDVL;
    static Service ImplKKL;
    static Service ImplWST;
    public static void main(String[] args) {
        try {
            new UserClient().run(args);
//            ORB orb = ORB.init(args, null);
//            org.omg.CORBA.Object objRef =
//                    orb.resolve_initial_references("NameService");
//            NamingContextExt ncRef =
//                    NamingContextExtHelper.narrow(objRef);
//            ImplDVL =
//                    ServiceHelper.narrow(ncRef.resolve_str("DVL"));
//            ImplKKL =
//                    ServiceHelper.narrow(ncRef.resolve_str("KKL"));
//            ImplWST =
//                    ServiceHelper.narrow(ncRef.resolve_str("WST"));
//            String id = "WSTS1111";
//            String date = "2017-10-29";
//            String room_Number = "201";
//            String timeslot = "08:00-10:00";
//            String[] list_Of_Timeslots = new String[4];
//            list_Of_Timeslots[0] = "08:00-10:00";
//            list_Of_Timeslots[1] = "10:00-10:20";
//            list_Of_Timeslots[2] = "10:20-10:40";
//            list_Of_Timeslots[3] = "10:40-11:00";
//            String[] lists = new String[1];
//            lists[0] = list_Of_Timeslots[0];
//            System.out.println(ImplWST.sayHello());
//            System.out.println(ImplWST.createRoom(date,room_Number, list_Of_Timeslots));
//            System.out.println(ImplDVL.createRoom(date,room_Number, list_Of_Timeslots));
//            System.out.println(ImplKKL.createRoom(date,room_Number, list_Of_Timeslots));
//            System.out.println(ImplWST.bookRoom(id,"WST",date,room_Number,list_Of_Timeslots[0]));
//            System.out.println(ImplWST.bookRoom(id,"DVL",date,room_Number,list_Of_Timeslots[0]));
//            ImplWST.bookRoom(id, "KKL", date, room_Number, list_Of_Timeslots[1]);
//            ImplWST.bookRoom(id, "WST",date, room_Number, list_Of_Timeslots[1]);
//            ImplWST.deleteRoom(date,room_Number,lists);
//            ImplWST.getAvailableTimeSlot(date);
//            System.out.println(ImplWST.bookRoom(id,"KKL", date, room_Number, list_Of_Timeslots[3]));
//            System.out.println(ImplWST.cancelBooking(id, "KKLBNO4"));
////            ImplWST.changeReservation("DVLBNO2",id,"KKL", date, room_Number, list_Of_Timeslots[2]);
//
//
//            System.out.println(ImplWST.getAvailableTimeSlot(date));
//            int serverPort = 0;
//            String data = "getAvailableTimeSlot,2017-10-31";
        } catch (Exception e) {
        }
    }
}