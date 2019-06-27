package com.talnex.wrongsbook.File;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class MbFile {
    public static String FILENAME = "hello";
    public static String Mbfile = "/sdcard/mindbook/"+FILENAME+"/";
    static String filenameTemp = Mbfile + "/tree" + ".txt";

    public static void writeTreeFile(String text){
        File file = new File(Mbfile);
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        File dir = new File(filenameTemp);
        if (!dir.exists()) {
            try {
                dir.createNewFile();
            } catch (Exception e) {
            }
        }

        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(filenameTemp, false);//
            // 创建FileWriter对象，用来写入字符流
            bw = new BufferedWriter(fw); // 将缓冲对文件的输出

            bw.write(text + "\n"); // 写入文件
            bw.newLine();
            bw.flush(); // 刷新该流的缓冲
            bw.close();
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
            }
        }



    }
    public static String readTreeFile(){
        try {
            FileInputStream fis= new FileInputStream(filenameTemp);
            byte[] buff=new byte[1024];
            int hasRead=0;
            StringBuffer sb=new StringBuffer();
            while ((hasRead=fis.read(buff))>0){
                sb.append(new String(buff,0,hasRead));
            }
            fis.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void ziptoMbFile(){

    }

    public static void unzipMbFile(){

    }
}
