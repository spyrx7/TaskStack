package taskstack.junjian.cn.taskstack.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.DeleteCallback;
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

    private int type = 0;

    private String _tag = "null";

    private TaskStackModel entity;

    private ImageView tag;

    
    public static Intent newIntent(Context c , TaskStackModel entity,int type){
        Intent intent = new Intent(c,AddTaskActivity.class);
        intent.putExtra("data",entity);
        intent.putExtra("type",type);
        return intent;
    }

    private void bindIntent(){
       if(getIntent() != null){
           this.entity = (TaskStackModel) getIntent().getSerializableExtra("data");
           this.type = getIntent().getIntExtra("type",0);
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
        if(id == R.id.menu_del){
            del();
        }
        if(id == R.id.menu_finish){
            Delete();
        }
        return super.onOptionsItemSelected(item);
    }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       switch (type){
           case 0:
               getMenuInflater().inflate(R.menu.menu_save,menu);
               break;
           case 1:
               getMenuInflater().inflate(R.menu.menu_edit_save,menu);
               break;
           case 2:
               getMenuInflater().inflate(R.menu.menu_del,menu);
               break;
           case 3:
               getMenuInflater().inflate(R.menu.menu_edit_save,menu);
               break;
       }

       return super.onCreateOptionsMenu(menu);
   }

   private void init(){
       dialog = new CatLoadingView();
       title = (EditText) findViewById(R.id.task_title);
       content = (EditText) findViewById(R.id.task_content);
       tag = (ImageView) findViewById(R.id.tag);

       if(entity != null){
           setTitle("编辑");

           title.setText(entity.getName());
           content.setText(entity.getContent());

           if(entity.getTagColor() != null && !entity.getTagColor().equals("null")){
               _tag = entity.getTagColor();
               tag.setBackgroundColor(Color.parseColor(_tag +""));
           }


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
       temp.setTagColor(_tag);

       Log.e("TAG >>>", "submit: " + _tag);

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
       avObject.put("tagColor",temp.getTagColor());


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

   public void onCheckTag(View v){
       ImageView img = (ImageView) v;
       _tag = img.getTag() + "";
       tag.setBackgroundColor(Color.parseColor(img.getTag() + ""));
   }

    /**
     *  永久删除
     */
    private void Delete(){

        AVObject object = AVObject.createWithoutData("TaskStackModel", entity.getId());

        object.deleteEventually(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                setResult(100);
                finish();
            }
        });
    }

    private void del(){

        AVObject object = AVObject.createWithoutData("TaskStackModel", entity.getId());

        object.put("status", App.STATUS_DEL);

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                setResult(100);
                finish();
            }
        });
    }

}
