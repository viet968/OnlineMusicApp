package com.example.onlinemusicapp.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinemusicapp.Presenter.RegisterPresenter;
import com.example.onlinemusicapp.R;
import com.example.onlinemusicapp.View.RegisterView;

public class RegisterAccountActivity extends AppCompatActivity implements RegisterView {
    private EditText edtRegisterUsername,edtRegisterPassword,edtConfirmRegisterPassword;
    private Button btRegisterAccount;
    private TextView txtHaveAnAccount;
    private RegisterPresenter registerPresenter;
    Animation scaleUp,scaleDown;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_OnlineMusicApp);

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 400) {
            setContentView(R.layout.activity_register_account_4xx);
        } else {
            setContentView(R.layout.activity_register_account);
        }
        andXa();

        // txtHaveAnAccount event
        txtHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btRegisterAccount.setOnClickListener(v ->{
            String getUsername = edtRegisterUsername.getText().toString().trim();
            String getPassword = edtRegisterPassword.getText().toString().trim();
            String getConfirmPassword = edtConfirmRegisterPassword.getText().toString().trim();
            btRegisterAccount.startAnimation(scaleUp);
            register(getUsername,getPassword,getConfirmPassword);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void andXa(){
        edtRegisterUsername = (EditText) findViewById(R.id.edtRegisterUsername);
        edtRegisterPassword = (EditText) findViewById(R.id.edtRegisterPassword);
        edtConfirmRegisterPassword = (EditText) findViewById(R.id.edtRegisterConfirmPassword);
        txtHaveAnAccount = (TextView) findViewById(R.id.txtHaveAnAccount);
        btRegisterAccount = (Button) findViewById(R.id.btRegisterAccount);
        registerPresenter = new RegisterPresenter(this);
        scaleUp = AnimationUtils.loadAnimation(this,R.anim.scale_button_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.scale_button_down);
    }

    @Override
    public void register(String username, String password, String confirmPassword) {
        boolean registerResult = registerPresenter.register(username, password, confirmPassword);
        if(registerResult){
            registerSuccess(username, password);
        }
    }

    @Override
    public void registerErrorUsername() {
        edtRegisterUsername.setError("Username can not be empty");
    }

    @Override
    public void registerErrorPassword() {
        edtRegisterPassword.setError("Password can not be empty");
    }

    @Override
    public void registerErrorConfirmPassword() {
        edtConfirmRegisterPassword.setError("Confirm password can not be empty");
    }

    @Override
    public void registerErrorPasswordAndConfirmPassword() {
        edtRegisterPassword.setError("");
        edtConfirmRegisterPassword.setError("Password and confirm password not same");
    }


    @Override
    public void registerFailed() {
        Toast.makeText(this, "Register ERROR", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerSuccess(String username,String password) {
        Toast.makeText(getApplicationContext(), "Register successfully", Toast.LENGTH_SHORT).show();
        Intent sendRegisteredAccount = new Intent();
        sendRegisteredAccount.putExtra("usernameRegistered",username);
        sendRegisteredAccount.putExtra("passwordRegistered",password);
        setResult(Activity.RESULT_OK,sendRegisteredAccount);
        finish();
    }
}