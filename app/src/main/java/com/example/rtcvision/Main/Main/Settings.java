package com.example.rtcvision.Main.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import com.example.rtcvision.Main.Main.GlobVar;

public class Settings extends AppCompatActivity {

    EditText editText;
    Switch chkCompare;
    GlobVar globVar;
    Spinner spin_format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editText = (EditText)findViewById(R.id.ed_NumberCharCompare);
        chkCompare = (Switch) findViewById(R.id.chk_compare);
        spin_format = (Spinner) findViewById(R.id.spin_format);
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
                        startActivity(new Intent(getApplicationContext(),LogData.class));
                        return true;
                }
                return false;
            }
        });

        globVar = new GlobVar();
        String swmode = globVar.ReadFileFromES().split("\n")[0].split("=")[1];
        String numberchar = globVar.ReadFileFromES().split("\n")[1].split("=")[1];
        globVar.FileOrTCP = Boolean.parseBoolean( globVar.ReadFileFromES().split("\n")[2].split("=")[1]);
        globVar.IP = globVar.ReadFileFromES().split("\n")[3].split("=")[1];
        globVar.Port = globVar.ReadFileFromES().split("\n")[4].split("=")[1];
        globVar.FormatFileSave = globVar.ReadFileFromES().split("\n")[5].split("=")[1];
        chkCompare.setChecked(Boolean.parseBoolean(swmode.trim().toLowerCase().trim()));
        if (chkCompare.isChecked()) editText.setEnabled(false);

        editText.setText(numberchar);
        SwitchControl();
        editTextControl();
        SpinerControl();
    }

    @SuppressLint("ResourceType")
    private void SpinerControl()
    {
        ArrayList<String> fileFormatArr= new ArrayList<String>();
        fileFormatArr.add(".txt");
        fileFormatArr.add(".csv");
        ArrayAdapter arr= new ArrayAdapter(this, android.R.layout.simple_spinner_item,fileFormatArr);
        arr.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spin_format.setAdapter(arr);
        if(globVar.FormatFileSave.equals(".txt"))         {
           spin_format.setSelection(0);
        }
        else{
            spin_format.setSelection(1);
        }
        spin_format.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String x = fileFormatArr.get(position);
                    globVar.FormatFileSave=fileFormatArr.get(position);
                    globVar.WriteToFileES("Mode="+ chkCompare.isChecked() +"\n" +"NumberChar=" +editText.getText() +"\n" + "FileOrTCP="+ globVar.FileOrTCP + "\n" + "IP="+ globVar.IP +"\n"+"Port="+globVar.Port +"\n"+ "FormatFile=" +globVar.FormatFileSave
                    );

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void SwitchControl(){
        chkCompare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (chkCompare.isChecked()){
                        editText.setEnabled(false);
                        globVar.WriteToFileES("Mode="+ chkCompare.isChecked() +"\n" +"NumberChar=" +editText.getText() +"\n" + "FileOrTCP="+ globVar.FileOrTCP + "\n" + "IP="+ globVar.IP +"\n"+"Port="+globVar.Port +"\n"+ "FormatFile=" +globVar.FormatFileSave);
                    }
                    else{
                        editText.setEnabled(true);
                        globVar.WriteToFileES("Mode="+ chkCompare.isChecked() +"\n" +"NumberChar=" +editText.getText() +"\n" + "FileOrTCP="+ globVar.FileOrTCP + "\n" + "IP="+ globVar.IP +"\n"+"Port="+globVar.Port +"\n"+ "FormatFile=" +globVar.FormatFileSave);
                    }
            }
        });
    }
    private void editTextControl(){
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String y = editText.getText().toString().trim();
                if(editText.getText().toString().trim().equals("")){
                    Toast.makeText(Settings.this,"Mời bạn nhập số kí tự so sánh",Toast.LENGTH_SHORT).show();
                }else{
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER))
                    {
                        globVar.WriteToFileES("Mode="+ chkCompare.isChecked() +"\n" +"NumberChar=" +editText.getText() +"\n" + "FileOrTCP="+ globVar.FileOrTCP + "\n" + "IP="+ globVar.IP +"\n"+"Port="+globVar.Port+"\n"+ "FormatFile=" +globVar.FormatFileSave );
                    }
                }
                return false;
            }

        });
    }

}