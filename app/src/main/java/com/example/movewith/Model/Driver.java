package com.example.movewith.Model;

import java.io.Serializable;
import java.util.Date;

public class Driver implements Serializable {
    public String fullName;
    public String gender;
    public int age;
    public String phoneNumber;
    public double price;
    public Date time;
    public Address source;
    public Address destination;
    public boolean canceled;

    public Driver(String fullName, String gender, int age, String phoneNumber, double price, Date time, Address source, Address destination) {
        this.fullName = fullName;
        this.gender = gender;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.price = price;
        this.time = time;
        this.source = source;
        this.destination = destination;
        this.canceled = false;
    }

    public Driver() {
    }
}
