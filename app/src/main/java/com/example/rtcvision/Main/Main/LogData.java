package com.example.rtcvision.Main.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.example.rtcvision.Main.Main.TcpClient;

public class LogData extends AppCompatActivity {

    GlobVar globVar;
    RadioButton btnFile,btnTCP;
    String swmode;
    String numberchar;
    public static Button btnConnect;
    EditText txtIP,txtPort,txtNameFile;
    TextView txt5,txt8,txt9;
    private Thread thread;
    //TcpClient mTcpClient;
    boolean connect_ok=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logdata);
        btnFile =(RadioButton)findViewById(R.id.btn_File);
        btnTCP =(RadioButton)findViewById(R.id.btn_TCP);
        btnConnect =(Button) findViewById(R.id.btn_Connect);
        txtIP =(EditText) findViewById(R.id.txtIP);
        txtPort =(EditText) findViewById(R.id.txtPort);
        txtNameFile =(EditText) findViewById(R.id.txt_Name);
        txt5 =(TextView) findViewById(R.id.textView5);
        txt8 =(TextView) findViewById(R.id.textView8);
        txt9 =(TextView) findViewById(R.id.textView9);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
        String datetime = simpleDateFormat.format(calendar.getTime());
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm:ss");
        String datetime1 = simpleDateFormat1.format(calendar.getTime());
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
                        startActivity(new Intent(getApplicationContext(),Settings.class));
                        return true;
                    case R.id.Info:
                        return true;
                }
                return false;
            }
        });
        globVar = new GlobVar();
        swmode = globVar.ReadFileFromES().split("\n")[0].split("=")[1].trim();
        numberchar = globVar.ReadFileFromES().split("\n")[1].split("=")[1];
        globVar.FileOrTCP = Boolean.parseBoolean( globVar.ReadFileFromES().split("\n")[2].split("=")[1]);
        globVar.IP = globVar.ReadFileFromES().split("\n")[3].split("=")[1];
        globVar.Port = globVar.ReadFileFromES().split("\n")[4].split("=")[1];
        globVar.FormatFileSave = globVar.ReadFileFromES().split("\n")[5].split("=")[1];

        txtIP.setText(globVar.IP);
        txtPort.setText(globVar.Port);
        txtNameFile.setText("DataCode/"+datetime+globVar.FormatFileSave);
        txtNameFile.setEnabled(false);
        if (!globVar.FileOrTCP){
            txt8.setVisibility(View.INVISIBLE);
            txt9.setVisibility(View.INVISIBLE);
            txtIP.setVisibility(View.INVISIBLE);
            txtPort.setVisibility(View.INVISIBLE);
            txt5.setVisibility(View.VISIBLE);
            txtNameFile.setVisibility(View.VISIBLE);
            btnConnect.setVisibility(View.INVISIBLE);
            btnFile.toggle();

        }

        else{
            txt8.setVisibility(View.VISIBLE);
            txt9.setVisibility(View.VISIBLE);
            txtIP.setVisibility(View.VISIBLE);
            txtPort.setVisibility(View.VISIBLE);
            txt5.setVisibility(View.INVISIBLE);
            txtNameFile.setVisibility(View.INVISIBLE);
            btnConnect.setVisibility(View.VISIBLE);
            btnTCP.toggle();

        }
        RadioButtonControl();
        EditTextControl();
        ConnectControl();

    }
    private void EditTextControl(){
        txtIP.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    globVar.IP = txtIP.getText().toString().trim();
                    globVar.WriteToFileES("Mode="+ swmode +"\n" +"NumberChar=" +numberchar +"\n" + "FileOrTCP="+ globVar.FileOrTCP + "\n" + "IP="+ globVar.IP +"\n"+"Port="+globVar.Port );
                }
                return false;
            }
        });
        txtPort.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    globVar.Port = txtPort.getText().toString().trim();
                    globVar.WriteToFileES("Mode="+ swmode +"\n" +"NumberChar=" +numberchar +"\n" + "FileOrTCP="+ globVar.FileOrTCP + "\n" + "IP="+ globVar.IP +"\n"+"Port="+globVar.Port +"\n"+ "FormatFile=" +globVar.FormatFileSave);
                }
                return false;
            }
        });
    }
    private void RadioButtonControl(){
        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globVar.FileOrTCP = false;
                globVar.WriteToFileES("Mode="+ swmode +"\n" +"NumberChar=" +numberchar +"\n" + "FileOrTCP="+ globVar.FileOrTCP + "\n" + "IP="+ globVar.IP +"\n"+"Port="+globVar.Port +"\n"+ "FormatFile=" +globVar.FormatFileSave);
                txt8.setVisibility(View.INVISIBLE);
                txt9.setVisibility(View.INVISIBLE);
                txtIP.setVisibility(View.INVISIBLE);
                txtPort.setVisibility(View.INVISIBLE);
                txt5.setVisibility(View.VISIBLE);
                btnConnect.setVisibility(View.INVISIBLE);
                txtNameFile.setVisibility(View.VISIBLE);
//                if (GlobVar.mTcpClient != null) {
//                    GlobVar.mTcpClient.sendMessage("testing");
//                }
            }
        });
        btnTCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globVar.FileOrTCP = true;
                globVar.WriteToFileES("Mode="+ swmode +"\n" +"NumberChar=" +numberchar +"\n" + "FileOrTCP="+ globVar.FileOrTCP + "\n" + "IP="+ globVar.IP +"\n"+"Port="+globVar.Port +"\n"+ "FormatFile=" +globVar.FormatFileSave);
                txt8.setVisibility(View.VISIBLE);
                txt9.setVisibility(View.VISIBLE);
                txtIP.setVisibility(View.VISIBLE);
                txtPort.setVisibility(View.VISIBLE);
                txt5.setVisibility(View.INVISIBLE);
                btnConnect.setVisibility(View.VISIBLE);
                txtNameFile.setVisibility(View.INVISIBLE);
            }
        });
    }
    public class Test{
        private void ShowTest(){
            if (connect_ok == true){
                btnConnect.setText("CONNECTED");
                btnConnect.setBackgroundColor(Color.GREEN);
            }
            else {
                btnConnect.setText("CONNECT");
                btnConnect.setBackgroundColor(Color.MAGENTA);
                Toast.makeText(LogData.this,"Error Saver",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ConnectControl(){
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TcpClient.mRun == false){
                    new ConnectTask().execute("");
                    if (connect_ok == true){
                        btnConnect.setText("CONNECTED");
                        btnConnect.setBackgroundColor(Color.GREEN);
                    }
                    else {
                        btnConnect.setText("CONNECT");
                        btnConnect.setBackgroundColor(Color.MAGENTA);
                        Toast.makeText(LogData.this,"Error Saver",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (GlobVar.mTcpClient != null) {
                        GlobVar.mTcpClient.stopClient();
                        btnConnect.setText("CONNECT");
                        btnConnect.setBackgroundColor(Color.MAGENTA);
                    }
                }
            }
        });
    }
    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            GlobVar.mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            connect_ok = GlobVar.mTcpClient.run();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....

        }
    }
}
