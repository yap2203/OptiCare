/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.entity;

import java.io.Serializable;

/**
 *
 * @author huany
 */
public class Booking implements Serializable {
    
    private String bookingId,date,day,status,bookingDate,patientId,doctorId;

    public Booking(String bookingId, String date, String day, String status, String bookingDate, String patientId, String doctorId) {
        this.bookingId = bookingId;
        this.date = date;
        this.day = day;
        this.status = status;
        this.bookingDate = bookingDate;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }
    
    public String getBookingId() {
        return bookingId;
    }


    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public String toString() {
        return "Booking{" + "bookingId=" + bookingId + ", date=" + date + ", day=" + day + ", status=" + status + ", bookingDate=" + bookingDate + ", patientId=" + patientId + ", doctorId=" + doctorId + '}';
    }

    
}
