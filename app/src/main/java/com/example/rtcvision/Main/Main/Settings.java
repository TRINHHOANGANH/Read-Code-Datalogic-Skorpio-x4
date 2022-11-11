package com.example.rtcvision.Main.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.rtcvision.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.rtcvision.Main.Main.GlobVar;

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

public class Settings extends AppCompatActivity {

    EditText editText;
    Switch chkCompare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editText = (EditText)findViewById(R.id.ed_NumberCharCompare);
        chkCompare = (Switch) findViewById(R.id.chk_compare);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Code);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Scan:
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Code:
                        return true;
                    case R.id.Info:
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //ReadData();
        SwitchControl();
        editTextControl();
    }
    private void SwitchControl(){
        chkCompare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkCompare.isChecked()){
                        editText.setEnabled(false);
                    }
                    else{
                        editText.setEnabled(true);
                    }
            }
        });
    }
    private void editTextControl(){
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                   /* String myData ="";
                    try {
                        FileInputStream fis = new FileInputStream("fileName.txt");
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
                    }*/
                    //writeFileOnInternalStorage(this,"123.txt","123456");
                    try {

                        FileOutputStream fOut = openFileOutput("/data/data/com.example.rtcvision/123.txt",MODE_WORLD_READABLE);
                        fOut.write("data".getBytes());
                        fOut.close();

                        //Toast.makeText(getBaseContext(), "file saved", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return true;
                    }
                }
                return false;
            }
        });
    }
    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File dir = new File(mcoContext.getFilesDir(), "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}