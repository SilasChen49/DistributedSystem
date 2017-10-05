package rmi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class Client2 {
    public static void main(String[] args) throws Exception {
        UserClient userClient = new UserClient();
        userClient.run();
    }
}
