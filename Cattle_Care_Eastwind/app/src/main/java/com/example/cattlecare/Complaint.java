package com.example.cattlecare;

import android.graphics.Bitmap;

public class Complaint {

    private String ID;
    private String Address;
    private String Status;




    public Complaint() {
    }

    public Complaint(String id, String address, String status) {
        ID = id;
        Address = address;
        Status = status;


    }

    public String getID() {
        return ID;
    }

    public String getAddress() {
        return Address;
    }

    public String getStatus() {
        return Status;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setStatus(String status) {
        Status = status;
    }







}
