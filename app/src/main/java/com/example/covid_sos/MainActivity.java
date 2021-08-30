package com.example.covid_sos;

import androidx.appcompat.app.AppCompatActivity;

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
    private TextView nickname;
    private ImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.login);
        nickname = findViewById(R.id.nickname);
        profileImage = findViewById(R.id.profile);

        /* 카카오톡 설치여부 확인 callback 객체*/
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


        /*로그인 버튼 눌렀을 때 실행되도록  */
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
                    /*얻을 수 있는 정보*/
                    Log.i(TAG, "invoke id="+user.getId());
                    Log.i(TAG, "invoke nickname="+user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "invoke gender="+user.getKakaoAccount().getGender());
                    Log.i(TAG, "invoke age="+user.getKakaoAccount().getAgeRange());

                    /*버튼에 정보 set*/

                    nickname.setText(user.getKakaoAccount().getProfile().getNickname());
                    /*카카오 프로필 이미지를 url로 가져와서 화면에 보여주는 Glide 오픈소스 추가가 우선임*/
                    Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImage);
                    /* 로그인 버튼 안보임*/
                    loginButton.setVisibility(View.GONE);

                } else {
                    /*로그인 X 경우*/
                    /*닉네임 등 로그인된 정보 안보임*/
                    nickname.setText(null);
                    profileImage.setImageBitmap(null);
                    Log.w(TAG, "Invoke"+throwable.getLocalizedMessage());
                    /*로그인 버튼 보임*/
                    loginButton.setVisibility(View.VISIBLE);
                }
                if (throwable != null) {
                    Log.w(TAG, "Invoke"+throwable.getLocalizedMessage());

                }
                return null;
            }
        });


    }


}