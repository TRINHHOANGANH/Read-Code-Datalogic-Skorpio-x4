package com.example.rtcvision.Main.Main;

import android.content.Context;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GlobVar extends AppCompatActivity
{
    public  static TcpClient mTcpClient;
    public static final String fileName ="/sdcard/Download/settings.txt";
    public static  String myData = "";
    public static String FILENAME = "Settings.txt";
    public static Boolean FileOrTCP = false;
    public static String IP = "192.168.1.13";
    public static String Port ="4000";
    public static Socket s;
    public static PrintWriter pw;
    public static String FormatFileSave=".txt";
    public static void WriteToFileES(String fcontent) {
        try {
            String x = Environment.getExternalStorageDirectory().toString();
            FileOutputStream fos = new FileOutputStream(
                    Environment.getExternalStorageDirectory() + "//" + FILENAME);
            fos.write(fcontent.getBytes());
            fos.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public static String ReadFileFromES() {
        String result = null;
        try {
            FileInputStream fis = new FileInputStream(
                    Environment.getExternalStorageDirectory() + "//"
                            + FILENAME);
            int flength = fis.available();
            if (flength != 0) {
                byte[] buffer = new byte[flength];
                if (fis.read(buffer) != -1) {
                    result = new String(buffer);
                }
            }
            fis.close();
        } catch (Exception e) {
        }

        return result;

    }
    public static void WriteLogDataToFileES(String fcontent, String fileName) {

        try {
            // kiểm tra folder có tồn tại
            String path=Environment.getExternalStorageDirectory() + "//DataCode";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            String x = Environment.getExternalStorageDirectory().toString();
            FileOutputStream fos = new FileOutputStream(
                    Environment.getExternalStorageDirectory() + "//DataCode//" + fileName,true);
            fos.write(fcontent.getBytes());
            fos.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public static void SenDataTCP(String fcontent) {

        try {
            // kiểm tra folder có tồn tại
           s = new Socket(IP,Integer.parseInt(Port));
           pw= new PrintWriter(s.getOutputStream());
           pw.write(fcontent);
           pw.flush();
           pw.close();
           s.close();
        } catch (Exception e) {
                String x =e.toString();
        }
    }

}
