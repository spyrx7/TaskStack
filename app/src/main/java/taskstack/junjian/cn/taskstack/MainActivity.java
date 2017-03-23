package taskstack.junjian.cn.taskstack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.roger.catloadinglibrary.CatLoadingView;

import java.util.ArrayList;
import java.util.List;

import taskstack.junjian.cn.taskstack.adapter.ListAdapter;
import taskstack.junjian.cn.taskstack.base.App;
import taskstack.junjian.cn.taskstack.bean.TaskStackModel;
import taskstack.junjian.cn.taskstack.ui.AboutActivity;
import taskstack.junjian.cn.taskstack.ui.AddTaskActivity;
import taskstack.junjian.cn.taskstack.utils.DataToObjectUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, AppBarLayout.OnOffsetChangedListener {

    private RecyclerView listView;

    private DrawerLayout drawer;

    private FloatingActionButton fab;

    private NavigationView navigationView;

    private Toolbar toolbar;

    private ListAdapter adapter;

    private CatLoadingView dialog;

    private SwipeRefreshLayout refresh;

    private FloatingActionButton fbt;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private AppBarLayout applayout;

    private boolean viewstatus = false;

    private  List<TaskStackModel> data = new ArrayList<>();

    private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTools();
        init();
    }

    private void initTools(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.task));
        setSupportActionBar(toolbar);
    }

    private void init(){

        dialog = new CatLoadingView();

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        applayout = (AppBarLayout) findViewById(R.id.applayout);
        fbt = (FloatingActionButton) findViewById(R.id.fab);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        listView = (RecyclerView) findViewById(R.id.list);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_task);
        status = App.STATUS_RUNING;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddTaskActivity.class),100);
            }
        });

        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.cardview_shadow_end_color));
        refresh.setOnRefreshListener(this);

        applayout.addOnOffsetChangedListener(this);

        listView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy < 0){
                    fbt.show();
                }else{
                    fbt.hide();
                }
            }
        });

        initData();
    }

    private void initData(){

        dialog.show(getSupportFragmentManager(),"");

        AVQuery<AVObject> avObject = new AVQuery("TaskStackModel");// 构建对象
        avObject.whereEqualTo("status",status);
        avObject.whereEqualTo("userid", App.imei);
        avObject.orderByDescending("createdAt");
        avObject.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                dialog.onDismiss(dialog.getDialog());

                if(list == null) return;

                List<TaskStackModel> data = DataToObjectUtils.getDate(list);

                adapter = new ListAdapter(MainActivity.this,data,viewstatus);
                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_view) {
            if(item.getTitle().equals("切换视图")){
                item.setTitle("切换列表");
                viewstatus = true;
                listView.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
            }else{
                item.setTitle("切换视图");
                viewstatus = false;
                listView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
            reLoad();
            changeView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_task:
                collapsingToolbarLayout.setTitle(getResources().getString(R.string.task));
                status = App.STATUS_RUNING;
                break;
            case R.id.nav_complete:
                collapsingToolbarLayout.setTitle(getResources().getString(R.string.complete));
                status = App.STATUS_END;
                break;
            case R.id.nav_del_list:
                collapsingToolbarLayout.setTitle(getResources().getString(R.string.deltask));
                status = App.STATUS_DEL;
                break;
            case R.id.nav_about:
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
                break;
        }

        reLoad();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onRefresh() {
        if(refresh.isRefreshing()){
            refresh.setRefreshing(false);
            reLoad();
        }
    }

    private void reLoad(){
        refresh.setRefreshing(true);

        AVQuery<AVObject> avObject = new AVQuery("TaskStackModel");// 构建对象
        avObject.whereEqualTo("status",status);
        avObject.whereEqualTo("userid", App.imei);
        avObject.orderByDescending("createdAt");
        avObject.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                refresh.setRefreshing(false);

                if(list == null) return;

                data = DataToObjectUtils.getDate(list);
                adapter.clear();

                if(adapter == null) {
                    adapter = new ListAdapter(MainActivity.this, data,viewstatus);
                    listView.setAdapter(adapter);
                }else{
                    adapter.setDate(data);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        if(refresh.isRefreshing()){
            refresh.setRefreshing(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            refresh.setRefreshing(true);
            reLoad();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int scrollRangle = appBarLayout.getTotalScrollRange();  // 获取本身的高度
        int scroll = scrollRangle - Math.abs(verticalOffset) ;  //

        if(scroll == 0){
            fbt.hide();
        }else{
            fbt.show();
        }
    }

    private void changeView(){
        adapter.clear();

        adapter = new ListAdapter(MainActivity.this, data,viewstatus);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}
