package com.example.jacky_pc.badgeview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    BadgeView bv_number;
    EditText et_input;

    private int num = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bv_number = findViewById(R.id.bv_number);
        et_input = findViewById(R.id.et_input);


//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        bv_number.setNumber(num = num+10);
//                    }
//                });
//            }
//        },1000,2000);
    }
    public void onChange(View view) {


        bv_number.setNumber(Integer.valueOf(et_input.getText().toString().trim()));
    }
}
