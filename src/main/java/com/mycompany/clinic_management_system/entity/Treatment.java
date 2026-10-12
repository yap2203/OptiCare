package com.mycompany.clinic_management_system.entity;

import java.util.Date;

public class Treatment {

    private String treatmentId;
    private Patient patient;
    private Doctor doctor;
    private Date treatmentDate;
    private String diagnosis;
    private String prescription;
    private String treatmentNotes;
    private Date nextAppointment;
    private double treatmentCost;
    private String[] medicineCodes;
    private int[] quantities;

    public Treatment(String treatmentId,Patient patient,Doctor doctor, Date treatmentDate, String diagnosis, String prescription, String treatmentNotes, Date nextAppointment, double treatmentCost, String[] medicineCodes, int[] quantities) {
        this.treatmentId = treatmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.treatmentDate = treatmentDate;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.treatmentNotes = treatmentNotes;
        this.nextAppointment = nextAppointment;
        this.treatmentCost = treatmentCost;
        this.medicineCodes = medicineCodes;
        this.quantities = quantities;
    }

//    public Treatment(String treatmentId, Patient patient, Doctor doctor, Date treatmentDate, String diagnosis, String prescription, String treatmentNotes, Date nextAppointment, double treatmentCost) {
//        this.treatmentId = treatmentId;
//        this.patient = patient;
//        this.doctor = doctor;
//        this.treatmentDate = treatmentDate;
//        this.diagnosis = diagnosis;
//        this.prescription = prescription;
//        this.treatmentNotes = treatmentNotes;
//        this.nextAppointment = nextAppointment;
//        this.treatmentCost = treatmentCost;
//    }
    public String getTreatmentId() {
        return treatmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Date getTreatmentDate() {
        return treatmentDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public String getTreatmentNotes() {
        return treatmentNotes;
    }

    public Date getNextAppointment() {
        return nextAppointment;
    }

    public double getTreatmentCost() {
        return treatmentCost;
    }
    

    public String[] getMedicineCodes() {
        return medicineCodes;
    }

    public int[] getQuantities() {
        return quantities;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setTreatmentDate(Date treatmentDate) {
        this.treatmentDate = treatmentDate;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public void setTreatmentNotes(String treatmentNotes) {
        this.treatmentNotes = treatmentNotes;
    }

    public void setNextAppointment(Date nextAppointment) {
        this.nextAppointment = nextAppointment;
    }

    public void setTreatmentCost(double treatmentCost) {
        this.treatmentCost = treatmentCost;
    }
    
    
    public void setMedicineCodes(String[] medicineCodes) {
        this.medicineCodes = medicineCodes;
    }

    public void setQuantities(int[] quantities) {
        this.quantities = quantities;
    }

    @Override
    public String toString() {
        return "Treatment{" + "treatmentId=" + treatmentId + ", patient=" + patient + ", doctor=" + doctor + ", treatmentDate=" + treatmentDate + ", diagnosis=" + diagnosis + ", prescription=" + prescription + ", treatmentNotes=" + treatmentNotes + ", nextAppointment=" + nextAppointment + ", treatmentCost=" + treatmentCost + '}';
    }
}
