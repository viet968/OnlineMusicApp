package com.example.onlinemusicapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.example.onlinemusicapp.Presenter.LoginPresenter;
import com.example.onlinemusicapp.R;
import com.example.onlinemusicapp.View.LoginView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private EditText edtLoginUsername,edtLoginPassword;
    private Button btLogin;
    private TextView txtCreateAccount,txtForgotPassword;
    private LoginPresenter loginPresenter;
    private MaterialCheckBox cbRemenberLogin;
    private SharedPreferences getRememberLogin;


    final private ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent getAccountRegisteredFromRegisterActivity = result.getData();
                        edtLoginUsername.setText(getAccountRegisteredFromRegisterActivity.getStringExtra("usernameRegistered"));
                        edtLoginPassword.setText(getAccountRegisteredFromRegisterActivity.getStringExtra("passwordRegistered"));
                    }
                }
            });
    private ActivityResultLauncher<String[]> mPermissionResultLaucher;
    private boolean isReadStorage = false,isWriteStorage = false,isCamera = false;
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_OnlineMusicApp);
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 400) {
            setContentView(R.layout.activity_login_4xx);
        } else {
            setContentView(R.layout.activity_login);
        }
        anhXa();

        mPermissionResultLaucher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                new ActivityResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onActivityResult(Map<String, Boolean> result) {
                        if(result.get(Manifest.permission.READ_EXTERNAL_STORAGE)!=null){
                            isReadStorage = result.get(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                        if(result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=null){
                            isReadStorage = result.get(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                        if(result.get(Manifest.permission.CAMERA)!=null){
                            isReadStorage = result.get(Manifest.permission.CAMERA);
                        }
                    }
                });

        requestPermission();

        // txtCreateAccount event
        txtCreateAccount.setOnClickListener(view ->{
            intentActivityResultLauncher.launch(new Intent(LoginActivity.this, RegisterAccountActivity.class));
        });

        btLogin.setOnClickListener(view -> {
            SharedPreferences.Editor editor = this.getSharedPreferences("REMEMBER_LOGIN",MODE_PRIVATE).edit();
            String getLoginUsername = edtLoginUsername.getText().toString().trim();
            String getLoginPassword = edtLoginPassword.getText().toString().trim();
            login(getLoginUsername,getLoginPassword);
            
            if(cbRemenberLogin.isChecked()){
                editor.putString("rememberUsername",edtLoginUsername.getText().toString().trim());
                editor.putString("rememberPassword",edtLoginPassword.getText().toString().trim());
                editor.apply();
            }else {
                editor.remove("rememberUsername");
                editor.remove("rememberPassword");
                editor.apply();
            }
        });

    }
    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
       String rememUsername =  getRememberLogin.getString("rememberUsername",null);
       String rememPass = getRememberLogin.getString("rememberPassword",null);
       if(rememPass!=null && rememUsername!=null){
           cbRemenberLogin.setChecked(true);
           edtLoginUsername.setText(rememUsername);
           edtLoginPassword.setText(rememPass);
       }else{
           cbRemenberLogin.setChecked(false);
           edtLoginUsername.setText("");
           edtLoginPassword.setText("");
       }
    }

    private void anhXa(){
        edtLoginUsername = (EditText) findViewById(R.id.edtLoginUsername);
        edtLoginPassword = (EditText) findViewById(R.id.edtLoginPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        txtCreateAccount = (TextView) findViewById(R.id.txtCreateAccount);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        loginPresenter = new LoginPresenter(this);
        cbRemenberLogin = findViewById(R.id.cbSaveLogin);
        getRememberLogin = this.getSharedPreferences("REMEMBER_LOGIN",MODE_PRIVATE);
    }

    @Override
    public void login(String username, String password) {
        if(loginPresenter.login(username,password,this)){
            loginSuccess(username);
        }
//        else{
//            loginFailed();
//        }
    }

    @Override
    public void loginFailed() {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void loginSuccess(String username) {
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
        Intent moveHome = new Intent(LoginActivity.this, HomeActivity.class);
        moveHome.putExtra("usernameLogged",username);
        startActivity(moveHome);
        finish();
    }

    @Override
    public void loginUsernameError() {
        edtLoginUsername.setError("Username can't be empty");
    }

    @Override
    public void loginPasswordError() {
        edtLoginPassword.setError("Password can't be empty");
    }

    @Override
    public void passwordWrong(){
        edtLoginPassword.setError("Password WRONG");
    }

    private void requestPermission(){
        isReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        isWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

        isCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

        List<String> permissionRequest = new ArrayList<>();
        if(!isReadStorage){
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(!isWriteStorage){
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!isCamera){
            permissionRequest.add(Manifest.permission.CAMERA);
        }

        if(!permissionRequest.isEmpty()){
            mPermissionResultLaucher.launch(permissionRequest.toArray(new String[0]));
        }
    }

}
