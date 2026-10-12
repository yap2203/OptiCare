/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.entity;

/**
 *
 * @author zheng
 */
public class DoctorSchedule {

    private String doctorID;
    private String day;
    private String status;

    public DoctorSchedule(String doctorID, String day) {
        this.doctorID = doctorID;
        this.day = day;
        this.status = "Available";
    }
    
    public DoctorSchedule(String doctorID, String day, String status) {
        this.doctorID = doctorID;
        this.day = day;
        this.status = status;
    }
      

    public String getDoctorID() {
        return doctorID;
    }

    public String getDay() {
        return day;
    }

    public String getStatus() {
        return status;
    }

    public void setDay(String day) {
        this.day = day;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DoctorID: " + doctorID + ", Day: " + day + ", Status: " + status;
    }
}
