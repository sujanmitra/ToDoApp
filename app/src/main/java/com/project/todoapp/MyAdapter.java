package com.project.todoapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private RealmResults<Tasks> userTasks;
    private Context mcontext;

    public MyAdapter(RealmResults<Tasks> tasks,Context context){
        userTasks=tasks;
        mcontext=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position){
        Tasks task=userTasks.get(position);
        holder.task_name.setText(task.getTask_name());
        holder.due_date.setText("Due by " + task.getDate());
        holder.checkbox.setChecked(task.isCheck_task());
        holder.relativeLayout.setBackgroundColor(Integer.parseInt(!task.getTask_color().equals("")?task.getTask_color():"-1"));
    }
    @Override
    public int getItemCount(){return userTasks.size();}
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView task_details,due_date,task_name,task_color;
        private CheckBox checkbox;
        private RelativeLayout relativeLayout;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            task_name=itemView.findViewById(R.id.task_name);
            due_date=itemView.findViewById(R.id.due_date);
            checkbox=itemView.findViewById(R.id.check_box);
            relativeLayout=itemView.findViewById(R.id.category_image);

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    Realm realm=Realm.getDefaultInstance();
                    Tasks current_task=realm.where(Tasks.class).equalTo("task_name",task_name.getText().toString()).findFirst();
                    realm.beginTransaction();
                    try{
                        current_task.setCheck_task(compoundButton.isChecked());
                        realm.commitTransaction();
                    }
                    catch (Exception ex){
                        realm.cancelTransaction();
                    }
                    realm.close();
                }
            });
        }
    }
}
