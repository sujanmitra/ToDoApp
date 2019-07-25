package com.project.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LoginActivity extends AppCompatActivity {

    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void onClickSignUp(View view){
        Intent intent = new Intent(this,SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void onClickLogin(View view){
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        if(username.getText().toString().equals("") || password.getText().toString().equals(""))
            Toast.makeText(this,"Fields cannot be empty",Toast.LENGTH_LONG).show();
        else{
            /*
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            */
            Realm realm=Realm.getDefaultInstance();
            User user=realm.where(User.class).equalTo("username",username.getText().toString()).findFirst();
            if(user==null){
                Toast.makeText(this,"User does not exist",Toast.LENGTH_LONG).show();
            }
            else{
                String userpass=user.getPassword();
                if(password.getText().toString().equals(userpass)) {
                    realm.close();
                    Toast.makeText(this,"Welcome " + username.getText().toString(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, ToDoTaskList.class);
                    intent.putExtra("username",username.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(this,"Wrong Credentials",Toast.LENGTH_LONG).show();
                    username.setText("");
                    password.setText("");
                }
            }
       }
    }
}

