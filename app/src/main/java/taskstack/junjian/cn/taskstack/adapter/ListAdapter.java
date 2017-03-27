package taskstack.junjian.cn.taskstack.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.List;

import taskstack.junjian.cn.taskstack.R;
import taskstack.junjian.cn.taskstack.base.App;
import taskstack.junjian.cn.taskstack.bean.TaskStackModel;
import taskstack.junjian.cn.taskstack.ui.AddTaskActivity;

/**
 *
 * Created by junjianliu
 * on 17/3/11
 * email:spyhanfeng@qq.com
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Context context;

    private List<TaskStackModel> data;

    private int index = 0;

    private boolean viewstatus = false;

    private int status = 0;

    public ListAdapter(Context context,List<TaskStackModel> data,boolean viewstatus,int status) {
        this.context = context;
        this.data = data;
        this.viewstatus = viewstatus;
        this.status = status;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public void setViewStatus(boolean viewstatus){
        this.viewstatus = viewstatus;
    }

    public void clear(){
        this.data.clear();
    }

    public void setDate(List<TaskStackModel> data){
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        if(!viewstatus){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0){
            return new ListViewHolder(View.inflate(context, R.layout.view_list,null));
        }else{
            return new ListViewHolder(View.inflate(context, R.layout.view_grid,null));
        }
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {
        TaskStackModel temp = data.get(position);
        holder.textView.setText(temp.getName());

        if(temp.getTagColor() != null && !temp.getTagColor().equals("null") && !temp.getTagColor().isEmpty()) {
            holder.tag.setBackgroundColor(Color.parseColor(temp.getTagColor()));
        }else{
            holder.tag.setBackgroundColor(Color.WHITE);
        }
        switch (status){
            case App.STATUS_RUNING:
                holder.imgMore.setImageResource(R.drawable.ic_more);
                break;
            case App.STATUS_END:
                holder.imgMore.setImageResource(R.drawable.ic_classify);
                break;
            case App.STATUS_DEL:
                holder.imgMore.setImageResource(R.drawable.ic_del);
                break;
        }

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = position;
                switch (status){
                    case App.STATUS_RUNING:
                        showMore();
                        break;
                    case App.STATUS_END:
                        showDel();
                        break;
                    case App.STATUS_DEL:
                        showDelete();
                        break;
                }
            }
        });


        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = position;
                actionActivity();
            }
        });

        if(holder.textTime != null){
            holder.textTime.setText(temp.getStartdatetime());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imgMore;
        CardView cardview;

        TextView textTime;
        ImageView tag;

        public ListViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.text);
            imgMore = (ImageView) view.findViewById(R.id.more);
            cardview = (CardView) view.findViewById(R.id.cardview);

            tag = (ImageView) view.findViewById(R.id.tag);

            if(viewstatus){
                textTime = (TextView) view.findViewById(R.id.texttime);
            }



        }
    }

    private void showMore(){

        String[] array = new String[]{"完成","删除"};
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    update();
                }else{
                    del();
                }
            }
        });

        alert.create().show();
    }

    private void update(){
        TaskStackModel temp = data.get(index);

        AVObject object = AVObject.createWithoutData("TaskStackModel", temp.getId());
        object.put("status", App.STATUS_END);

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                data.remove(index);
                notifyDataSetChanged();
            }
        });

    }

    private void del(){
        TaskStackModel temp = data.get(index);

        AVObject object = AVObject.createWithoutData("TaskStackModel", temp.getId());

        object.put("status", App.STATUS_DEL);

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                data.remove(index);
                notifyDataSetChanged();
            }
        });

        /*object.deleteEventually(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                data.remove(index);
                notifyDataSetChanged();
            }
        });*/

    }

    /**
     *  完成状态下 删除到 删除列表
     */
    private void showDel(){
        del();
    }

    /**
     * 删除列表 永久删除
     */
    private void showDelete(){

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("是否删除？");
        alert.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Delete();
            }
        });

        alert.setPositiveButton("取消", null);

        alert.create().show();
    }

    /**
     *  永久删除
     */
    private void Delete(){

        TaskStackModel temp = data.get(index);

        AVObject object = AVObject.createWithoutData("TaskStackModel", temp.getId());

        object.deleteEventually(new DeleteCallback() {
                @Override
                public void done(AVException e) {
                    data.remove(index);
                    notifyDataSetChanged();
                }
        });
    }

    private void actionActivity(){
        TaskStackModel temp = data.get(index);
        context.startActivity(AddTaskActivity.newIntent(context,temp,status));
    }

}
