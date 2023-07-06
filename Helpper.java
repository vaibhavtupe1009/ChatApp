

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 *
 * @author Gauri
 */
public class Helpper {
    public static void startReader(BufferedReader br, String s,Socket socket){
        Runnable r1 = () ->{
        
        System.out.println("Reader Started...");
        try {
            while(!socket.isClosed()){
                String msg = br.readLine();
                if(msg.equalsIgnoreCase("EXIT")){
                    System.out.println("Closed The Connection!");
                    socket.close();
                    break;
                }
                if(s == "Server")
                    System.out.println("Client : "+msg);
                else
                    System.out.println("Server : "+msg);
            }
        } catch (Exception e) {
            System.out.println("Connection Closed");
        }
        };
        new Thread(r1).start();
        
    }
    public static void startWriter(PrintWriter out,Socket socket){
        Runnable r2 = ()->{
        System.out.println("Writer Started..");
        try {
            while(!socket.isClosed()){
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String content = br.readLine();
                out.println(content);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        };
        new Thread(r2).start();
    }
}
