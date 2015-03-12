/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smtpclient;

/**
 *
 * @author nikita
 */
import java.io.*;
import java.net.*;
import java.util.Base64;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class SMTPClient {

    static PrintStream ps = null;
    static DataInputStream dis = null;

    public static void send(String str) {
        ps.println(str);      // посылка строки на SMTP
        System.out.println("sent: " + str);
    }

    public static void receive() throws IOException {
        String readstr;
        readstr = dis.readLine(); // получение ответа от SMTP
        System.out.println("SMTP respons: " + readstr);

    }

    public static String encodeString(String str) {
        byte[] b = str.getBytes();
        String encodedStr = Base64.getEncoder().encodeToString(b);
        return encodedStr;
    }

    public static void sendMessage() {
        String pass = "pass";
        String source = "nikent18@yandex.ru";
        String dest = "nikent18@yandex.ru";
        String HELO = "HELO ";
        String MAIL_FROM = "MAIL FROM: " + source;
        String RCPT_TO = "RCPT TO: " + dest;
        String SUBJECT = "SUBJECT: Test message!";
        String DATA = "DATA";
        String AUTH = "AUTH LOGIN";
        String BODY = "\r\nHello, it`s message from Nikita!\r\n.\r\n";
        try {
            String loc = InetAddress.getLocalHost().getHostName();
            send(HELO + loc);
            receive();          // получение ответа SMTP
            send(AUTH);
            receive();
            send(encodeString(source));
            receive();
            send(encodeString(pass));
            receive();
            send(MAIL_FROM);
            receive();
            send(RCPT_TO);
            receive();
            send(DATA);
            receive();
            send(SUBJECT);
            receive();
            send(BODY);
            receive();
        } catch (IOException e) {
            System.out.println("Error sending: " + e);
        }
    }

    public static void main(String args[]) throws UnsupportedEncodingException {

        SocketFactory factory = SSLSocketFactory.getDefault();

        Socket smtp = null;

        try {
            smtp = factory.createSocket("smtp.yandex.ru", 465);
            OutputStream os = smtp.getOutputStream();
            ps = new PrintStream(os);
            InputStream is = smtp.getInputStream();
            dis = new DataInputStream(is);
        } catch (IOException e) {
            System.out.println("Error connection: " + e);
        }
        sendMessage();
        try {
            smtp.close();
        } catch (IOException e) {
            System.out.println("Can`t close socket");
        }

        System.out.println("Mail sent!");
    }
}
