/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.dao;

import com.mycompany.clinic_management_system.adt.ArrayList;
import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.entity.DispenseRecord;
import java.io.*;

/**
 *
 * @author ASUS
 */
public class DispenseRecordDAO {

    private static final String DISPENSE_FILE = "data/Dispense_Record.txt";

    public ListInterface<DispenseRecord> loadDispenseData() {
        ListInterface<DispenseRecord> dispenseRecords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(DISPENSE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    String patientId = parts[0].trim();
                    String medCode = parts[1].trim();
                    String medName = parts[2].trim();
                    int qty = Integer.parseInt(parts[3].trim());
                    String datetime = parts[4].trim();
                    String doctorId = parts[5].trim();

                    DispenseRecord record = new DispenseRecord(patientId, medCode, medName, qty, datetime, doctorId);
                    dispenseRecords.add(record);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading dispense file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing quantity: " + e.getMessage());
        }

        return dispenseRecords;
    }

    public void saveDispenseRecords(ListInterface<DispenseRecord> dispenseList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DISPENSE_FILE))) {
            for (int i = 1; i <= dispenseList.getNumberOfEntries(); i++) {
                DispenseRecord record = dispenseList.getEntry(i);
                writer.write(String.format("%s|%s|%s|%d|%s|%s",
                        record.getPatientId(), record.getMedicineCode(),
                        record.getMedicineName(), record.getQuantity(),
                        record.getDispenseDateTime(), record.getDoctorId()));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving dispense file: " + e.getMessage());
        }
    }
}
