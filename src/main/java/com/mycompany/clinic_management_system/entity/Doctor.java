/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.entity;

import java.io.Serializable;

/**
 *
 * @author zheng
 */
public class Doctor implements Serializable {

    private String doctorID;
    private String name;

    public Doctor(String doctorID, String name) {
        this.doctorID = doctorID;
        this.name = name;
    }

    // getter and setters
    public String getDoctorID() {
        return doctorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

     @Override
    public String toString() {
        return "DoctorID: " + doctorID + ", Name: " + name;
    }
}
