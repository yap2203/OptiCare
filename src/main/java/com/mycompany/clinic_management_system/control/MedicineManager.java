/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.control;

import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.boudary.PharmacyUi;
import com.mycompany.clinic_management_system.dao.MedicineDAO;
import com.mycompany.clinic_management_system.entity.Medicine;
import java.util.*;

/**
 *
 * @author ASUS
 */
public class MedicineManager {

    private ListInterface<Medicine> medicineList;
    private MedicineDAO medicineDAO;
    private Scanner scanner = new Scanner(System.in);

    public MedicineManager() {
        medicineDAO = new MedicineDAO();
        this.medicineList = medicineDAO.loadMedicineData();
    }

    public void saveMedicineData() {
        medicineDAO.saveMedicineData(medicineList);
    }

    public void refresh() {
        this.medicineList = medicineDAO.loadMedicineData();
    }

    public void displayMedicineStock() {
        refresh();
        System.out.println("\n============================================ Medicine Stock ====================================================");
        System.out.printf("| %-8s | %-20s | %-8s | %-8s | %-30s | %-19s |\n",
                "Code", "Name", "Qty", "Price", "Description", "Dosage");
        System.out.println("----------------------------------------------------------------------------------------------------------------");

        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            System.out.printf("| %-8s | %-20s | %-8d | %-8.2f | %-30s | %-19s |\n",
                    med.getMedicineCode(), med.getMedicineName(),
                    med.getQuantityInStock(), med.getPricePerUnit(),
                    med.getDescription(), med.getDosage());
        }
        System.out.println("================================================================================================================\n");
    }

    public void stockInMedicine(String code) {
        refresh();
        System.out.println("\n============= Add New Medicine =============");

        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            if (medicineList.getEntry(i).getMedicineCode().equalsIgnoreCase(code)) {
                System.out.println("Medicine code already exists.");
                return;
            }
        }

        String name = getNonEmptyInput("Enter Medicine Name: ", "Medicine Name cannot be empty!");
        int quantity = getPositiveInteger("Enter Quantity: ", "Quantity must be positive!");
        double price = getPositiveDouble("Enter Price Per Unit: ", "Price must be positive!");

        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Dosage (e.g. 1 tab x 3/day): ");
        String dosage = scanner.nextLine();

        Medicine newMed = new Medicine(code, name, description, dosage, quantity, price);
        medicineList.add(newMed);
        medicineDAO.saveMedicineData(medicineList);
        System.out.println("Medicine added successfully!\n");
    }

    public void editMedicine() {
        refresh();
        System.out.println("\n========= Edit Medicine =========");
        System.out.print("Enter Medicine Code to Edit: ");
        String code = scanner.nextLine();

        Medicine medToEdit = findMedicineByCode(code);

        if (medToEdit == null) {
            System.out.println("Medicine not found.\n");
            return;
        }

        System.out.println("\n--- Current Medicine Record ---");
        System.out.printf("%-12s: %s\n", "Code", medToEdit.getMedicineCode());
        System.out.printf("%-12s: %s\n", "Name", medToEdit.getMedicineName());
        System.out.printf("%-12s: %d\n", "Quantity", medToEdit.getQuantityInStock());
        System.out.printf("%-12s: %.2f\n", "Price", medToEdit.getPricePerUnit());
        System.out.printf("%-12s: %s\n", "Description", medToEdit.getDescription());
        System.out.printf("%-12s: %s\n", "Dosage", medToEdit.getDosage());
        System.out.println("-------------------------------\n");

        PharmacyUi.editMedicineMenu(medToEdit);
        medicineDAO.saveMedicineData(medicineList);
        System.out.println("Medicine updated successfully!\n");
    }

    public void deleteMedicine() {
        refresh();
        System.out.println("\n======== Delete Medicine ========");
        System.out.print("Enter Medicine Code to Delete: ");
        String code = scanner.nextLine().trim();

        boolean found = false;

        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            if (med.getMedicineCode().equalsIgnoreCase(code)) {
                found = true;

                // Show the medicine record before confirming
                System.out.println("\nFound at Row #" + i);
                System.out.println("Medicine Code : " + med.getMedicineCode());
                System.out.println("Name          : " + med.getMedicineName());
                System.out.println("Quantity      : " + med.getQuantityInStock());
                System.out.println("Price         : RM" + String.format("%.2f", med.getPricePerUnit()));
                System.out.println("Description   : " + med.getDescription());
                System.out.println("Dosage        : " + med.getDosage());

                // Confirm before deleting
                System.out.print("\nAre you sure you want to delete this medicine? (Y/N): ");
                String confirm = scanner.nextLine().trim().toUpperCase();

                if (confirm.equals("Y")) {
                    medicineList.remove(i);
                    medicineDAO.saveMedicineData(medicineList);
                    System.out.println("Medicine deleted successfully!\n");
                } else {
                    System.out.println("Deletion cancelled.\n");
                }
                return;
            }
        }

        if (!found) {
            System.out.println("Medicine not found.\n");
        }
    }

    public void checkLowStock() {
        refresh();
        System.out.println("\n=== Low Stock Medicines (Qty < 5) ===");
        boolean hasLowStock = false;

        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            if (med.getQuantityInStock() < 5) {
                System.out.printf("Code: %s | Name: %s | Qty: %d\n",
                        med.getMedicineCode(), med.getMedicineName(), med.getQuantityInStock());
                hasLowStock = true;
            }
        }

        if (!hasLowStock) {
            System.out.println("No medicine with low stock.\n");
        }
    }

    public Medicine findMedicineByCode(String code) {
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine med = medicineList.getEntry(i);
            if (med.getMedicineCode().equalsIgnoreCase(code)) {
                return med;
            }
        }
        return null;
    }

    public String getNonEmptyInput(String prompt, String errorMessage) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(errorMessage);
            }
        } while (input.isEmpty());
        return input;
    }

    public int getPositiveInteger(String prompt, String errorMessage) {
        int number = 0;
        boolean isValid;
        do {
            isValid = true;
            String input = getNonEmptyInput(prompt, "This field cannot be empty!");
            try {
                number = Integer.parseInt(input);
                if (number <= 0) {
                    System.out.println(errorMessage);
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
                isValid = false;
            }
        } while (!isValid);
        return number;
    }

    public double getPositiveDouble(String prompt, String errorMessage) {
        double number = 0;
        boolean isValid;
        do {
            isValid = true;
            String input = getNonEmptyInput(prompt, "This field cannot be empty!");
            try {
                number = Double.parseDouble(input);
                if (number <= 0) {
                    System.out.println(errorMessage);
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number!");
                isValid = false;
            }
        } while (!isValid);
        return number;
    }

    public String generateAutoCode() {
        refresh();
        return "MED" + String.format("%03d", medicineList.getNumberOfEntries() + 1);
    }

}
