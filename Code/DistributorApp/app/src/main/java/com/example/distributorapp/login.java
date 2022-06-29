package com.example.distributorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    EditText username, password;
    Button login, cancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        login = findViewById(R.id.loginBTN);
        cancel = findViewById(R.id.cancelBTN);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void login()
    {
        String uname = username.getText().toString();
        String pswd = password.getText().toString();

        username.setText("asd");
        password.setText(("12345"));

        if(uname.equals("") || pswd.equals(""))
        {
            Toast.makeText(this, "username or password is blank",Toast.LENGTH_LONG).show();
        }
        else if(uname.equals("asd") && pswd.equals("12345"))
        {
            Toast.makeText(this, "Login success",Toast.LENGTH_LONG).show();
            Intent intent= new Intent(login.this, home.class);
            startActivity(intent);
            username.setText("");
            password.setText((""));
        }
        else
        {
            Toast.makeText(this, "username or password not match",Toast.LENGTH_LONG).show();
        }
    }
}
