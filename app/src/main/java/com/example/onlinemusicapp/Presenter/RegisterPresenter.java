package com.example.onlinemusicapp.Presenter;

import androidx.annotation.NonNull;

import com.example.onlinemusicapp.Activity.LoginActivity;
import com.example.onlinemusicapp.Model.Account;
import com.example.onlinemusicapp.View.RegisterView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RegisterPresenter {
    private RegisterView registerView;
    DatabaseReference databaseReference = LoginActivity.databaseReference;
    public RegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
    }

    public boolean register(String username,String password,String confirmPassword){
       if(checkRegisterAccount(username, password, confirmPassword)){
           return true;
       }
        return false;
    }

    public boolean checkRegisterAccount(String username,String password,String confirmPassword){
        final boolean[] checkRegisterAccountResult = {false};
        if(username.isEmpty()){
            registerView.registerErrorUsername();
            return false;
        }else if(password.isEmpty()){
            registerView.registerErrorPassword();
            return  false;
        }else if(confirmPassword.isEmpty()){
            registerView.registerErrorConfirmPassword();
            return false;
        }else if(!password.equals(confirmPassword)){
           registerView.registerErrorPasswordAndConfirmPassword();
           return false;
        }
        else{
            databaseReference.child("accounts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //check username before register
                    if(snapshot.hasChild(username)){
                       registerView.registerFailed();
                        checkRegisterAccountResult[0] = false;
                    }else{
                        //sending data to realtime db
                        Account registerAccount = new Account(username,password);
                        databaseReference.child("accounts").child(username).setValue(registerAccount);
//                        databaseReference.child("accounts").child("password").setValue(password);
                        registerView.registerSuccess(username,password);
                        checkRegisterAccountResult[0] = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return  checkRegisterAccountResult[0];
    }
}
