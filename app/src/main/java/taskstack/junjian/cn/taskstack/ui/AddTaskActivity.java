package taskstack.junjian.cn.taskstack.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.roger.catloadinglibrary.CatLoadingView;

import java.io.Serializable;

import taskstack.junjian.cn.taskstack.Constant;
import taskstack.junjian.cn.taskstack.R;
import taskstack.junjian.cn.taskstack.base.App;
import taskstack.junjian.cn.taskstack.bean.TaskStackModel;

/**
 * Created by junjianliu
 * on 17/3/14
 * email:spyhanfeng@qq.com
 */
public class AddTaskActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText title;

    private EditText content;

    private CatLoadingView dialog;

    private int STATUS_RUNING = 1;     // 进行中

    private int STATUS_DEL = 2;        // 已删除

    private int STATUS_END = 3;        // 已归档

    private TaskStackModel entity;

    
    public static Intent newIntent(Context c , TaskStackModel entity){
        Intent intent = new Intent(c,AddTaskActivity.class);
        intent.putExtra("data",entity);
        return intent;
    }

    private void bindIntent(){
       if(getIntent() != null){
         this.entity = (TaskStackModel) getIntent().getSerializableExtra("data");
       }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindIntent();

        setContentView(R.layout.activity_addtask);

        initBar();

        init();
    }

    private void initBar(){
        setTitle("添加任务");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            this.finish();
        }
        if(id == R.id.menu_save){
            submit();
        }

        return super.onOptionsItemSelected(item);
    }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_save,menu);
       return super.onCreateOptionsMenu(menu);
   }

   private void init(){
       dialog = new CatLoadingView();
       title = (EditText) findViewById(R.id.task_title);
       content = (EditText) findViewById(R.id.task_content);

       if(entity != null){
           setTitle("编辑");

           title.setText(entity.getName());
           content.setText(entity.getContent());
       }
   }

   private void submit(){


       if(title.getText().toString().isEmpty()){
           title.setError("请输入任务标题～");
           return;
       }

       dialog.show(getSupportFragmentManager(),"");

       TaskStackModel temp = new TaskStackModel();
       temp.setName(title.getText().toString());
       temp.setContent(content.getText().toString());
       temp.setStack(false);
       temp.setStatus(App.STATUS_RUNING);
       temp.setUserid(App.imei);

       AVObject avObject;// 构建对象

       if(entity == null){
           avObject = new AVObject("TaskStackModel");// 构建对象
       }else{
           avObject = AVObject.createWithoutData("TaskStackModel", entity.getId());
       }

       avObject.put("name",temp.getName());
       avObject.put("content",temp.getContent());
       avObject.put("isStack",temp.isStack());
       avObject.put("status",temp.getStatus());
       avObject.put("startdatetime",temp.getStartdatetime());
       avObject.put("enddatetime",temp.getEnddatetime());
       avObject.put("userid",temp.getUserid());

       avObject.saveInBackground(new SaveCallback() {
           @Override
           public void done(AVException e) {
               dialog.onDismiss(dialog.getDialog());
               if(e == null){
                    Toast.makeText(AddTaskActivity.this,R.string.save_success,Toast.LENGTH_LONG).show();
                    setResult(100);
                    finish();
               }else{

               }
           }
       });
   }
}
