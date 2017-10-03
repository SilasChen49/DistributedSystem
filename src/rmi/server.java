package rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.TimerTask;

public class server {

    static String x = "sbyo";
    static state state = new state();
    public static void main(String[] args) throws Exception{
        //创建服务端
        try {
            //创建服务端
            AddInterface hello = new AddClass(state);
            //注册1098号端口，注意这一步注册可以注册到别的机器上。
            LocateRegistry.createRegistry(8000);
            Timer timer = new Timer();
            timer.schedule(new TimerTaskTest(), 1000, 2000);
            //绑定服务端到指定的地址，这里的localhost对应的上一步注册端口号的机器
            Naming.rebind("rmi://localhost:8000/URLDispatcher", hello);
            System.out.println("Ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
