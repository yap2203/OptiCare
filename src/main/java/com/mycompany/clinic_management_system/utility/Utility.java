/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.utility;

/**
 *
 * @author huany
 */
import com.mycompany.clinic_management_system.adt.ArrayList;
import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.dao.DoctorDao;
import com.mycompany.clinic_management_system.dao.PatientDao;
import com.mycompany.clinic_management_system.entity.Doctor;
import com.mycompany.clinic_management_system.entity.Patient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;
import java.util.function.*;

public class Utility {

    public static void customMenu(String[] title, String menuTitle, String customMessage, Runnable[] action) {
        Scanner scanner = new Scanner(System.in);
        Boolean resume;

        do {
            resume = false;
            System.out.println();
            if (menuTitle != null && !menuTitle.isEmpty()) {
                System.out.printf("==== %s ====\n", menuTitle);
            }

            for (String titleItem : title) {
                System.out.println(titleItem);
            }

            System.out.print(customMessage);
            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                resume = true;
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            if (choice == 0) {
                action[title.length - 1].run();
                break; // exit menu
            } else if (choice >= 1 && choice <= title.length - 1) {
                action[choice - 1].run();
            } else {
                resume = true;
                showInvalidChoiceMessage();
            }
        } while (resume);
    }

    public static void showContinueMessage() {
        System.out.print("Do you want to continue(Y/N): ");
    }

    public static void showInvalidChoiceMessage() {
        System.out.println("Invalid choice! Try again.");
    }

    public static Date convertStringToDate(String strDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertDateToString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    // Get doctor name by ID from doctor list
    public static String getDoctorNameById(String doctorID, ListInterface<Doctor> doctorList) {
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor doc = doctorList.getEntry(i);
            if (doc.getDoctorID().equalsIgnoreCase(doctorID)) {
                return doc.getName();
            }
        }
        return "Unknown";
    }

    /// Validation
    public static boolean isValidDoctorId(String id) {
        return id.matches("^[Dd]\\d{4}$");
    }

    public static boolean isValidName(String name) {
        return name.matches("^[A-Za-z ]+$");
    }

    public static Patient getPatientFromList(String id) {
        PatientDao patientDao = new PatientDao();
        Patient patient = null;
        ListInterface<Patient> patientList = patientDao.loadFromFile();
        if (patientList != null) {
            for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
                patient = patientList.getEntry(i);
                if (patient.getID().equals(id)) {
                    return patient;
                }
            }
        }
        return null;
    }

    public static Doctor getDoctorById(String doctorID) {
        DoctorDao doctorDao = new DoctorDao();
        Doctor doc = null;
        ListInterface<Doctor> doctorList = doctorDao.loadDoctorsFromFile();
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            doc = doctorList.getEntry(i);
            if (doc.getDoctorID().equals(doctorID)) {
                return doc;
            }

        }
        return null;
    }

    public static String getCurrentDateTimeString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }

    public static String promptUntilValid(Supplier<String> inputSupplier, Predicate<String> validator, String errorMessage) {
        String value;
        do {
            value = inputSupplier.get();
            if (!validator.test(value)) {
                System.out.println(errorMessage);
                value = null;
            }
        } while (value == null);
        return value;
    }

    public static ArrayList<String> getDaysOfWeek() {
        ArrayList<String> daysOfWeek = new ArrayList<>();
        daysOfWeek.add("Monday");
        daysOfWeek.add("Tuesday");
        daysOfWeek.add("Wednesday");
        daysOfWeek.add("Thursday");
        daysOfWeek.add("Friday");
        daysOfWeek.add("Saturday");
        daysOfWeek.add("Sunday");
        return daysOfWeek;
    }

    public static String getToday() {
        DayOfWeek day = LocalDate.now().getDayOfWeek();
        return capitalize(day.toString().toLowerCase()); // "Wednesday"
    }

    public static String getNextDay() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        DayOfWeek next = today.plus(1); // adds 1 day
        return capitalize(next.toString().toLowerCase()); // "Thursday"
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
