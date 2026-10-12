package com.mycompany.clinic_management_system.entity;

import java.util.Date;

public class Payment {
    public static final String PAYMENT_PENDING = "PENDING";
    public static final String PAYMENT_PAID = "PAID";
    public static final String PAYMENT_FAILED = "FAILED";
    public static final double DIANOGSIS_FEE = 30.0;
    private String paymentId;         // Unique ID for each payment
    private Treatment treatment;      // Associated treatment
    private Patient patient;          // Patient making the payment
    private double amountPaid;        // Amount paid by patient
    private String paymentMethod;     // e.g., Cash, Card, Insurance
    private String paymentStatus;     // e.g., Pending, Completed, Failed
    private Date paymentDate;         // When payment was made

    public Payment(String paymentId, Treatment treatment, Patient patient, 
                   double amountPaid, String paymentMethod, String paymentStatus, Date paymentDate) {
        this.paymentId = paymentId;
        this.treatment = treatment;
        this.patient = patient;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public Patient getPatient() {
        return patient;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", treatment=" + (treatment != null ? treatment.getTreatmentId() : "null") +
                ", patient=" + (patient != null ? patient.getID() : "null") +
                ", amountPaid=" + amountPaid +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
