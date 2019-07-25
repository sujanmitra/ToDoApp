package com.project.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ToDoTaskList extends AppCompatActivity {

    RecyclerView mRecyclerView;
    private Boolean exit=false;
    String user;
    RealmResults<Tasks> userTasks;
    DrawerLayout dl;
    NavigationView nv;
    ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_task_list);

        Bundle bundle=getIntent().getExtras();
        user=bundle.getString("username");
        mRecyclerView=findViewById(R.id.task_recycler);
        /*RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();*/
        Realm realm=Realm.getDefaultInstance();
        userTasks=realm.where(Tasks.class).equalTo("username",user).findAll();
        User nm=realm.where(User.class).equalTo("username",user).findFirst();
        String name=nm.getName();
        if(userTasks.size()==0)
            fragment();
        else {
            MyAdapter myAdapter=new MyAdapter(userTasks,this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(myAdapter);
        }
        dl = findViewById(R.id.rootLayout);
        nv = findViewById(R.id.navigationView);
        View navView=nv.getHeaderView(0);
        TextView nav_nameText=navView.findViewById(R.id.textUserName);
        nav_nameText.setText("Logged in as "+name);
        toggle = new ActionBarDrawerToggle(this, dl, R.string.open_menu, R.string.close_menu);
        dl.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.refresh:
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.logout:
                        finish();
                        Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.exit:
                        finish();
                        return true;
                }
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    public void fragment(){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.add(R.id.empty_container,new NoTaskFragment());
        ft.commit();
    }
    public void onClickCreateTask(View view){
        Intent intent = new Intent(this,CreateToDoTask.class);
        intent.putExtra("username",user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
    public void onClickMarkAllDone(View view){
        Tasks task;
        for(int i=0;i<userTasks.size();i++){
            task=userTasks.get(i);
            Realm realm=Realm.getDefaultInstance();
            realm.beginTransaction();
            try {
                task.setCheck_task(true);
                realm.commitTransaction();
            }
            catch (Exception ex)
            {
                realm.cancelTransaction();
            }
            realm.close();
        }
        MyAdapter myAdapter=new MyAdapter(userTasks,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(myAdapter);
    }

}
