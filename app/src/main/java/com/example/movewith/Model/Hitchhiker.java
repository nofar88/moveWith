package com.example.movewith.Model;

import java.io.Serializable;
import java.util.Date;

public class Hitchhiker implements Serializable {
        public String fullName;
        public String gender;
        public int age;
        public String phoneNumber;
        public Date time;
        public Address source;
        public Address destination;

        public Hitchhiker(String fullName, String gender, int age, String phoneNumber, Date time, Address source, Address destination) {
            this.fullName = fullName;
            this.gender = gender;
            this.age = age;
            this.phoneNumber = phoneNumber;
            this.time = time;
            this.source = source;
            this.destination = destination;
        }

}
