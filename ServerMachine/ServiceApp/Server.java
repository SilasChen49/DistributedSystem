package ServerMachine.ServiceApp;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server extends ServicePOA{
    DatagramSocket socket;
    Database database;
    String campus;
    String requestString;
    int port;
    private ORB orb;
    ServerLog serverLog;
    static String[] campusNames = {"DVL", "KKL", "WST"};
    public Server(String campus){
        serverLog = new ServerLog(campus);
        database = new Database();
        this.campus = campus;
        port = getPort(campus);
        try {
            socket = new DatagramSocket(port);
        }catch (Exception e){
            System.out.println("socket error!");
        }
    }

    public static void main(String[] args) {
        for (String campus: campusNames)
            new Server(campus).startServerThreads(args);
    }

    public static int getPort(String campus){
        if (campus.equals("DVL"))
            return 6999;
        else if (campus.equals("KKL")) {
            return 7999;
        } else {
            return 8999;
        }
    }
    public void setORB(ORB orb_val) {
        orb = orb_val;
    }
    public String sayHello(){
        return "Hello";
    }

    public String changeReservation(String bookingID, String studentID, String campusName, String date, String room_Number, String timeslot){
        String data = "getCancelAvailable,"+ studentID + "," + bookingID;
        String campus = bookingID.substring(0,3);
        String cancelAvailable = UDPTransort(data, campus);
        data = "getRoomRecordAvailable," + studentID + "," + campusName + "," + date + "," + room_Number + "," + timeslot;
        String bookRoomAvailable = UDPTransort(data, campusName);
        System.out.println(cancelAvailable+" "+bookRoomAvailable);
        if (cancelAvailable.equals("true") && bookRoomAvailable.equals("true")){
            data = "cancelBooking," + studentID + "," + bookingID;
            UDPTransort(data, campus);
            data = "bookRoom," + studentID + "," + campusName + "," + date + "," + room_Number + "," + timeslot;
            String new_bookingID = UDPTransort(data, campusName);
            System.out.println("success");
            return new_bookingID;
        }
        else return "false";
    }

    public String bookRoom(String studentID, String campusName, String date, String room_Number, String timeslot) {

        String bookingID = "";
        String data = "";
        String result="";
        String response="";
            System.out.println("Book on other campus");
            data = "bookRoom," + studentID + "," + campusName + "," + date + "," + room_Number + "," + timeslot;

            bookingID = UDPTransort(data, campusName);
        return bookingID;
    }

    public String getAvailableTimeSlot(String date) {
        /*
        * also get from other servers
        * */
        String availableTimeSlots = new String();
        String data = "getAvailableTimeSlot," + date;
        availableTimeSlots += UDPTransort(data, "DVL");
        availableTimeSlots += UDPTransort(data, "KKL");
        availableTimeSlots += UDPTransort(data, "WST");
        return availableTimeSlots;
    }

    public boolean cancelBooking(String studentID, String bookingID) {
        String result="";
        String response="";
        String campusName = bookingID.substring(0,3);
        String data = "cancelBooking," + studentID + "," + bookingID;
        boolean flag = false;
        String reply = UDPTransort(data, campusName);
        if (reply.equals("true")){
            System.out.println("cancelled success");
            flag = true;
        }
        else{
            flag = false;
        }
        String parameters = "StudentID: "+studentID+", BookingID: "+bookingID+".";
//        serverLog.writeFile("Cancel Booking", parameters, ""+flag, "");
        return flag;
    }

    public int createRoom(String date, String room_Number, String[] list_Of_Time_Slots) {
        String data = "";
        String result="";
        String response="";
        System.out.println("create room here");
        int createdNumber =  database.createRoom(date, room_Number, list_Of_Time_Slots);
        String parameters = "Date:" + date + " ,Room Number:" + room_Number + ".";
        if (createdNumber>0)
            result = "success";
        else
            result = "failed";
        serverLog.writeFile("Create Room", parameters, "success", "The number of room has been created is:"+createdNumber);
        return createdNumber;
    }

    public int deleteRoom(String date, String room_Number, String[] list_Of_Time_Slots) {
        String data = "";
        String result="";
        String response="";
        int deletedNumber = database.deleteRoom(date, room_Number, list_Of_Time_Slots);
        String parameters = "Date:" + date + " ,Room Number:" + room_Number + ".";
        if (deletedNumber>0)
            result = "success";
        else
            result = "failed";
        serverLog.writeFile("Delete Room", parameters, result, "The number of room has been created is:"+deletedNumber);
        return deletedNumber;
    }

    public static String UDPTransort(String data, String campusName) {
        int serverPort = getPort(campusName);
        System.out.println(data);
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

    public void shutdown() {
        orb.shutdown(false);
    }

    private void handleUDPRequest(String s, InetAddress address, int port){
        System.out.println("Handle UDP Request"+s);
        try {
            byte[] buffer = new byte[1000];
            String[] requestStringList = s.split(",");
            String resultType = "";
            String result = "";
            String response = "";
            String parameters = "";
            if (requestStringList[0].equals("getAvailableTimeSlot")) {
                int availableNumber = database.getAvailableTimeSlot(requestStringList[1]);
                String replyData = campus + " " + availableNumber + ", ";
                buffer = replyData.getBytes();
                parameters = "Date:" + requestStringList[1]+".";
                result = "success";
                response = "Available Time Slot Number:" + replyData;
            } else if (requestStringList[0].equals("bookRoom")) {
                String replyData = database.bookRoom(requestStringList[1], requestStringList[2], requestStringList[3], requestStringList[4], requestStringList[5]);
                buffer = replyData.getBytes();
                parameters = "User:" + requestStringList[1] + ", Date: " + requestStringList[3] + ", Room number: " + requestStringList[4] + ", Time slot:" + requestStringList[5] + ".";
                if (replyData.isEmpty()) {
                    result = "failed";
                    response = "No Booking ID.";
                } else {
                    result = "success";
                    response = "Booking ID: " + replyData;
                }
            } else if (requestStringList[0].equals("cancelBooking")) {
                String replyData = "";
                if (database.cancelBooking(requestStringList[1], requestStringList[2]))
                    replyData = "true";
                else
                    replyData = "false";
                buffer = replyData.getBytes();
                parameters = "User:" + requestStringList[1] + ", BookingID: " + requestStringList[2] + ".";
                if (replyData.equals("true")) {
                    result = "success";
                    response = "Cancel Booking Successed.";
                } else {
                    result = "false";
                    response = "cancel Booking failed.";
                }
            } else if (requestStringList[0].equals("modifyCount")) {
                if (requestStringList[3].equals("+1"))
                    database.modifyBookingCount(requestStringList[1], requestStringList[2], 1);
                else
                    database.modifyBookingCount(requestStringList[1], requestStringList[2], -1);
                buffer = "modifyCount".getBytes();
                parameters = "User:" + requestStringList[1] + ", Date: " + requestStringList[2] + ".";
                result = "success";
                response = "Modify Booking Count Success.";
            } else if (requestStringList[0].equals("getBookingCount")) {
                int x = database.getBookingCount(requestStringList[1], requestStringList[2]);
                String replyData = Integer.toString(x);
                buffer = replyData.getBytes();
                parameters = "User:" + requestStringList[1] + ", Date: " + requestStringList[2] + ".";
                result = "success";
                response = "Booking Count :" + replyData+" .";
            } else if (requestStringList[0].equals("getCancelAvailable")){
                boolean flag = database.getCancelAvailable(requestStringList[1], requestStringList[2]);
                System.out.println("flag is"+flag);
                String replyData = flag + "";
                buffer = replyData.getBytes();
                parameters = "User:" + requestStringList[1] + ", BookingID: " + requestStringList[2] + ".";
                result = "success";
                response = "Get Cancel is " + replyData + " .";
            } else if (requestStringList[0].equals("getRoomRecordAvailable")){
                boolean flag = database.getRoomRecordAvailable(requestStringList[1], requestStringList[2], requestStringList[3], requestStringList[4], requestStringList[5]);
                System.out.println("flag is"+flag);
                String replyData = flag + "";
                buffer = replyData.getBytes();
                parameters = "User:" + requestStringList[1] + ", BookingID: " + requestStringList[2] + ".";
                result = "success";
                response = "Get Room Record is " + replyData + " .";
            }

            serverLog.writeFile(requestStringList[0], parameters, result, response);
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length,
                    address, port);
            socket.send(reply);
        } catch (Exception e){
            System.out.println("Handle UDP error!");
        }
    }

    public void exportUDP(){
        System.out.println(campus+" UDP");
        // create socket at agreed port
        try {
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);
                requestString = "";
                for (byte b : request.getData()) {
                    if (b != 0)
                        requestString += (char) b;
                }
                System.out.println(requestString);
                requestString.trim();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handleUDPRequest(requestString, request.getAddress(), request.getPort());
                    }
                }).start();
            }
        }catch (Exception e){
            System.out.println(e+"UDP error");
        }
    }

    public void exportRPC(String[] args){
        try {
    // create and initialize the ORB
            ORB orb = ORB.init(args, null);
    // get reference to rootpoa & activate the POAManager
            POA rootpoa =
                    (POA) orb.resolve_initial_references("RootPOA");
            rootpoa.the_POAManager().activate();
    // create servant and register it with the ORB
            this.setORB(orb);
    // get object reference from the servant
            org.omg.CORBA.Object ref =
                    rootpoa.servant_to_reference(this);
    // and cast the reference to a CORBA reference
            Service href = ServiceHelper.narrow(ref);
            // get the root naming context
    // NameService invokes the transient name service
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
    // Use NamingContextExt, which is part of the
    // Interoperable Naming Service (INS) specification.
            NamingContextExt ncRef =
                    NamingContextExtHelper.narrow(objRef);
    // bind the Object Reference in Naming
            String name = campus;
            NameComponent path[] = ncRef.to_name(name);
            ncRef.rebind(path, href);
            System.out.println("HelloServer ready and waiting ...");
    // wait for invocations from clients
            orb.run();
        } catch (Exception e) {
            System.out.println(e+"Corba Error");
        }
    }

    private void startServerThreads(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                exportUDP();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                exportRPC(args);
            }
        }).start();

    }
}