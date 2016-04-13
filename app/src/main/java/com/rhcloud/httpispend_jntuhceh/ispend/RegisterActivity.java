package com.rhcloud.httpispend_jntuhceh.ispend;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextMobile, editTextPassword,editTextConfirmPassword;
    Button buttonRegister;
    String name, email, mobile, accountNumber, password, confirmPassword;
    TextView textViewName, textViewEmail, textViewMobile,textViewPassword, textViewConfirmPassword;

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

                if(validate()) {
                    User user = new User(email, mobile, name, password);
                    registerUser(user);
                }
            }
        });
        
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewMobile = (TextView) findViewById(R.id.textViewMobile);
        textViewPassword = (TextView) findViewById(R.id.textViewPassword);
        textViewConfirmPassword = (TextView) findViewById(R.id.textViewConfirmPassword);

        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewName.setTextColor(Color.parseColor("#689f38"));
                    textViewName.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewName.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewName.setTypeface(null, Typeface.NORMAL);
                }
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

        editTextMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewMobile.setTextColor(Color.parseColor("#689f38"));
                    textViewMobile.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewMobile.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewMobile.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewPassword.setTextColor(Color.parseColor("#689f38"));
                    textViewPassword.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewPassword.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewPassword.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        editTextConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewConfirmPassword.setTextColor(Color.parseColor("#689f38"));
                    textViewConfirmPassword.setTypeface(null, Typeface.BOLD);
                } else {
                    textViewConfirmPassword.setTextColor(Color.parseColor("#6d6d6d"));
                    textViewConfirmPassword.setTypeface(null, Typeface.NORMAL);
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

    public boolean validate() {
        String name = editTextName.getText().toString();
        if(!isValidName(name)) {
            editTextName.setError("Enter a valid name");
            editTextName.requestFocus();
            return false;
        }

        String email = editTextEmail.getText().toString();
        if(!isValidEmail(email)) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return false;
        }

        String mobile = editTextMobile.getText().toString();
        if(!isValidMobile(mobile)) {
            editTextMobile.setError("Enter a 10 digit mobile number");
            editTextMobile.requestFocus();
            return false;
        }

        String password = editTextPassword.getText().toString();
        if(!isValidPassword(password)) {
            editTextPassword.setError("Password should have at least 6 characters");
            editTextPassword.requestFocus();
            return false;
        }

        String confirmPassword = editTextConfirmPassword.getText().toString();
        if(!isValidConfirmPassword(password, confirmPassword)) {
            editTextConfirmPassword.setError("Password and Confirm Password don't match");
            editTextConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidName(String name) {
        if (name != null && name.length() > 0) {
            return true;
        }
        return false;
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidMobile(String mobile) {
        if (mobile != null && mobile.length() == 10) {
            return true;
        }
        return false;
    }

    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }

    private boolean isValidConfirmPassword(String password, String confirmPassword) {
        if (password != null && password.equals(confirmPassword)) {
            return true;
        }
        return false;
    }
}
