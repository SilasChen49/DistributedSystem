package rmi;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class WSTServer {

    public static void main(String[] args) throws Exception {
        // create server
        DatagramSocket WSTSocket;
        Database WSTDatabase = Database.getInstance();

        try {
            // registetry RMI
            ServiceInterface service = new ServiceImpl(WSTDatabase, "WST");
            // create registry port for service
            LocateRegistry.createRegistry(8000);
            // bind service to specific url
            Naming.rebind("rmi://localhost:8000/WST", service);
            System.out.println("WST Server is Ready");

            // UDP socket
            WSTSocket = new DatagramSocket(8999);
            // create socket at agreed port
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                WSTSocket.receive(request);
                String requestString = new String();
                for (byte b : request.getData()) {
                    if (b != 0)
                        requestString += (char) b;

                }
                requestString.trim();
                String[] requestStringList = requestString.split(",");
                if (requestStringList[0].equals("getAvailableTimeSlot")) {
                    int availableNumber = WSTDatabase.getAvailableTimeSlot(requestStringList[1]);
                    String replyData = "WST " + availableNumber + ".";
                    buffer = replyData.getBytes();
                } else if (requestStringList[0].equals("bookRoom")) {
                    String replyData = WSTDatabase.bookRoom(requestStringList[1], requestStringList[2], requestStringList[3], requestStringList[4], requestStringList[5]);
                    buffer = replyData.getBytes();
                }else if (requestStringList[0].equals("cancelBooking")){
                    String replyData="";
                    if (WSTDatabase.cancelBooking(requestStringList[1], requestStringList[2]))
                        replyData = "true";
                    else
                        replyData="false";
                    buffer = replyData.getBytes();
                }else if (requestStringList[0].equals("modifyCount")){
                    if (requestStringList[2].equals("+1"))
                        WSTDatabase.modifyBookingCount(requestStringList[1], 1);
                    else
                        WSTDatabase.modifyBookingCount(requestStringList[1], -1);
                    buffer = "modifyCount".getBytes();
                }else if (requestStringList[0].equals("getBookingCount")){
                    if (Database.booking_count.containsKey(requestStringList[1])){
                        String replyData = Database.booking_count.get(requestStringList[1]).toString();
                        buffer = replyData.getBytes();
                    }
                    else{
                        Database.booking_count.put(requestStringList[1], 0);
                        String replyData = "0";
                        buffer = replyData.getBytes();
                    }
                }
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length,
                        request.getAddress(), request.getPort());
                WSTSocket.send(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
