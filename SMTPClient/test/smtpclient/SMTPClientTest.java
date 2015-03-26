/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smtpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nikita
 */
public class SMTPClientTest {

    SMTPClient client;
    ServerConnection serv;
    protected final ByteArrayOutputStream output = new ByteArrayOutputStream();
    Mail mail;

    public SMTPClientTest() {
    }

    @Before
    public void setup() {
        client = new SMTPClient();
        mail = new Mail();
        serv = new ServerConnection("smtp.yandex.ru", 465);
        System.setOut(new PrintStream(output));
    }

    @After
    public void cleanStream() {
        System.setOut(null);
    }

    @Test
    public void encodeTest() {
        String str = SMTPClient.encodeString("Username");
        assertEquals("Wrong encoding", str, "VXNlcm5hbWU=");
    }

    @Test
    public void createObj() {
        assertNotNull(new SMTPClient());
        assertNotNull(new Mail());
        assertNotNull(new ServerConnection("smtp.yandex.ru", 465));
    }

    @Test
    public void testSend() {
        serv.send("test");
        assertEquals("sent: test\r\n", output.toString());

    }

 
    @Test
    public void testBody() {
        String expected, text;
        text = "test";
        mail.BODY = text;
        expected = "X-MAILER: NIKMailer\r\n" + "'MIME-Version: 1.0\r\n"
                + "CONTENT-TYPE: text/plain\r\n" + "\r\n" + text + "\r\n.\r\n";
        assertEquals(expected, mail.mailBody(text));
    }

    @Test(expected = Exception.class)
    public void testAddr() {
        String correctAddr = "addr@addr.ru";
        String incorrectAddr1 = "addr@addr.ru1";
        String incorrectAddr2 = "a@b.r";
        String incorrectAddr3 = "@addr.ru";
        String incorrectAddr4 = "arrdg.gggggggg";
        String incorrectAddr5 = "addr@.ru";
        String incorrectAddr6 = "addr.@ru";

        assertTrue(client.isAddressCorrect(correctAddr));
        assertFalse(client.isAddressCorrect(null));
        assertFalse(client.isAddressCorrect(incorrectAddr1));
        assertFalse(client.isAddressCorrect(incorrectAddr2));
        assertFalse(client.isAddressCorrect(incorrectAddr3));
        assertFalse(client.isAddressCorrect(incorrectAddr4));
        assertFalse(client.isAddressCorrect(incorrectAddr5));
        assertFalse(client.isAddressCorrect(incorrectAddr6));
    }

    @Test(expected = Exception.class)
    public void checkInput() throws IOException {
        serv.checkInput(null);
    }

}
