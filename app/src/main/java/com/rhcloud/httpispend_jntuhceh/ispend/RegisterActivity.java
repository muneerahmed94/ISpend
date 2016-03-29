package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextMobile, editTextPassword,editTextConfirmPassword;
    Button buttonRegister;
    String name, email, mobile, accountNumber, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextMobile = (EditText) findViewById(R.id.editTextMobile);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText)findViewById(R.id.editTextConfirmPassword);

        TextView textViewLoginHere = (TextView) findViewById(R.id.textViewLoginHere);
        textViewLoginHere.setPaintFlags(textViewLoginHere.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewLoginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editTextName.getText().toString();
                email = editTextEmail.getText().toString();
                mobile = editTextMobile.getText().toString();
                password = editTextPassword.getText().toString();
                confirmPassword = editTextConfirmPassword.getText().toString();

                if(!password.equals(confirmPassword)) {
                    showPasswordConfirmPasswordError();
                }
                else {
                    User user = new User(email, mobile, name, password);
                    registerUser(user);
                }
            }
        });


    }

    public void registerUser(User user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    public void showPasswordConfirmPasswordError() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
        dialogBuilder.setMessage("Password and Confirm Password should match");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}
