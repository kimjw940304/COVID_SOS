package com.example.covid_sos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class loginsuccess extends AppCompatActivity {

    /* Intent 넘겨받을 변수 선언 */
    private String strNick, strProfileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsuccess);

        /*Intent 넘겨 받는 코드*/
        Intent intent = getIntent();
        strNick = intent.getStringExtra("name");
        strProfileImg = intent.getStringExtra("profileImg");

        /* View 에 값을 연결 */
        TextView tv_nick = findViewById(R.id.tv_nickname);
        ImageView iv_profile = findViewById(R.id.iv_profile);

        /* view에 값 set */
        tv_nick.setText(strNick);
        Glide.with(this).load(strProfileImg).into(iv_profile);
    }
}