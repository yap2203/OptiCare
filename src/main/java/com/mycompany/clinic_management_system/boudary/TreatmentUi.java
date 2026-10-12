/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.boudary;

import com.mycompany.clinic_management_system.adt.ArrayStack;
import com.mycompany.clinic_management_system.adt.StackInterface;
import com.mycompany.clinic_management_system.control.PaymentManager;
import com.mycompany.clinic_management_system.control.ReportManager;
import com.mycompany.clinic_management_system.control.TreatmentManager;
import com.mycompany.clinic_management_system.utility.Utility;

public class TreatmentUi {
    private static final StackInterface<Runnable> menuStack = new ArrayStack<>();
    
    public static void menu() {
        menuStack.push(treatmentMainMenu);

        boolean exit = false;

        while (!menuStack.isEmpty() && !exit) {
            Runnable currentMenu = menuStack.peek();
            if (currentMenu != null) {
                currentMenu.run();
            } else {
                exit = true;
            }
        }

        System.out.println("Returning to main menu...");
        MainUi.mainUI();
    }
    
    private static final Runnable generateReport = () -> {
        ReportManager reportManager = new ReportManager();
        String options[] = {
            "1. Generate Average Treatment Cost Report",
            "2. Generate Diagnosis Frequency Report",
            "0. Back"
        };
        
        Runnable[] actions = {
            () -> reportManager.generateAverageTreatmentCostReport(),
            () -> reportManager.generateDiagnosisFrequencyReport(),
            () -> menuStack.pop(),
        };

        Utility.customMenu(options, "Treatment Record Menu", "Choose an option: ", actions);
    };
    
    private static final Runnable treatmentRecord = () -> {
        TreatmentManager treatmentManager = new TreatmentManager();
        String options[] = {
            "1. View Treatment Record",
            "2. Modify Treatment Record",
            "3. Delete Treatment Record",
            "0. Back"
        };
        
        Runnable[] actions = {
            () -> treatmentManager.displayTreatmentRecord(),
            () -> treatmentManager.modify(),
            () -> treatmentManager.delete(),
            () -> menuStack.pop(),
        };

        Utility.customMenu(options, "Treatment Record Menu", "Choose an option: ", actions);
    };
    
    private static final Runnable paymentMenu = () -> {
        String options[] = {
            "1. View Payment",
            "2. Generate Payment report",
            "0. Back"
        };
        
        Runnable[] actions = {
            () -> {PaymentManager paymentManager = new PaymentManager();
                paymentManager.displayPaymentRecord();
            },
            () -> {ReportManager reportManager = new ReportManager();
                reportManager.generateDailyPaymentReport();
            },
            () -> menuStack.pop(),
        };
        
        Utility.customMenu(options, "Payment Menu", "Choose an option: ", actions);
    };
    
    private static final Runnable treatmentMainMenu = () -> {        
        TreatmentManager treatmentManager = new TreatmentManager();
        String options[] = {
            "1. Diagnose",
            "2. Treatment Record",
            "3. Generate Report",
            "4. Payment Record",
            "0. Back"
        };
        
        Runnable[] actions = {
            () -> treatmentManager.diagnose(),
            () -> menuStack.push(treatmentRecord),
            () -> menuStack.push(generateReport),
            () -> menuStack.push(paymentMenu),
            () -> menuStack.pop(),
        };

        Utility.customMenu(options, "Treatment Menu", "Choose an option: ", actions);
        
    };
    
}
