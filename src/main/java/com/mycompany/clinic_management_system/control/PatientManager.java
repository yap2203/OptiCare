/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.control;

import com.mycompany.clinic_management_system.adt.ArrayList;
import com.mycompany.clinic_management_system.adt.LinkedQueue;
import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.adt.QueueInterface;
import com.mycompany.clinic_management_system.entity.Patient;
import com.mycompany.clinic_management_system.boudary.PatientUi;
import com.mycompany.clinic_management_system.dao.PatientDao;
import com.mycompany.clinic_management_system.dao.QueueDao;
import com.mycompany.clinic_management_system.entity.AppointmentQueue;
import com.mycompany.clinic_management_system.utility.Navigation;
import com.mycompany.clinic_management_system.utility.Utility;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author huany
 */
public class PatientManager {
    
    private final PatientUi patientUi;
    private ListInterface<Patient> patientList;
    private final Scanner scanner = new Scanner(System.in);
    private final PatientDao patientDao;
    
    public PatientManager() {
        patientUi = new PatientUi();
        patientList = new ArrayList<>();
        patientDao = new PatientDao();
    }
    
    public void add() {
        Boolean resume;
        String choice;
        String ic;
        String gender;
        String dob;
        String ageStr;
        do {
            patientList = patientDao.loadFromFile();
            resume = false;
            String name = patientUi.inputName();
            ic = Utility.promptUntilValid(
                    patientUi::inputIC,
                    this::isValidIC,
                    "Invalid IC please enter the correct format (e.g. 111111-11-1111)"
            );
            
            gender = Utility.promptUntilValid(
                    patientUi::inputGender,
                    this::isValidGender,
                    "Please enter the correct gender (F(Female), M(Male), O(Other))"
            );
            
            dob = Utility.promptUntilValid(
                    patientUi::inputBirthday,
                    this::isValidBirthday,
                    "Please enter the correct birthday format (e.g. 31/12/2025)"
            );
            
            ageStr = Utility.promptUntilValid(
                    patientUi::inputAge,
                    this::isValidAge,
                    "Please enter a correct age"
            );
            int age = Integer.parseInt(ageStr);
            String id;
            
            if (patientList.isEmpty() || patientList == null) {
                id = "P001";
            } else {
                int lastNum = patientList.getNumberOfEntries();
                String[] splitId = patientList.getEntry(lastNum).getID().split("P");
                int currentId = Integer.parseInt(splitId[1]) + 1;
                id = String.format("P%03d", currentId);
            }
            
            Patient patient = new Patient(
                    name,
                    gender,
                    ic,
                    dob,
                    age,
                    id
            );
            if (patientList.add(patient)) {
                System.out.println("patient added! Here is your patient id: " + id);
            } else {
                System.out.println("Something Wrong");
            }
            Boolean continueChoice = true;
            while (continueChoice) {
                continueChoice = false;
                Utility.showContinueMessage();
                choice = scanner.nextLine();
                
                switch (choice.toLowerCase()) {
                    case "y":
                        resume = true;
                        break;
                    case "n":
                        resume = false;
                        break;
                    default:
                        continueChoice = true;
                        Utility.showInvalidChoiceMessage();
                        break;
                }
            }
            
            patientDao.saveFile(patientList);
        } while (resume);
        
    }
    
