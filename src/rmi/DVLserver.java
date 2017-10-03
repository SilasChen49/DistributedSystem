package rmi;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Timer;

public class DVLserver {

    static Database database = new Database();

    public static void main(String[] args) throws Exception {
        // create server
        DatagramSocket DVLSocket;
        try {
            // registetry RMI
            AdminServiceInterface adminService = new AdminServiceImpl(database);
            StudentServiceInterface studentService = new StudentServiceImpl(database);
            // create registry port for service
            LocateRegistry.createRegistry(6000);
            LocateRegistry.createRegistry(6001);
            // bind service to specific url
            Naming.rebind("rmi://localhost:6000/DVLStudent", studentService);
            Naming.rebind("rmi://localhost:6001/DVLAdmin", adminService);
            System.out.println("DVL server is Ready");

            // UDP socket
            DVLSocket = new DatagramSocket(6999);
            // create socket at agreed port
            byte[] buffer = new byte[1000];
            while(true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                DVLSocket.receive(request);
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(),
                        request.getAddress(), request.getPort());
                DVLSocket.send(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
