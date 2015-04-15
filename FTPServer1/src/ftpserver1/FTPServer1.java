/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftpserver1;

/**
 *
 * @author nikita
 */
//FtpServer.java
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FTPServer1 {

    public static void main(String args[]) throws Exception {
        ServerSocket soc = new ServerSocket(21);
        System.out.println("FTP Server Started on Port Number 21");
        while (true) {
            System.out.println("Waiting for Connection ...");
            transferfile t = new transferfile(soc.accept());

        }
    }
}

class transferfile extends Thread {

    Socket ClientSoc;
    String curDir;
    DataInputStream din, din2;
    DataOutputStream dout, dout2;
    WorkWithDirectory direct;

    transferfile(Socket soc) {
        try {
            // dataSoc= null;
            direct = new WorkWithDirectory();
            curDir = "C:\\FTP SERVER DIRECTORY";
            ClientSoc = soc;
            din = new DataInputStream(ClientSoc.getInputStream());
            dout = new DataOutputStream(ClientSoc.getOutputStream());
            System.out.println("FTP Client Connected ...");
            dout.writeBytes("220 FTP server ready\n");
            start();

        } catch (Exception ex) {
        }
    }

    Socket dataSoc;

    void connectNewPort(int port) {
        try {
            dataSoc = new Socket();
            //  dataSoc = new Socket("127.0.0.1", port, InetAddress.getByName(null), 20);
            dataSoc.bind(new InetSocketAddress(20));
            dataSoc.connect(new InetSocketAddress("::1", port));
            din2 = new DataInputStream(dataSoc.getInputStream());
            dout2 = new DataOutputStream(dataSoc.getOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(transferfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listCommand(int port) throws IOException {
        dout.writeBytes("150 ok, it`s xlist command\n");
        connectNewPort(port);
        direct.fileParams(curDir);
        for (int i = 0; i < direct.fileParams.size(); i++) {
            dout2.writeBytes(direct.fileParams.get(i) + "\n");
        }
        direct.fileParams.clear();
        dataSoc.close();
        dout.writeBytes("226 Transfer complete.\n");
    }

    public void xlistCommand(int port) throws IOException {
        dout.writeBytes("150 ok, it`s list command\n");
        connectNewPort(port);

        direct.getFileNames(curDir);
        for (int i = 0; i < direct.fileNames.size(); i++) {
            dout2.writeBytes(direct.fileNames.get(i) + "\n");
        }
        direct.fileNames.clear();
        dataSoc.close();
        dout.writeBytes("226 Transfer complete.\n");
    }

    public void sendCommand(int port, String fileName) throws IOException {
        dout.writeBytes("150 ok, it`s get command\n");
        connectNewPort(port);
        direct.sendFile(curDir + "\\" + fileName, dout2);
        dout2.close();
        dout.writeBytes("226 Transfer complete.\n");
    }

    public void putCommand(int port, String fileName) throws IOException {
        dout.writeBytes("150 ok, it`s send command\n");
        connectNewPort(port);
        direct.receiveFile(curDir + "\\" + fileName, din2, dataSoc.getReceiveBufferSize());
        din2.close();
        dout.writeBytes("226 Receiving complete.\n");
    }

    public void mkdirCommand(String dirName) throws IOException {
        //    dout.writeBytes("150 ok, it`s mkdir command\n");
        //      connectNewPort(port);
        direct.mkDir(curDir + "\\" + dirName);
        //      din2.close();
        dout.writeBytes("226 new directory ok\n");
    }

    public void deleteDirCommand(String dirName) throws IOException {
        File dir = new File(curDir + "\\" + dirName);
        direct.deleteDir(dir);
        dout.writeBytes("226 dir deleted\n");
    }

    public void deleteFileCommand(String fileName) throws IOException {
        direct.delereFile(curDir + "\\" + fileName);
        dout.writeBytes("226 file deleted\n");
    }
    public void cwdCommand(String dirName) throws IOException{
        curDir= direct.cwd(dirName, curDir);
        dout.writeBytes("226 directory changed\n");
    }
    public void xpwdCommand() throws IOException{
       dout.writeBytes("220"+" "+curDir+"\n");
    }
    public void quitCommand() throws IOException{
         dout.writeBytes("bye\n");
    }
    public void run() {
        int tmpPort = 0;
        try {
            String str2;
            String str = din.readLine();
            System.out.println(str);
            if (str.compareTo("USER user ftp") == 0) {
                dout.writeBytes("230 Login successful.\n");
            }
            while (true) {
                str = din.readLine();
                System.out.println(str);
                if (str.substring(0, 4).equalsIgnoreCase("EPRT")) {
                    tmpPort = Integer.parseInt(str.substring(12, 17));
                    System.out.println(tmpPort);
                    dout.writeBytes("220 Port command successful\n");
                    str = din.readLine();
                    System.out.println(str);
                }

                if (str.substring(0, 4).compareToIgnoreCase("NLST") == 0) {
                    xlistCommand(tmpPort);
                    continue;
                }
                if (str.substring(0, 4).compareToIgnoreCase("RETR") == 0) {
                    sendCommand(tmpPort, str.substring(5));
                    continue;
                }
                if (str.substring(0, 4).compareToIgnoreCase("STOR") == 0) {
                    putCommand(tmpPort, str.substring(5));
                    continue;
                }
                if (str.substring(0, 4).compareToIgnoreCase("XMKD") == 0) {
                    mkdirCommand(str.substring(5));
                    continue;
                }
                if (str.substring(0, 4).compareToIgnoreCase("XRMD") == 0) {
                    deleteDirCommand(str.substring(5));
                    continue;
                }
                if (str.substring(0, 4).compareToIgnoreCase("DELE") == 0) {
                    deleteFileCommand(str.substring(5));
                    continue;
                }
                if (str.substring(0, 4).compareToIgnoreCase("LIST") == 0) {
                    listCommand(tmpPort);
                    continue;
                }
                if (str.substring(0, 3).compareToIgnoreCase("CWD") == 0) {
                    cwdCommand((str.substring(4)));
                    continue;
                }
                if (str.substring(0, 4).compareToIgnoreCase("XPWD") == 0) {
                    xpwdCommand();
                    continue;
                }
                if (str.substring(0, 4).compareToIgnoreCase("QUIT") == 0) {
                    quitCommand();
                    continue;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(transferfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