    public void edit(Patient patient) {
        Boolean isError;
        do {
            isError = false;
            String[] title = {"1.IC", "2.Name", "3.Gender", "4.birthday", "5.Age", "0.back"};
            
            for (int i = 0; i < title.length; i++) {
                System.out.println(title[i]);
            }
            patientList = patientDao.loadFromFile();
            System.out.print("Select a record to edit: ");
            String choice = scanner.nextLine();
            int numOfPatient = 0;
            
            for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
                if (patient.getID().equals(patientList.getEntry(i).getID())) {
                    numOfPatient = i;
                    break;
                }
            }
            
            switch (choice) {
                case "1" -> {
                    String ic = Utility.promptUntilValid(
                            patientUi::inputIC,
                            this::isValidIC,
                            "Invalid IC please enter the correct format (e.g. 111111-11-1111)"
                    );
                    patient.setIc(ic);
                    patientList.getEntry(numOfPatient).setIc(ic);
                }
                case "2" -> {
                    String name = patientUi.inputName();
                    patient.setName(name);
                    patientList.getEntry(numOfPatient).setName(name);
                }
                case "3" -> {
                    String gender = Utility.promptUntilValid(
                            patientUi::inputGender,
                            this::isValidGender,
                            "Please enter the correct gender (F(Female), M(Male))"
                    );
                    patient.setGender(gender);
                    patientList.getEntry(numOfPatient).setGender(gender);
                }
                case "4" -> {
                    String dob = Utility.promptUntilValid(
                            patientUi::inputBirthday,
                            this::isValidBirthday,
                            "Please enter the correct birthday format (e.g. 31/12/2025)"
                    );
                    patient.setBirthday(dob);
                    patientList.getEntry(numOfPatient).setBirthday(dob);
                }
                case "5" -> {
                    String ageStr = Utility.promptUntilValid(
                            patientUi::inputAge,
                            this::isValidAge,
                            "Please enter a correct age"
                    );
                    int age = Integer.parseInt(ageStr);
                    patient.setAge(age);
                    patientList.getEntry(numOfPatient).setAge(age);
                }
                case "0" -> {
                    break;
                }
                default -> {
                    isError = true;
                    Utility.showInvalidChoiceMessage();
                    
                }
            }
        } while (isError);
        
        patientDao.saveFile(patientList);
        System.out.println("Edit Successfully");
        
    }
    
    public void delete(Patient patient) {
        patientList = patientDao.loadFromFile();
        QueueDao queueDao = new QueueDao();
        QueueInterface<AppointmentQueue> queue = queueDao.loadFromFile();
        Iterator<AppointmentQueue> iterator = queue.getIterator();
        
        String choice;
        String patientId = null;
        do {
            System.out.print("Are you sure? (Y/N): ");
            choice = scanner.nextLine().trim();
            
            if (choice.equalsIgnoreCase("y")) {
                for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
                    patientId = patientList.getEntry(i).getID();
                    if (patient.getID().equals(patientList.getEntry(i).getID())) {
                        patientList.remove(i);
                        break;
                    }
                }
                while (iterator.hasNext()) {
                    AppointmentQueue currentQueue = iterator.next();
                    if (currentQueue.getPatientId().equalsIgnoreCase(patientId) && patientId != null) {
                        queue.remove(currentQueue);
                    }
                }
                queueDao.saveFile(queue);
                patientDao.saveFile(patientList);
                System.out.println("Patient removed successfully.");
            } else if (choice.equalsIgnoreCase("n")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter Y or N.");
            }
            
        } while (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n"));
        if (choice.equalsIgnoreCase("y")) {
            Navigation.goBack();
        }
        
    }
    
    public Patient checkValidPatient() {
        String id;
        String ic;
        Patient patient;
        do {
            id = patientUi.inputPatientId();
            patient = Utility.getPatientFromList(id);
            if (patient == null) {
                System.out.println("Patient not found!!Please process to register");
            }
        } while (patient == null);
        do {
            ic = patientUi.inputIC();
            if (!ic.equals(patient.getIc())) {
                System.out.println("please Enter the correct ic");
            }
        } while (!ic.equals(patient.getIc()));
        return patient;
    }
    
    public boolean isValidIC(String ic) {
        return ic != null && ic.matches("\\d{6}-\\d{2}-\\d{4}");
    }
    
    public boolean isValidGender(String gender) {
        return gender != null && gender.matches("[fmoFMO]");
    }
    
    public boolean isValidBirthday(String birthday) {
        if (birthday == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(birthday);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    public boolean isValidAge(String ageStr) {
        if (ageStr == null || ageStr.isBlank()) {
            return false;
        }
        try {
            int age = Integer.parseInt(ageStr);
            return age >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
