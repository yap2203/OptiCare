/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.dao;

import com.mycompany.clinic_management_system.adt.ArrayList;
import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.entity.Patient;
import java.io.*;

/**
 *
 * @author huany
 */
public class PatientDao {

    private String fileName = "data/patient.dat";

    public void saveFile(ListInterface<Patient> patientList) {
        File file = new File(fileName);
        try {
            ObjectOutputStream ooStream = new ObjectOutputStream(new FileOutputStream(file));
            ooStream.writeObject(patientList);
            ooStream.close();
        } catch (IOException e) {
            System.out.println("Cannot save to file" + e.getMessage());
        }
    }

    public ListInterface<Patient> loadFromFile() {
        File file = new File(fileName);
        ListInterface<Patient> patientList = new ArrayList<>();
        try {
            ObjectInputStream oiStream = new ObjectInputStream(new FileInputStream(file));
            patientList = (ArrayList<Patient>) (oiStream.readObject());
            oiStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("No such file");

        } catch (IOException ex) {
            System.out.println("Cannot read from file");
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found");
        } finally {
            return patientList;
        }
    }
}
