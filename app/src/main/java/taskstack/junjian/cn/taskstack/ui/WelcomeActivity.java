package taskstack.junjian.cn.taskstack.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

import taskstack.junjian.cn.taskstack.MainActivity;
import taskstack.junjian.cn.taskstack.R;

/**
 * Created by junjianliu
 * on 17/3/23
 * email:spyhanfeng@qq.com
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        handler.postDelayed(runnable,1000);
    }

    int time = 1;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(runnable,500);
            if(time == 0){
                toActivity();
            }
            time --;
        }
    };

    @SuppressLint("NewApi")
    private void toActivity(){
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
