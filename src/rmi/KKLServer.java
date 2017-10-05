package rmi;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class KKLServer {

    public static void main(String[] args) throws Exception {
        // create server
        DatagramSocket KKLSocket;
        Database KKLDatabase = Database.getInstance();

        try {
            // registetry RMI
            ServiceInterface service = new ServiceImpl(KKLDatabase, "KKL");
            // create registry port for service
            LocateRegistry.createRegistry(7000);
            // bind service to specific url
            Naming.rebind("rmi://localhost:7000/KKL", service);
            System.out.println("KKL Server is Ready");

            // UDP socket
            KKLSocket = new DatagramSocket(7999);
            // create socket at agreed port
            while (true) {
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                KKLSocket.receive(request);
                String requestString = new String();
                for (byte b : request.getData()) {
                    if (b != 0)
                        requestString += (char) b;

                }
                requestString.trim();
                String[] requestStringList = requestString.split(",");
                if (requestStringList[0].equals("getAvailableTimeSlot")) {
                    int availableNumber = KKLDatabase.getAvailableTimeSlot(requestStringList[1]);
                    String replyData = "KKL " + availableNumber + ", ";
                    buffer = replyData.getBytes();
                } else if (requestStringList[0].equals("bookRoom")) {
                    System.out.println(requestStringList[1]+requestStringList[2]+ requestStringList[3]+ requestStringList[4]+ requestStringList[5]);
                    String replyData = KKLDatabase.bookRoom(requestStringList[1], requestStringList[2], requestStringList[3], requestStringList[4], requestStringList[5]);
                    buffer = replyData.getBytes();
                }else if (requestStringList[0].equals("cancelBooking")){
                    String replyData="";
                    if (KKLDatabase.cancelBooking(requestStringList[1], requestStringList[2]))
                        replyData = "true";
                    else
                        replyData="false";
                    buffer = replyData.getBytes();
                }else if (requestStringList[0].equals("modifyCount")){
                    if (requestStringList[2].equals("+1"))
                        KKLDatabase.modifyBookingCount(requestStringList[1], 1);
                    else
                        KKLDatabase.modifyBookingCount(requestStringList[1], -1);
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
                KKLSocket.send(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
