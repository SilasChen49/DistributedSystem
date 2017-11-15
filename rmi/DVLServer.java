package rmi;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class DVLServer {

    public static void main(String[] args) throws Exception {
        // create server
        DatagramSocket DVLSocket;
        Database DVLDatabase = Database.getInstance();

        try {
            // registetry RMI
            ServiceInterface service = new ServiceImpl(DVLDatabase, "DVL");
            // create registry port for service
            LocateRegistry.createRegistry(6000);
            // bind service to specific url
            Naming.rebind("rmi://localhost:6000/DVL", service);
            System.out.println("DVL Server is Ready");

            // UDP socket
            DVLSocket = new DatagramSocket(6999);
            // create socket at agreed port
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                DVLSocket.receive(request);
                String requestString = new String();
                for (byte b : request.getData()) {
                    if (b != 0)
                        requestString += (char) b;

                }
                requestString.trim();
                String[] requestStringList = requestString.split(",");
                if (requestStringList[0].equals("getAvailableTimeSlot")) {
                    int availableNumber = DVLDatabase.getAvailableTimeSlot(requestStringList[1]);
                    String replyData = "DVL " + availableNumber + ", ";
                    buffer = replyData.getBytes();
                } else if (requestStringList[0].equals("bookRoom")) {
                    String replyData = DVLDatabase.bookRoom(requestStringList[1], requestStringList[2], requestStringList[3], requestStringList[4], requestStringList[5]);
                    buffer = replyData.getBytes();
                }else if (requestStringList[0].equals("cancelBooking")){
                    String replyData="";
                    if (DVLDatabase.cancelBooking(requestStringList[1], requestStringList[2]))
                        replyData = "true";
                    else
                        replyData="false";
                    buffer = replyData.getBytes();
                }else if (requestStringList[0].equals("modifyCount")){
                    if (requestStringList[2].equals("+1"))
                        DVLDatabase.modifyBookingCount(requestStringList[1], 1);
                    else
                        DVLDatabase.modifyBookingCount(requestStringList[1], -1);
                    buffer = "modifyCount".getBytes();
                }else if (requestStringList[0].equals("getBookingCount")){
                    if (Database.booking_count.containsKey(requestStringList[1])){
                        System.out.println("has it already:"+requestStringList[1]);
                        String replyData = Database.booking_count.get(requestStringList[1]).toString();
                        System.out.println(replyData);
                        buffer = replyData.getBytes();
                    }
                    else{
                        System.out.println("create a new one");
                        Database.booking_count.put(requestStringList[1], 0);
                        String replyData = "0";
                        buffer = replyData.getBytes();
                    }
                }
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length,
                        request.getAddress(), request.getPort());
                DVLSocket.send(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
