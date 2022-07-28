package com.example.onlinemusicapp.View;

public interface LoginView {
    public void login(String username, String password);
    public void loginFailed();
    public void loginSuccess(String username);
    public void loginUsernameError();
    public void loginPasswordError();
    public void passwordWrong();
}
