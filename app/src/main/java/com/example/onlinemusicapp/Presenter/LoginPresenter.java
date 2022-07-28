package com.example.onlinemusicapp.Presenter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.onlinemusicapp.Activity.LoginActivity;
import com.example.onlinemusicapp.View.LoginView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginPresenter {
    private LoginView loginView;
    DatabaseReference databaseReference = LoginActivity.databaseReference;
    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    public boolean login(String username, String password,Context context){
        if(checkLoginAccount(username,password,context)) return true;
        return false;
    }

    public boolean checkLoginAccount(String username, String password, Context context){
        boolean checkLoginAccountResult[] = {false};
        if(username.isEmpty()){
            loginView.loginUsernameError();
            return false;
        }else if(password.isEmpty()){
            loginView.loginPasswordError();
            return false;
        }else{
            databaseReference.child("accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //check username is exits in db
                    if(snapshot.hasChild(username)){
                        String getPassword = snapshot.child(username).child("password").getValue().toString();
                        if(password.equals(getPassword)) {
                            loginView.loginSuccess(username);
                            checkLoginAccountResult[0] = true;
                        }else{
                            loginView.passwordWrong();
                            checkLoginAccountResult[0] = false;
                        }
                    }else{
                        loginView.loginFailed();;
                        checkLoginAccountResult[0] = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return checkLoginAccountResult[0];
    }
}
