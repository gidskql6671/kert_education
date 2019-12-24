package com.kdh.test;

public class Contact {
    private long id;
    private String phoneNumber;
    private String name;

    public void setId(long id){
        this.id = id;
    }
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
