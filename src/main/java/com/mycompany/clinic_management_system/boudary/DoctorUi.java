/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.boudary;

import com.mycompany.clinic_management_system.control.DoctorManager;
import com.mycompany.clinic_management_system.control.DoctorScheduleManager;
import com.mycompany.clinic_management_system.control.ReportManager;
import com.mycompany.clinic_management_system.utility.Navigation;
import com.mycompany.clinic_management_system.utility.Utility;

/**
 *
 * @author zheng
 */
public class DoctorUi {

    private static final DoctorManager dm = new DoctorManager();
    private static final DoctorScheduleManager sm = new DoctorScheduleManager();
    private static final ReportManager report = new ReportManager();

    public static void menu() {
        Navigation.stack.push(doctorMainMenu);

        boolean exit = false;

        while (!Navigation.stack.isEmpty() && !exit) {
            Runnable currentMenu = Navigation.stack.peek();
            if (currentMenu != null) {
                currentMenu.run();
            } else {
                exit = true;
            }
        }

        System.out.println("Returning to main menu...");
    }

    private static final Runnable manageDoctorMenu = () -> {
        String[] options = {
            "1. Show Doctor List",
            "2. Add Doctor",
            "3. Update Doctor",
            "4. Delete Doctor",
            "0. Back"
        };

        Runnable[] actions = {
            () -> dm.showDoctorList(),
            () -> dm.addDoctor(),
            () -> dm.updateDoctor(),
            () -> dm.deleteDoctor(),
            () -> Navigation.goBack() 
        };

        Utility.customMenu(options, "Manage Doctor", "Enter your choice: ", actions);
    };

    private static final Runnable manageScheduleMenu = () -> {
        String[] options = {
            "1. Show Doctor Schedule",
            "2. Add Doctor Schedule",
            "3. Update Doctor Schedule",
            "4. Delete Doctor Schedule",
            "5. Doctor Apply Leave",
            "0. Back"
        };

        Runnable[] actions = {
            () -> sm.showSchedules(),
            () -> sm.addSchedule(),
            () -> sm.updateSchedule(),
            () -> sm.deleteSchedule(),
            () -> sm.applyLeave(),
            () -> Navigation.goBack() 
        };

        Utility.customMenu(options, "Manage Doctor Schedule", "Enter your choice: ", actions);
    };

    private static final Runnable reportMenu = () -> {
        String[] options = {
            "1. Generate Doctor Workload Report",
            "2. Generate Doctor Availability Report",
            "0. Back"
        };

        Runnable[] actions = {
            () -> report.generateDoctorWorkloadReport(),
            () -> report.generateDoctorAvailabilityReport(),
            () -> Navigation.goBack() 
        };

        Utility.customMenu(options, "Generate Report", "Enter your choice: ", actions);
    };
    
        private static final Runnable doctorMainMenu = () -> {
        String[] options = {
            "1. Manage Doctor",
            "2. Manage Doctor Schedule",
            "3. View Reports",
            "0. Back to Main Menu"
        };

        Runnable[] actions = {
            () -> Navigation.stack.push(manageDoctorMenu),
            () -> Navigation.stack.push(manageScheduleMenu),
            () -> Navigation.stack.push(reportMenu),
            () -> MainUi.mainUI()
        };

        Utility.customMenu(options, "Doctor Management", "Enter your choice: ", actions);
    };
}
