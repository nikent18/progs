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
import java.util.Scanner;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class SMTPClient {

    static PrintStream ps = null;
    static DataInputStream dis = null;
    static Socket smtp;
    static SocketFactory factory;
    static String SMTPServer;

    SMTPClient() throws IOException {
        factory = SSLSocketFactory.getDefault();

        smtp = factory.createSocket("smtp.yandex.ru", 465);
        OutputStream os = smtp.getOutputStream();
        ps = new PrintStream(os);
        InputStream is = smtp.getInputStream();
        dis = new DataInputStream(is);
    }

    public static String encodeString(String str) {
        byte[] b = str.getBytes();
        String encodedStr = Base64.getEncoder().encodeToString(b);
        return encodedStr;
    }

    public static boolean isAddressCorrect(String addr) {
        if (addr.length() < 6) {
            return false;
        }
            boolean b=addr.indexOf('@') == -1 || addr.indexOf('.') == -1;
        if (b) {
            return false;
        }
        String domain = addr.substring(addr.indexOf('.') + 1);
        for (int i = 0; i < domain.length(); i++) {
            if (domain.charAt(i) >= 'a' && domain.charAt(i) <= 'z' || domain.charAt(i) >= 'A'
                    && domain.charAt(i) <= 'Z') {
                continue;
            } else {
                return false;
            }
        }
    
        if ((addr.indexOf('@') == 0 
                || addr.indexOf('@') == (addr.indexOf('.') + 1)
                || addr.indexOf('@') == (addr.indexOf('.') - 1))) {
            return false;
        }

        return true;
    }
   
    public static void main(String args[]) throws UnsupportedEncodingException, IOException {

        new SMTPClient();
        ServerConnection con = new ServerConnection();

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter dest");
        con.dest = scan.nextLine();
        if (!isAddressCorrect(con.dest)) {
            return;
        }
        System.out.println("Enter login");
        con.source = scan.nextLine();
        if (!isAddressCorrect(con.source)) {
            return;
        }
        System.out.println("Enter password");
        con.pass = scan.nextLine();

        con.sendMail(encodeString(con.pass), encodeString(con.source), ps, dis);
    }
    

}
