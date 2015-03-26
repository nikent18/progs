/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smtpclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author nikita
 */
public class ServerConnection {

    PrintStream ps = null;
    DataInputStream dis = null;
    Socket smtp;
    SocketFactory factory;
    String SMTPServer;
    int PORT;
    Mail mail;
    String pass;
    String source;
    String dest;

    String HELO = "HELO ";
    String MAIL_FROM = "MAIL FROM: ";
    String RCPT_TO = "RCPT TO: ";

    String DATA = "DATA";
    String AUTH = "AUTH LOGIN";
    String SUBJECT = "SUBJECT: ";
    String TO = "TO: " + dest;
    String QUIT = "QUIT";
    String loc;

    public ServerConnection(String serv, int port) {
        mail = new Mail();

        factory = SSLSocketFactory.getDefault();
        SMTPServer = serv;
        PORT = port;
        try {
            smtp = smtp = factory.createSocket(SMTPServer, PORT);
            OutputStream os = smtp.getOutputStream();
            ps = new PrintStream(os);
            InputStream is = smtp.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send(String str) {
        ps.println(str);      // посылка строки на SMTP
        System.out.println("sent: " + str);
    }
    /*
     public String receive() throws IOException {
     String readstr;
     readstr = dis.readLine(); // получение ответа от SMTP
     //   System.out.println("SMTP respons: " + readstr);
     return readstr;
     }
     */

    public void checkInput(DataInputStream dis) throws IOException {
        String readstr;

        readstr = dis.readLine();
        System.out.println("SMTP respons: " + readstr);
        if (readstr.charAt(0) == '5' || readstr.charAt(0) == '4') {
            return;
        }

    }

    public String enterMessage() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter message");
        String tmp;
        String body = "";
        while (true) {
            tmp = scan.nextLine();
            if (tmp.equals(".")) {
                break;
            }
            body = body + " " + tmp;
        }
        return body;
    }
    
    public void sendMail(String pass, String login) {
        try {
            String tmp;
            send(HELO + loc);
            checkInput(dis);          // получение ответа SMTP
            send(AUTH);
            checkInput(dis);
            send(login);
            checkInput(dis);
            send(pass);
            checkInput(dis);
            send(MAIL_FROM + source);
            checkInput(dis);
            send(RCPT_TO + dest);
            checkInput(dis);
            send(DATA);
            checkInput(dis);

            send("FROM: " + source);

            send(SUBJECT + mail.mailSubj());

            send("TO: " + dest);
            checkInput(dis);

            tmp = enterMessage();
            //    System.out.println(tmp);
            send(mail.mailBody(tmp));
            checkInput(dis);
            send(QUIT);
        } catch (IOException e) {
            System.out.println("Error sending: " + e);
        }

    }
}
