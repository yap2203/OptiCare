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
public class AppointmentQueue implements Serializable {

    private String timeSlot, queueId, status, day,patientId,doctorId,bookingId;
    private int num;

    public AppointmentQueue(String timeSlot, String queueId, String doctorId, String patientId, int num, String status, String day, String bookingId) {
        this.timeSlot = timeSlot;
        this.queueId = queueId;
        this.status = status;
        this.day = day;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.bookingId = bookingId;
        this.num = num;
    }

    public AppointmentQueue(String timeSlot, String queueId, String doctorId, String patientId, int num, String status, String day) {
        this.timeSlot = timeSlot;
        this.queueId = queueId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.num = num;
        this.status = status;
        this.day = day;
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

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
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

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

}
