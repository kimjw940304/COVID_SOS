package com.example.covid_sos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;



public class MainActivity extends AppCompatActivity {


    /*카카오 로그인된 사용자 정보 */
    private static final String TAG = "MainActivity";

    /* 카카오 로그인 class 멤버 생성. */
    private View loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.login);



        /* 카카오톡 설치여부 확인 callback 함수*/
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>()  {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken != null){
                    //TBD
                }
                if (throwable != null){
                    //TBD
                    Log.w(TAG, "invoke: " + throwable.getLocalizedMessage());
                }
                MainActivity.this.updateKakaoLoginUi();
                return null;
            }
        };


        /*로그인 버튼 클릭 함수  */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*카카오톡이 설치되어있는지 먼저 확인*/
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)) {
                    /*카카오톡 설치시, 카카오톡으로 로그인*/
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, callback);
                    /*카카오톡 미설치시, 카카오웹으로 로그인.*/
                } else{
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this,callback);
                }
            }
        });

        /*화면이 생성되면, 사용자의 카카오톡 로그인 여부 확인 */
        updateKakaoLoginUi();
    }



    private void updateKakaoLoginUi() {
        /*사용자(me)의 API 로그인 정보를 가져온다.*/
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            /*로그인 여부를 확인하고 invoke 라는 callback 객체를 전달한다.*/
            public Unit invoke(User user, Throwable throwable) {
                /*이부분이 카카오 API 가이드 코드 참고 */
                if (user != null) {
                    /*로그인된 상태일 경우*/

                    /*loginsucces 로 넘겨줄 사용자 정보 intent*/
                    Intent intent = new Intent(MainActivity.this, loginsuccess.class);

                    /*intent객체에 onSuccess의 result를 넣어준다(putExtra).*/

                    /*     ★ App을 위해 필요한 로그인된 사용자의 정보를 여기에 써둬야함 !!★      */
                    intent.putExtra("name",user.getKakaoAccount().getProfile().getNickname());
                    intent.putExtra("profileImg",user.getKakaoAccount().getProfile().getThumbnailImageUrl());


                    /*다음 Activity에 intent를 넘겨준다.(loginsuccess 에서는 넘겨받을 코드를 작성해야함)*/
                    startActivity(intent);


                    /*얻을 수 있는 정보*/
                    Log.i(TAG, "invoke id="+user.getId());
                    Log.i(TAG, "invoke nickname="+user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "invoke gender="+user.getKakaoAccount().getGender());
                    Log.i(TAG, "invoke age="+user.getKakaoAccount().getAgeRange());



                } else {
                    /*로그인 X 경우*/
                    /*닉네임 등 로그인된 정보 안보임*/

                    Log.w(TAG, "Invoke"+throwable.getLocalizedMessage());

                }
                if (throwable != null) {
                    Log.w(TAG, "Invoke"+throwable.getLocalizedMessage());

                }
                return null;
            }
        });


    }


}