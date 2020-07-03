package org.kxsj.com.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class IOUtil {

    public static byte[] read(String fileName,String encode) {
        String fileContent = "";
        try {
            File f = new File(fileName);
            if (f.isFile() && f.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(f), encode);
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent += line + "\n";
                }
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent.getBytes();
    }

    public static void writeAsText(String fileName, String fileContent,String encode) {
        System.out.println("write path:"+fileName);
        System.out.println("write encode:"+encode);
        try {
            File f = new File(fileName);
            if (!f.exists()) {
                System.out.println("notexist");
                f.createNewFile();
            } else {
                System.out.println("exist");
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f), encode);
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(fileContent);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

