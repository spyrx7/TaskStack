package taskstack.junjian.cn.taskstack.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
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

    public ListAdapter(Context context,List<TaskStackModel> data,boolean viewstatus) {
        this.context = context;
        this.data = data;
        this.viewstatus = viewstatus;
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

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = position;
                showMore();
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

        public ListViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.text);
            imgMore = (ImageView) view.findViewById(R.id.more);
            cardview = (CardView) view.findViewById(R.id.cardview);

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

    private void actionActivity(){
        TaskStackModel temp = data.get(index);
        context.startActivity(AddTaskActivity.newIntent(context,temp));
    }

}
