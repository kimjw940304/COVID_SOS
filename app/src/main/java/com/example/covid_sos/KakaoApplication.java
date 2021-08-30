package com.example.covid_sos;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

/*카카오 API 초기화 */

public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSdk.init(this,"7be28d1255ec8798c7665bc682a40c34");
    }
}
