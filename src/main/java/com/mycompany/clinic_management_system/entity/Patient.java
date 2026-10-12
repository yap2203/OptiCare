/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.entity;

import java.io.Serializable;

/**
 *
 * @author huany
 */
public class Patient implements Serializable {

    private String name, gender, ic, birthday, patientId;
    private int age;

    public Patient(String patientId, String name) {
        this.patientId = patientId;
        this.name = name;

    }

    public Patient(String name, String gender, String ic, String dob, int age, String patientId) {
        this.name = name;
        this.gender = gender;
        this.ic = ic;
        this.birthday = dob;
        this.age = age;
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getID() {
        return patientId;
    }

    public void setID(String patientId) {
        this.patientId = patientId;
    }

}
