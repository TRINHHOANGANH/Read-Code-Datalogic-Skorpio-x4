package com.example.rtcvision.Main.Main;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GlobVar extends AppCompatActivity
{
    private final String fileName ="Settings.txt";
    String myData = "";
    public void SendData(String mode,String numberchar){
        try
        {
            File file = new File(getFilesDir(), fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(mode);
        }
        catch (IOException ex){
                ex.printStackTrace();
        }
    }
    public void ReadData(){
        try {
            FileInputStream fis = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
