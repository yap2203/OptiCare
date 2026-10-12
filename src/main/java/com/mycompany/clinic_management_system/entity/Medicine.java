/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.entity;

/**
 *
 * @author ASUS
 */
public class Medicine {

    private String medicineCode;
    private String medicineName;
    private String description;
    private String dosage;
    private int quantityInStock;
    private double pricePerUnit;

    // Constructor
    public Medicine(String medicineCode, String medicineName, String description, String dosage, int quantityInStock, double pricePerUnit) {
        this.medicineCode = medicineCode;
        this.medicineName = medicineName;
        this.description = description;
        this.dosage = dosage;
        this.quantityInStock = quantityInStock;
        this.pricePerUnit = pricePerUnit;
    }

    // Getters
    public String getMedicineCode() {
        return medicineCode;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getDescription() {
        return description;
    }

    public String getDosage() {
        return dosage;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    // Setters
    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    // Method to reduce stock when dispensing
    public boolean reduceStock(int quantity) {
        if (quantity <= quantityInStock) {
            quantityInStock -= quantity;
            return true;
        }
        return false;
    }

    // Method to increase stock when restocking
    public void addStock(int quantity) {
        this.quantityInStock += quantity;
    }

    @Override
    public String toString() {
        return "Medicine Code: " + medicineCode
                + "\nName: " + medicineName
                + "\nDescription: " + description
                + "\nDosage: " + dosage
                + "\nStock: " + quantityInStock
                + "\nPrice (per unit): RM " + String.format("%.2f", pricePerUnit);
    }

}
