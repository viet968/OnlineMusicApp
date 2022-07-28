package com.example.onlinemusicapp.View;

public interface RegisterView {
    public void register(String username,String password,String confirmPassword);
    public void registerErrorUsername();
    public void registerErrorPassword();
    public void registerErrorConfirmPassword();
    public void registerErrorPasswordAndConfirmPassword();
    public void registerFailed();
    public void registerSuccess(String username,String password);
}
