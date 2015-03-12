/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smtpclient;

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
    
    public SMTPClientTest() {
    }
    
    @Test
    public void encodeTest(){
        String str =SMTPClient.encodeString("Username");
        assertEquals("Wrong coding", str, "VXNlcm5hbWU=");
    }
    
    
    /**
     * Test of main method, of class SMTPClient.
     */
/*    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        SMTPClient.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
  */  
}
