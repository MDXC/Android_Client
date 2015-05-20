package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.manager.AirlineManager;
import com.test.juxiaohui.mdxc.manager.CityManager;
import com.test.juxiaohui.mdxc.manager.UserManager;

/**
 * Created by yihao on 15/5/20.
 */
public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                UserManager.getInstance().checkLogin(SplashActivity.this, null, true);
                CityManager.getInstance();
                AirlineManager.getInstance();
                EntryActivity.startActivity(SplashActivity.this);
            }
        });
        t.start();

    }
}