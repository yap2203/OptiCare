/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.clinic_management_system.boudary;

/**
 *
 * @author huany
 */
import com.mycompany.clinic_management_system.utility.Navigation;
import com.mycompany.clinic_management_system.utility.Utility;

public class MainUi {
    
    public static void mainUI() {
        Navigation.stack.push(() -> mainUI());
        System.out.println("  ___        _   _                       ____ _ _       _       ");
        System.out.println(" / _ \\ _ __ | |_(_) ___ __ _ _ __ ___   / ___| (_)_ __ (_) ___  ");
        System.out.println("| | | | '_ \\| __| |/ __/ _` | '__/ _ \\ | |   | | | '_ \\| |/ __| ");
        System.out.println("| |_| | |_) | |_| | (_| (_| | | |  __/ | |___| | | | | | | (__  ");
        System.out.println(" \\___/| .__/ \\__|_|\\___\\__,_|_|  \\___|  \\____|_|_|_| |_|_|\\___| ");
        System.out.println("      |_|                                                       ");

        Utility.customMenu(
                new String[]{"1.Patient", "2.Doctor", "3.Treatment", "4.Pharmacist", "0.Exit"},
                "Main Menu",
                "Select a role: ",
                new Runnable[]{
                    () -> PatientUi.menu(),
                    () -> DoctorUi.menu(),
                    () -> TreatmentUi.menu(),
                    () -> PharmacyUi.menu(),
                    () -> System.exit(0)
                }
        );
        
    }
}
