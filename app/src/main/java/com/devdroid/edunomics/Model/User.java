package com.devdroid.edunomics.Model;

public class User {
    private String id;
    private String username;
    private String phone;
    private String email;
    private String status;

    public User(String id,String username,String phone,String email,String status){
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.status = status;
    }
    public User(){

    }
    public String getEmail(){
        return email;
    }
    public String getID(){
        return id;
    }
    public  String getUsername(){
        return username;
    }
    public String getPhone(){
        return phone;
    }
    public void setID(String id){
        this.id = id;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
