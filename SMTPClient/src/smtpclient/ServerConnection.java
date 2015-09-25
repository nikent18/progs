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

    //   PrintStream ps = null;
//    DataInputStream dis = null;
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

    public ServerConnection() {
        mail = new Mail();

        //     factory = SSLSocketFactory.getDefault();
        //  SMTPServer = serv;
        //  PORT = port;
      /*  try {
         smtp = smtp = factory.createSocket(SMTPServer, PORT);
         OutputStream os = smtp.getOutputStream();
         ps = new PrintStream(os);
         InputStream is = smtp.getInputStream();
         dis = new DataInputStream(is);
         } catch (IOException ex) {
         Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
         }*/
    }

    public void send(String str, PrintStream ps1) {
        ps1.println(str);      // посылка строки на SMTP
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

    public boolean checkInput(DataInputStream dis) throws IOException {
        String readstr;

        readstr = dis.readLine();
        System.out.println("SMTP respons: " + readstr);
        if (readstr.charAt(0) == '5' || readstr.charAt(0) == '4') {
            return false;
        }
        return true;
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

    public boolean sendMail(String pass, String login, PrintStream ps1, DataInputStream dis1) {
        try {
            String tmp;
            send(HELO + loc, ps1);
            if (!checkInput(dis1)) // получение ответа SMTP
            {
                return false;
            }
            send(AUTH, ps1);
            if (!checkInput(dis1)) // получение ответа SMTP
            {
                return false;
            }
            send(login, ps1);
            if (!checkInput(dis1)) // получение ответа SMTP
            {
                return false;
            }
            send(pass, ps1);
            if (!checkInput(dis1)) // получение ответа SMTP
            {
                return false;
            }
            send(MAIL_FROM + source, ps1);
            if (!checkInput(dis1)) // получение ответа SMTP
            {
                return false;
            }
            send(RCPT_TO + dest, ps1);
            if (!checkInput(dis1)) // получение ответа SMTP
            {
                return false;
            }
            send(DATA, ps1);
            if (!checkInput(dis1)) // получение ответа SMTP
            {
                return false;
            }

            send("FROM: " + source, ps1);

            send(SUBJECT + mail.mailSubj(), ps1);

            send("TO: " + dest, ps1);
            if (!checkInput(dis1)) // получение ответа SMTP
            {
                return false;
            }

            tmp = enterMessage();
            //    System.out.println(tmp);
            send(mail.mailBody(tmp), ps1);
            if (!checkInput(dis1)) // получение ответа SMTP
            {
                return false;
            }
            send(QUIT, ps1);
        } catch (IOException e) {
            System.out.println("Error sending: " + e);
        }

        return true;
    }
}
