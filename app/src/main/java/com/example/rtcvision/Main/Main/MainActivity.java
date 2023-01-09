package com.example.rtcvision.Main.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rtcvision.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button _btnStart;
    ImageView _imageView;
    TextView _txtSlogan;
    String[] ListImage= {"logo"};
    int _indexImage,_indexSlogan;
    String _NameImage,_NameSlogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _btnStart = findViewById(R.id.btnStart);
        _imageView = findViewById(R.id.ImageViewMain);
        _btnStart.setOnClickListener(this);
        // nextInt: Random 1 số bất kì trong khoảng
        _indexImage = new Random().nextInt(ListImage.length);
        _NameImage = ListImage[_indexImage];
        // get Id của image trong drawable
        int id= getResources().getIdentifier(_NameImage,"drawable",MainActivity.this.getPackageName());
        _imageView.setImageResource(id);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.btnStart)
        {
            Intent intent = new Intent(MainActivity.this,Home.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        if (GlobVar.mTcpClient != null) {
            GlobVar.mTcpClient.stopClient();
        }
        super.onDestroy();
    }
}