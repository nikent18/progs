import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Server {
    private int PORT =9999;
    private ServerSocket svSoc;
    private Socket soc;
    private final int messagesLim =5;

    private List <Connect> clients = Collections.synchronizedList(new ArrayList<Connect>());
    private List<String> names =Collections.synchronizedList(new ArrayList<String>());
    private List<String> messages =Collections.synchronizedList(new ArrayList<String>(messagesLim));
    public Server(){
        try {
            svSoc=new ServerSocket(PORT);

            System.out.println("Waiting...");
            while (true) {
                soc =svSoc.accept();
                Connect con= new Connect(soc);
                clients.add(con);
                con.start();
                System.out.println("connected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class Connect extends Thread {
        private PrintWriter out;
        private String nickname;
        private BufferedReader in;
        private String str="";

        public Connect(Socket soc){
            try {
                out = new PrintWriter(soc.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        @Override
        public void run() {
            try {
                addName();
                for (Connect c : clients){
                    c.out.println("new person: "+nickname);
                }
                if (!messages.isEmpty()) {
                    for (String s: messages)
                    out.println(s);
                }
                while (!str.equalsIgnoreCase("quit")){
                    str=in.readLine();
                    addMessage(str);
                    for (Connect c : clients){
                        c.out.println(nickname+" "+str);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                close();
            }

        }
        private void addMessage(String str){
            messages.add(nickname+": "+str);
            if (messages.size()>messagesLim) {
                messages.remove(0);
            }
        }
        private void addName() throws IOException {
            while (true) {
                out.println("Enter your nickname");

                    str = in.readLine();
                    if (!names.contains(str))
                        break;
            }
            nickname=str;
            names.add(nickname);
        }
        private void close() {
            try {
                in.close();
                out.close();
                soc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String [] args) {
         new Server();

    }
}
