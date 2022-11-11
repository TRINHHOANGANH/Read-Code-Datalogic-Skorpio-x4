package com.example.rtcvision.Main.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rtcvision.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.datalogic.decode.BarcodeManager;
import com.datalogic.decode.DecodeException;
import com.datalogic.decode.DecodeResult;
import com.datalogic.decode.ReadListener;
import com.datalogic.device.ErrorManager;


public class Home extends AppCompatActivity implements View.OnClickListener {

    Button btnScan;
    TextView mtext,mtypes;
    BarcodeManager decoder = null;
    ReadListener listener = null;
    TextView Results_Code_1 = null;
    TextView Results_Code_2 = null;
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
                    startActivity(new Intent(getApplicationContext(),Settings.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
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
                        CompareString(Results_Code_1.getText().toString().toLowerCase(),Results_Code_2.getText().toString().toLowerCase(),number);
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
    private void CompareString(String value1, String value2, int number){
        String Value1After = value1.substring(0,number);
        String Value2After = value2.substring(0,number);
        if (Value1After.equals(Value2After)){
            btnScan.setText("OK");
            btnScan.setBackgroundColor(Color.GREEN);
        }
        else{
            btnScan.setText("NG");
            btnScan.setBackgroundColor(Color.RED);
        }
    }
}