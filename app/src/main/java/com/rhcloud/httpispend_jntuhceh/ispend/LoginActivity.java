package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    String email, password;
    UserLocalStore userLocalStore;
    TextView textViewEmail, textViewPassowrd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userLocalStore = new UserLocalStore(this);

        TextView textViewNewToISpend = (TextView) findViewById(R.id.textViewNewToISpend);
        textViewNewToISpend.setPaintFlags(textViewNewToISpend.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewNewToISpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewPassowrd = (TextView) findViewById(R.id.textViewPassword);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        Button button = (Button) findViewById(R.id.buttonLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                User user = new User(email, password);
                authenticate(user);
            }
        });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewEmail.setTextColor(Color.parseColor("#689f38"));
                    textViewEmail.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewEmail.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewEmail.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewPassowrd.setTextColor(Color.parseColor("#689f38"));
                    textViewPassowrd.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewPassowrd.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewPassowrd.setTypeface(null, Typeface.NORMAL);
                }
            }
        });
    }

    public void authenticate(User user)
    {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.fetchUserDataAsyncTask(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }

    public void showErrorMessage()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("The email and password you entered don't match.");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    public void logUserIn(User returnedUser)
    {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this, WelcomeActivity.class));
    }
}
