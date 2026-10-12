/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.dao;
import com.mycompany.clinic_management_system.adt.ArrayList;
import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.entity.Doctor;
import java.io.*;
import java.nio.file.*;
/**
 *
 * @author zheng
 */
public class DoctorDao {

    public static final String DOCTOR_FILE_PATH = "data/Doctor_Data.txt";

    public ListInterface<Doctor> loadDoctorsFromFile() {
        
        ListInterface<Doctor> doctorList = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTOR_FILE_PATH))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (lineNumber <= 2 || line.trim().isEmpty()) {
                    continue;
                }
                if (line.endsWith("|")) {
                    line = line.substring(0, line.length() - 1);
                }

                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    Doctor doctor = new Doctor(id, name);
                    doctorList.add(doctor);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading doctor file: " + e.getMessage());
        }
        return doctorList;
    }

    public void saveDoctorListToFile(ListInterface<Doctor> doctorList) {
        Path filePath = Path.of(DOCTOR_FILE_PATH);
        StringBuilder content = new StringBuilder();
        content.append("DoctorID | Name |\n");
        content.append("=========================\n");

        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor doctor = doctorList.getEntry(i);
            content.append(String.format("%s|%s|%n", doctor.getDoctorID(), doctor.getName()));
        }

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, content.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Doctor list saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving doctor list: " + e.getMessage());
        }
    }
}
