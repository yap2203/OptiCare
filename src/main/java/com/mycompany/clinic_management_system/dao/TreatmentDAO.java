/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.dao;

import com.mycompany.clinic_management_system.adt.ArrayList;
import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.entity.Patient;
import com.mycompany.clinic_management_system.entity.Doctor;
import com.mycompany.clinic_management_system.entity.Treatment;
import java.io.BufferedReader;
import java.io.FileReader;
import com.mycompany.clinic_management_system.utility.Utility;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class TreatmentDAO {

    private static final String TREATMENT_FILE = "data/treatmentrecord.txt";

    public ListInterface<Treatment> loadTreatmentRecord() {
        ListInterface<Treatment> treatmentList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(TREATMENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 13) {
                    String treatmentId = parts[0].trim();
                    String patientId = parts[1].trim();
                    String patientName = parts[2].trim();
                    String doctorId = parts[3].trim();
                    String doctorName = parts[4].trim();
                    Date treatmentDate = Utility.convertStringToDate(parts[5].trim());
                    String diagnosis = parts[6].trim();
                    String prescription = parts[7].trim();
                    String treatmentNotes = parts[8].trim();
                    Date nextAppointment = Utility.convertStringToDate(parts[9].trim());
                    double treatmentCost = Double.parseDouble(parts[10].trim());
                    String[] medicineCodes = parts[11].trim().split(",");
                    String[] quantityStrings = parts[12].trim().split(",");
                    int[] quantities = new int[quantityStrings.length];
                    for (int i = 0; i < quantityStrings.length; i++) {
                        quantities[i] = Integer.parseInt(quantityStrings[i].trim());
                    }
                    
                    Patient patient = Utility.getPatientFromList(patientId);
                    Doctor doctor = Utility.getDoctorById(doctorId);
                    Treatment treatment = new Treatment(treatmentId, patient, doctor, treatmentDate, diagnosis, prescription, treatmentNotes, nextAppointment, treatmentCost, medicineCodes, quantities);
                    treatmentList.add(treatment);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading treatment file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number in treatment file: " + e.getMessage());
        }

        return treatmentList;
    }

    public void saveTreatmentRecord(ListInterface<Treatment> treatmentList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TREATMENT_FILE))) {
            for (int i = 1; i <= treatmentList.getNumberOfEntries(); i++) {
                Treatment treatment = treatmentList.getEntry(i);

                // Convert medicineCodes array to comma-separated string
                String[] medicineCodes = treatment.getMedicineCodes();
                String medicineCodesStr = String.join(",", medicineCodes);

                // Convert quantities array to comma-separated string
                int[] quantities = treatment.getQuantities();
                StringBuilder quantitiesStrBuilder = new StringBuilder();
                for (int j = 0; j < quantities.length; j++) {
                    quantitiesStrBuilder.append(quantities[j]);
                    if (j < quantities.length - 1) {
                        quantitiesStrBuilder.append(",");
                    }
                }
                String quantitiesStr = quantitiesStrBuilder.toString();

                // Write the record
                writer.write(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%.2f|%s|%s",
                        treatment.getTreatmentId(),
                        treatment.getPatient().getID(),
                        treatment.getPatient().getName(),
                        treatment.getDoctor().getDoctorID(),
                        treatment.getDoctor().getName(),
                        Utility.convertDateToString(treatment.getTreatmentDate()),
                        treatment.getDiagnosis(),
                        treatment.getPrescription(),
                        treatment.getTreatmentNotes(),
                        Utility.convertDateToString(treatment.getNextAppointment()),
                        treatment.getTreatmentCost(),
                        medicineCodesStr,
                        quantitiesStr
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving treatment file: " + e.getMessage());
        }
    }

}
