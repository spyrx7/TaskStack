package taskstack.junjian.cn.taskstack.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import taskstack.junjian.cn.taskstack.R;
/**
 * Created by junjianliu
 * on 17/3/21
 * email:spyhanfeng@qq.com
 */

public class AboutActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ablout);
        initBar();
        inti();
    }

    private void inti(){

        textView = (TextView) findViewById(R.id.text);

        textView.setText("    任务栈是一个专注于决解自己近期的需要做的事情，非常方便的记录自己需要做的事情，为人们提供快速，高效的生活方式。");

    }

    private void initBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.ablout));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
