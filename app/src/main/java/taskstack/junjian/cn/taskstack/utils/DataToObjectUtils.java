package taskstack.junjian.cn.taskstack.utils;

import android.util.Log;

import com.avos.avoscloud.AVObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import taskstack.junjian.cn.taskstack.base.App;
import taskstack.junjian.cn.taskstack.bean.TaskStackModel;

/**
 * Created by junjianliu
 * on 17/3/16
 * email:spyhanfeng@qq.com
 */

public class DataToObjectUtils {

    public DataToObjectUtils() {

    }

    public static List<TaskStackModel> getDate(List<AVObject> list){
        List<TaskStackModel> data = new ArrayList<>();
        int count = list.size();
        for(int i = 0; i < count; i++){
            AVObject obj = list.get(i);

            TaskStackModel temp = new TaskStackModel();
            temp.setId(obj.getObjectId());
            temp.setName(obj.getString("name"));
            temp.setContent(obj.getString("content"));
            temp.setStatus(obj.getInt("status"));
            temp.setStack(obj.getBoolean("isStack"));
            temp.setUserid(obj.getString("userid"));
            temp.setStartdatetime(getTodayDateTime(obj.getCreatedAt()));
            temp.setEnddatetime(getTodayDateTime(obj.getUpdatedAt()));
            data.add(temp);

        }

        return data;
    }

    public static String getTodayDateTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm",
                Locale.getDefault());
        return format.format(date);
    }

}
