package com.example.appoidochef.data.model;
public class LoginResponse {
    private boolean success;
    private String error; // opcional

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }
}

