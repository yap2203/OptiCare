/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.boudary;

import com.mycompany.clinic_management_system.control.MedicineManager;
import com.mycompany.clinic_management_system.control.DispenseRecordManager;
import com.mycompany.clinic_management_system.control.ReportManager;
import com.mycompany.clinic_management_system.entity.Medicine;
import com.mycompany.clinic_management_system.utility.Navigation;
import com.mycompany.clinic_management_system.utility.Utility;
import java.util.Scanner;

/**
 *
 * @author ASUS
 */
public class PharmacyUi {

    private static final DispenseRecordManager manager = new DispenseRecordManager();
    private static final MedicineManager medicine = new MedicineManager();
    private static final ReportManager report = new ReportManager();

    public static void menu() {
        Navigation.stack.push(pharmacyMainMenu);

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

    private static final Runnable dispenseMenu = () -> {
        String[] options = {
            "1. View Prescriptions And Dispense",
            "2. Display Today's Prescriptions",
            "3. Display Dispense Record",
            "0. Back"
        };

        Runnable[] actions = {
            () -> manager.viewAndDispense(),
            () -> manager.displayTodaysPrescriptions(),
            () -> manager.displayDispenseRecords(),
            () -> Navigation.goBack()
        };

        Utility.customMenu(options, "Dispense Medicine", "Choose an option: ", actions);
    };

    private static final Runnable stockMenu = () -> {
        String[] options = {
            "1. Show Medicine Stock",
            "2. Stock In Medicine",
            "3. Edit Medicine Stock",
            "4. Delete Medicine",
            "5. Check Low Stock",
            "0. Back"
        };

        Runnable[] actions = {
            () -> medicine.displayMedicineStock(),
            () -> stockInMedicineWithChoice(),
            () -> medicine.editMedicine(),
            () -> medicine.deleteMedicine(),
            () -> medicine.checkLowStock(),
            () -> Navigation.goBack()
        };

        Utility.customMenu(options, "Manage Medicine Stock", "Choose an option: ", actions);
    };

    private static final Runnable reportMenu = () -> {
        String[] options = {
            "1. Generate Daily Dispense Report",
            "2. Generate Low Stock Report",
            "0. Back"
        };

        Runnable[] actions = {
            () -> report.generateDailyDispenseReport(),
            () -> report.generateLowStockReport(50),
            () -> Navigation.goBack()
        };

        Utility.customMenu(options, "Generate Report", "Choose an option: ", actions);
    };

    private static final Runnable pharmacyMainMenu = () -> {
        String[] options = {
            "1. Dispense Medicine",
            "2. Manage Medicine Stock",
            "3. Generate Report",
            "0. Back"
        };

        Runnable[] actions = {
            () -> Navigation.stack.push(dispenseMenu),
            () -> Navigation.stack.push(stockMenu),
            () -> Navigation.stack.push(reportMenu),
            () -> MainUi.mainUI(),};

        Utility.customMenu(options, "Pharmacy Menu", "Choose an option: ", actions);
    };

    private static void stockInMedicineWithChoice() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nChoose code input method:");
        System.out.println("1. Auto-generate medicine code");
        System.out.println("2. Enter code manually");
        System.out.print("Your choice: ");

        int choice = 0;
        boolean valid = false;
        while (!valid) {
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
                if (choice == 1 || choice == 2) {
                    valid = true;
                } else {
                    System.out.print("Invalid choice. Please enter 1 or 2: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }

        String code;
        if (choice == 1) {
            code = medicine.generateAutoCode();
            System.out.println("Generated Code: " + code);
        } else {
            code = medicine.getNonEmptyInput("Enter Medicine Code: ", "Medicine Code cannot be empty!");
        }

        medicine.stockInMedicine(code);
    }

    public static void editMedicineMenu(Medicine medToEdit) {
        Scanner scanner = new Scanner(System.in);

        boolean editing = true;
        while (editing) {
            System.out.println("\nChoose field to edit:");
            System.out.println("1. Name");
            System.out.println("2. Quantity");
            System.out.println("3. Price");
            System.out.println("4. Description");
            System.out.println("5. Dosage");
            System.out.println("0. Done Editing");
            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.print("Enter New Name: ");
                    medToEdit.setMedicineName(scanner.nextLine());
                    break;
                case "2":
                    medToEdit.setQuantityInStock(medicine.getPositiveInteger("Enter New Quantity: ", "Quantity must be positive!"));
                    break;
                case "3":
                    medToEdit.setPricePerUnit(medicine.getPositiveDouble("Enter New Price: ", "Price must be positive!"));
                    break;
                case "4":
                    System.out.print("Enter New Description: ");
                    medToEdit.setDescription(scanner.nextLine());
                    break;
                case "5":
                    System.out.print("Enter New Dosage: ");
                    medToEdit.setDosage(scanner.nextLine());
                    break;
                case "0":
                    editing = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

}
