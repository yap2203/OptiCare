/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.dao;

import com.mycompany.clinic_management_system.adt.ArrayList;
import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.entity.Medicine;
import java.io.*;

/**
 *
 * @author ASUS
 */
public class MedicineDAO {

    private static final String MEDICINE_FILE = "data/medicine.txt";

    public ListInterface<Medicine> loadMedicineData() {
        ListInterface<Medicine> medicineList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(MEDICINE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    String code = parts[0];
                    String name = parts[1];
                    String description = parts[2];
                    String dosage = parts[3];
                    int quantity = Integer.parseInt(parts[4]);
                    double price = Double.parseDouble(parts[5]);

                    Medicine medicine = new Medicine(code, name, description, dosage, quantity, price);
                    medicineList.add(medicine);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading medicine file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number in medicine file: " + e.getMessage());
        }

        return medicineList;
    }

    public void saveMedicineData(ListInterface<Medicine> medicineList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEDICINE_FILE))) {
            for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
                Medicine med = medicineList.getEntry(i);
                writer.write(String.format("%s|%s|%s|%s|%d|%.2f",
                        med.getMedicineCode(), med.getMedicineName(),
                        med.getDescription(),med.getDosage(), med.getQuantityInStock(),
                        med.getPricePerUnit()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving medicine file: " + e.getMessage());
        }
    }
}
