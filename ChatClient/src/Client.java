
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private Socket soc;
    private Scanner scan;
    private static final int PORT =9999;
    private PrintWriter out;
    private BufferedReader in;
    private boolean stop = false;
    private String str="";
    public Client(){
        try {
            soc = new Socket("localhost", PORT);
            out = new PrintWriter(soc.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            scan = new Scanner(System.in);
            Receiver recv = new Receiver();
            recv.start();
            chat();

        }
        catch (Exception e) {
            System.err.println("Не могу подключиться");
        }
        finally {
            close();
        }
    }

    private void chat(){
        while (!str.equalsIgnoreCase("quit")){
            str=scan.nextLine();
            out.println(str);
        }
        stop=true;
    }
    private void close()  {
        try {
            in.close();
            out.close();
            soc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private class Receiver extends Thread {
        public void run (){
            while (!stop){
                try {
                    System.out.println(in.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String [] args){
        new Client();
    }
}
