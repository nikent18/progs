/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftpserver1;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author nikita
 */
public class WorkWithDirectory {

    private File[] files;

    ArrayList<String> fileNames = new ArrayList();
    ArrayList<String> fileParams = new ArrayList();
    Utils util;
    String DIR;

    WorkWithDirectory() {
        DIR = "C:\\FTP SERVER DIRECTORY";
        util = new Utils();
    }

    private void getListFiles(String path) {
        File myFolder = new File(path);
        files = myFolder.listFiles();
    }

    public void fileParams(String path) {
        getListFiles(path);

        for (int i = 0; i < files.length; i++) {
            fileParams.add(files[i].toString() + " " + util.getTime(files[i].lastModified()) + " "
                    + files[i].length());
        }

    }
 public String cwd(String dirName, String curDir) {
        StringTokenizer st;
        String tmpDir=null;
        int index;
        if (dirName.equalsIgnoreCase("..")) {
            if (curDir.equalsIgnoreCase(DIR)) {
                return DIR;
            }
            else {
                index=curDir.lastIndexOf("\\");
                return curDir.substring(0, index);
            }
        }
        else {
            return curDir+"\\"+dirName;
        }
    }
    public int getFileNames(String path) {
        getListFiles(path);
        String tmpStr;
        StringTokenizer st;
        for (int i = 0; i < files.length; i++) {

            tmpStr = files[i].toString();
            st = new StringTokenizer(tmpStr, "\\");
            while (true) {
                if (st.countTokens() != 1) {
                    st.nextElement();
                } else {
                    fileNames.add(st.nextToken());
                    break;
                }
            }

        }
        return files.length;
    }

    public void sendFile(String name, DataOutputStream dout) throws IOException {
        File f = new File(name);
        if (!f.exists()) {
            dout.writeBytes("500 can`t find file\n");
            return;
        } else {

            FileInputStream fin = new FileInputStream(f);
            int ch = fin.read();
            while (ch != -1) {
                dout.writeByte((ch));
                ch = fin.read();
            }

            fin.close();
        }
    }

    public void receiveFile(String name, DataInputStream din, int size) throws FileNotFoundException, IOException {
        String filename = name;
        byte[] bytes = new byte[size];
        int count;
        File f = new File(name);
        FileOutputStream fos = new FileOutputStream(f);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        while ((count = din.read(bytes)) > 0) {
            bos.write(bytes, 0, count);
        }
        bos.close();
        fos.close();
    }

    public void mkDir(String dirName) {
        File f = new File(dirName);
        f.mkdir();
    }

    public void deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                File f = new File(dir, children[i]);
                deleteDir(f);
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }

    public void delereFile(String FileName) {
        File f = new File(FileName);
        f.delete();

    }

   
}
