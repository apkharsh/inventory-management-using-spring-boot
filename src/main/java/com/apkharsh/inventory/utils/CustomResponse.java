package com.apkharsh.inventory.utils;

public class CustomResponse {
    private String message;

    public CustomResponse(){
        super();
    }

    public CustomResponse(String message){
        super();
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
