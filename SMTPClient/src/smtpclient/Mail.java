/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smtpclient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nikita
 */
public class Mail {

    String BODY = "";
    String SUBJ;
    String XMAILER = "X-MAILER: NIKMailer\r\n";
    String MIME = "'MIME-Version: 1.0\r\n";
    String CONTENT_TYPE = "CONTENT-TYPE: text/plain\r\n";
    String END = "\r\n.\r\n";
    public String mailSubj() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter subject");
        SUBJ = scan.nextLine();
        return SUBJ;
    }
    public String mailBody(String body) {
       BODY=XMAILER+MIME+CONTENT_TYPE+"\r\n"+body+ END;
       return BODY;
    }

}
