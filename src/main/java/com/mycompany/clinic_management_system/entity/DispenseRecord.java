/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.entity;

/**
 *
 * @author ASUS
 */
public class DispenseRecord {

    private String patientId;
    private String medicineCode;
    private String medicineName;
    private int quantity;
    private String dispenseDateTime;
    private String doctorId;

    public DispenseRecord(String patientId, String medicineCode, String medicineName, int quantity, String dispenseDateTime, String doctorId) {
        this.patientId = patientId;
        this.medicineCode = medicineCode;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.dispenseDateTime = dispenseDateTime;
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDispenseDateTime() {
        return dispenseDateTime;
    }

    public String getDoctorId() {
        return doctorId;
    }

    @Override
    public String toString() {
        return patientId + " | " + medicineCode + " | " + medicineName + " | " + quantity + " | " + dispenseDateTime + " | " + doctorId;
    }
}
