package ClientMachine.ServiceApp;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserLog {

    String userName = "";
    String fileName = "";
    public UserLog(String userName){
        this.userName = userName;
        fileName = "src/ClientMachine/userLog/" + userName+".txt";
    }

    public void writeFile(String action, String result){
        try{
            FileWriter writer = new FileWriter(fileName, true);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            writer.append("Time: "+ LocalDateTime.now().format(formatter)+"\n");
            writer.append("action: "+action+"\n");
            writer.append("result: "+result+"\n\n");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
