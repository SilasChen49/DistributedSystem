package rmi;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class UserLog {

    String userName = "";
    String fileName = "";
    public UserLog(String userName){
        this.userName = userName;
        fileName = "src/rmi/userLog/" + userName+".txt";
    }

    public void writeFile(String action, String result){
        try{
            FileWriter writer = new FileWriter(fileName, true);
            writer.append("action: "+action+"\n");
            writer.append("result: "+result+"\n\n");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
