package rmi;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class client {

    public static void main(String[] args) throws Exception{
        try {
            //客户端查找指定的服务
            AddInterface hello = (AddInterface) Naming.lookup("rmi://localhost:8000/URLDispatcher");
            //打印的结果应该是www.baidu.com
            System.out.println(hello.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
