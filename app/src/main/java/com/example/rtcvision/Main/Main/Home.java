package com.example.rtcvision.Main.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rtcvision.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.datalogic.device.ErrorManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Home extends AppCompatActivity implements View.OnClickListener {

    Button btnScan;
    TextView mtext,mtypes;
    BarcodeManager decoder = null;
    ReadListener listener = null;
    TextView Results_Code_1 = null;
    TextView Results_Code_2 = null;
    String swmode;
    String numberchar;
    GlobVar globVar;
    int number =2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnScan = findViewById(R.id.btn_scan);
        Results_Code_1=findViewById(R.id.txtResult_Code_1);
        Results_Code_2=findViewById(R.id.txtResult_Code_2);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Scan);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.Scan:
                    return true;
                case R.id.Code:
                    startActivity(new Intent(getApplicationContext(),Settings.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.Info:
                    startActivity(new Intent(getApplicationContext(),LogData.class));
                    return true;
            }
            return false;
        });
        globVar = new GlobVar();
        swmode = globVar.ReadFileFromES().split("\n")[0].split("=")[1];
        numberchar = globVar.ReadFileFromES().split("\n")[1].split("=")[1];
        globVar.FileOrTCP = Boolean.parseBoolean( globVar.ReadFileFromES().split("\n")[2].split("=")[1]);
        globVar.IP = globVar.ReadFileFromES().split("\n")[3].split("=")[1];
        globVar.Port = globVar.ReadFileFromES().split("\n")[4].split("=")[1];
        globVar.FormatFileSave = globVar.ReadFileFromES().split("\n")[5].split("=")[1];

        btnScan.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // If the decoder instance is null, create it.
        if (decoder == null) { // Remember an onPause call will set it to null.
            decoder = new BarcodeManager();
        }

        // From here on, we want to be notified with exceptions in case of errors.
        ErrorManager.enableExceptions(true);

        try {

            // Create an anonymous class.
            listener = new ReadListener() {

                // Implement the callback method.
                @Override
                public void onRead(DecodeResult decodeResult) {
                    // Change the displayed text to the current received result.
                    String x = Results_Code_1.getText().toString().toLowerCase();
                    if (Results_Code_1.getText().toString().toLowerCase() == "" && Results_Code_2.getText().toString().toLowerCase() == ""){
                        Results_Code_1.setText(decodeResult.getText());
                    }
                    else if(Results_Code_1.getText().toString().toLowerCase() != "" && Results_Code_2.getText().toString().toLowerCase() == ""){
                        Results_Code_2.setText(decodeResult.getText());
                        CompareString(Results_Code_1.getText().toString().toLowerCase(),Results_Code_2.getText().toString().toLowerCase(),numberchar);
                    }
                    else if(Results_Code_1.getText().toString().toLowerCase() != "" && Results_Code_2.getText().toString().toLowerCase() != ""){
                        Results_Code_1.setText(decodeResult.getText());
                        Results_Code_2.setText("");
                    }
                }

            };

            // Remember to add it, as a listener.
            decoder.addReadListener(listener);

        } catch (DecodeException e) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();


        // If we have an instance of BarcodeManager.
        if (decoder != null) {
            try {
                // Unregister our listener from it and free resources.
                decoder.removeReadListener(listener);

                // Let the garbage collector take care of our reference.
                decoder = null;
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
    private void CompareString(String value1, String value2, String numberchar){
        value1 = value1.replace("\n","");
        value2 = value2.replace("\n","");
        String Value1After;
        String Value2After;
        if(swmode.trim().equals("true"))
        {
            ShowCompare(value1,value2);
        }
        else
        {
            int number = Integer.parseInt(numberchar);
            if(value1.length() > number)
                 Value1After = value1.substring(0,number);
            else
                Value1After = value1;
            if(value2.length() > number)
                Value2After = value2.substring(0,number);
            else
                Value2After = value2;
            ShowCompare(Value1After,Value2After);
        }
        ExportDataCode(value1,value2,globVar.FileOrTCP,globVar.FormatFileSave);
    }
    private  void ExportDataCode(String value1, String value2, Boolean fileorTCP, String formatfile){
        if (!fileorTCP)
        {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
            String datetime = simpleDateFormat.format(calendar.getTime());
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datetime1 = simpleDateFormat1.format(calendar.getTime());
            globVar.WriteLogDataToFileES(datetime1 + ","+ value1 + "," + value2+","+ btnScan.getText()+"\n",datetime +formatfile);
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");
            String datetime = simpleDateFormat.format(calendar.getTime());
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datetime1 = simpleDateFormat1.format(calendar.getTime());
            if (GlobVar.mTcpClient != null) {
                GlobVar.mTcpClient.sendMessage(datetime1 + ","+ value1 + "," + value2+","+ btnScan.getText());
            }
            else{
                Toast.makeText(Home.this, "Error Send Data To Server !!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void ShowCompare(String value1, String value2){
        if (value1.equals(value2)){
            btnScan.setText("OK");
            btnScan.setBackgroundColor(Color.GREEN);
        }
        else{
            btnScan.setText("NG");
            btnScan.setBackgroundColor(Color.RED);
        }
    }
}