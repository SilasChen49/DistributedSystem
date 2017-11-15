package rmi;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLog {

    String serverName = "";
    String fileName = "";
    public ServerLog(String serverName){
        this.serverName = serverName;
        fileName = "src/rmi/serverLog/" + serverName+".txt";
    }

    public void writeFile(String requestType, String parameters, String result, String response){
        try{
            FileWriter writer = new FileWriter(fileName, true);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            writer.append("Time: "+ LocalDateTime.now().format(formatter)+"\n");
            writer.append("Request Type: "+requestType+"\n");
            writer.append("Parameters: "+parameters+"\n");
            writer.append("Request Result:" + result+"\n");
            writer.append("Response:" + response+"\n\n");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
