package taskstack.junjian.cn.taskstack.base;

import android.app.Application;
import android.telephony.TelephonyManager;

import com.avos.avoscloud.AVOSCloud;

import taskstack.junjian.cn.taskstack.Constant;

/**
 * Created by junjianliu
 * on 17/3/6
 * email:spyhanfeng@qq.com
 */

public class App extends Application {

    public static final int STATUS_RUNING = 1;     // 进行中

    public static final int STATUS_DEL = 2;        // 已删除

    public static final int STATUS_END = 3;        // 已归档

    public static String imei;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化 leanClound
        AVOSCloud.initialize(this, Constant.leanCloundAppId,Constant.leanCloundAppKay);
        AVOSCloud.setDebugLogEnabled(true);

        TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        imei = TelephonyMgr.getDeviceId();

    }

    public static String getImei() {
        return imei;
    }

    public static void setImei(String imei) {
        App.imei = imei;
    }
}
