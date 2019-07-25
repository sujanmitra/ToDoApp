package com.project.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class    SignUpActivity extends AppCompatActivity {

    EditText name,username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onButtonPressed(View view) {
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        if (username.getText().toString().equals("") || name.getText().toString().equals("") || password.getText().toString().equals(""))
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
        else {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<User> result = realm.where(User.class).equalTo("username", username.getText().toString()).findAll();
            if (result.size() == 0) {
                realm.beginTransaction();
                try {
                    User user = realm.createObject(User.class, username.getText().toString());
                    user.setName(name.getText().toString());
                    user.setPassword(password.getText().toString());
                    realm.commitTransaction();
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                    name.setText("");
                } catch (Exception ex) {
                    realm.cancelTransaction();
                    Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show();
                }
                realm.close();
            } else
                Toast.makeText(this, "Username is not available", Toast.LENGTH_LONG).show();
        }
        username.setText("");
        password.setText("");
    }
    public void onPressLoginPage(View view){
        Intent intent=new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
