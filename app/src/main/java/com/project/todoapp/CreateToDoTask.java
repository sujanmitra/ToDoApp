package com.project.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;

public class CreateToDoTask extends AppCompatActivity implements ColorPickerDialog.OnColorChangedListener{

    String user;
    EditText task_date, task_name, task_details, task_color;
    TextView textBox;
    private Context mContext;
    private Paint mPaint;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_to_do_task);
        mContext = this;
        task_date = findViewById(R.id.et_date);
        task_name = findViewById(R.id.task_name);
        task_details = findViewById(R.id.task_details);
        task_color=findViewById(R.id.holder_color);
        textBox = findViewById(R.id.textBox);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getString("username");
        textBox.setText("Created for User - " + user);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        task_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateToDoTask.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mPaint=new Paint();
    }

    public void onColorClick(View view){
        Toast.makeText(getApplicationContext(),"Tap the middle to select",Toast.LENGTH_LONG).show();
        new ColorPickerDialog(this,this, mPaint.getColor()).show();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        task_date.setText(sdf.format(myCalendar.getTime()));
    }

    public void onClickSaveTask(View view) {
        if(task_name.getText().toString().equals("") || task_date.getText().toString().equals("") || task_details.getText().toString().equals(""))
            Toast.makeText(this,"Fields cannot be empty",Toast.LENGTH_LONG).show();
        else {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            try {
                Tasks task = realm.createObject(Tasks.class, System.currentTimeMillis() / 1000);
                task.setUsername(user);
                task.setTask_name(task_name.getText().toString());
                task.setDate(task_date.getText().toString());
                task.setTask_details(task_details.getText().toString());
                task.setTask_color(task_color.getText().toString());
                realm.commitTransaction();
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                task_date.setText("");
                task_name.setText("");
                task_details.setText("");
                task_color.setText("");
            }
            catch (Exception ex) {
                realm.cancelTransaction();
                Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
            }
            realm.close();
            Toast.makeText(getApplicationContext(),"Refresh to load Items",Toast.LENGTH_LONG).show();
            finish();
        }
    }
    public void onClickDiscardTask(View view){
        finish();
    }

    @Override
    public void colorChanged(int color) {
        task_color.setText(Integer.toString(color));
    }
}
