/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.control;

import com.mycompany.clinic_management_system.adt.ArrayList;
import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.dao.DoctorDao;
import com.mycompany.clinic_management_system.entity.Doctor;
import com.mycompany.clinic_management_system.entity.DoctorSchedule;
import com.mycompany.clinic_management_system.utility.Navigation;
import com.mycompany.clinic_management_system.utility.Utility;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 *
 * @author zheng
 */
public class DoctorManager {

    private ListInterface<Doctor> doctorList;
    private Scanner scanner = new Scanner(System.in);
    private DoctorDao doctorDao = new DoctorDao();

    public static final String DOCTOR_FILE_PATH = "data/Doctor_Data.txt";
    public static final String SCHEDULE_FILE_PATH = "data/Schedule_Data.txt";

    // Constructor
    public DoctorManager() {
        this.doctorDao = new DoctorDao();
        doctorList = doctorDao.loadDoctorsFromFile();
    }

    // ================== DOCTOR ==================
    public void addDoctor() {
        System.out.print("Enter Doctor ID (must start with 'D' followed by 4 digits, e.g., D1234): ");
        String id = scanner.nextLine().trim();

        if (!Utility.isValidDoctorId(id)) {
            System.out.println("Error: Invalid ID format. It must start with 'D' followed by 4 digits.");
            return;
        }

        // Check for duplicate ID
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            if (doctorList.getEntry(i).getDoctorID().equalsIgnoreCase(id)) {
                System.out.println("Error: Doctor ID already exists.");
                return;
            }
        }

        System.out.print("Enter Name (letters and spaces only): ");
        String name = scanner.nextLine().trim();

        if (!Utility.isValidName(name)) {
            System.out.println("Error: Name can only contain letters and spaces.");
            return;
        }

        Doctor doctor = new Doctor(id, name);
        doctorList.add(doctor);

        doctorDao.saveDoctorListToFile(doctorList);
        System.out.println("Doctor added and saved to Doctor_Data.txt.");
    }

    public void deleteDoctor() {
        System.out.println("\nList of Doctors:");
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            System.out.println(i + ". " + doctorList.getEntry(i));
        }

        System.out.print("Enter the Doctor ID to delete (e.g., D0001): ");
        String doctorId = scanner.nextLine().trim();

        if (!Utility.isValidDoctorId(doctorId)) {
            System.out.println("Error: Invalid ID format. It must start with 'D' followed by 4 digits.");
            return;
        }

        String doctorName = Utility.getDoctorNameById(doctorId, doctorList);
        if (doctorName.equals("Unknown")) {
            System.out.println("Doctor ID not found.");
            return;
        }

        System.out.print("Confirm deletion for " + doctorName + "? (Y/N): ");
        String confirm = scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        boolean found = false;
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor doctor = doctorList.getEntry(i);
            if (doctor.getDoctorID().equalsIgnoreCase(doctorId)) {
                doctorList.remove(i);
                doctorDao.saveDoctorListToFile(doctorList);
                System.out.println("Doctor removed successfully.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Doctor ID not found.");
        }
    }

    public void updateDoctor() {
        System.out.println("\nList of Doctors:");
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            System.out.println(i + ". " + doctorList.getEntry(i));
        }

        System.out.print("Enter Doctor ID to update (must start with 'D' followed by 4 digits, e.g., D1234): ");
        String id = scanner.nextLine();

        if (!Utility.isValidDoctorId(id)) {
            System.out.println("Error: Invalid ID format. It must start with 'D' followed by 4 digits.");
            return;
        }

        boolean found = false;

        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor doctor = doctorList.getEntry(i);
            if (doctor.getDoctorID().equalsIgnoreCase(id)) {
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();
                if (!Utility.isValidName(newName)) {
                    System.out.println("Error: Name can only contain letters and spaces.");
                    return;
                }
                doctor.setName(newName);
                found = true;
                break;
            }
        }

        if (found) {
            doctorDao.saveDoctorListToFile(doctorList);
            System.out.println("Doctor updated successfully.");
        } else {
            System.out.println("Doctor with ID " + id + " not found.");
        }
    }

    public void showDoctorList() {
        if (doctorList.isEmpty()) {
            System.out.println("No doctors found.");
            return;
        }

        System.out.println("DoctorID | Name");
        System.out.println("==========================");

        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor doctor = doctorList.getEntry(i);
            System.out.printf("%-8s | %s%n", doctor.getDoctorID(), doctor.getName());
        }
    }
}
