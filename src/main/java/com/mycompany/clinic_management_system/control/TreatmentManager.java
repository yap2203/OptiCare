package com.mycompany.clinic_management_system.control;

import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.adt.QueueInterface;
import com.mycompany.clinic_management_system.dao.MedicineDAO;
import com.mycompany.clinic_management_system.dao.QueueDao;
import com.mycompany.clinic_management_system.dao.TreatmentDAO;
import com.mycompany.clinic_management_system.entity.Doctor;
import com.mycompany.clinic_management_system.entity.Patient;
import com.mycompany.clinic_management_system.entity.AppointmentQueue;
import com.mycompany.clinic_management_system.entity.Medicine;
import com.mycompany.clinic_management_system.entity.Treatment;
import com.mycompany.clinic_management_system.utility.Utility;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class TreatmentManager {

    private QueueDao queueDao;
    private QueueInterface<AppointmentQueue> queue;
    private TreatmentDAO treatmentDao;
    private ListInterface<Treatment> treatments;
    private Scanner scanner;
    private ListInterface<Medicine> medicineList;
    private MedicineDAO medicineDAO;
    private MedicineManager medicineManager;
    
    public TreatmentManager() {
        queueDao = new QueueDao();
        queue = queueDao.loadFromFile();
        treatmentDao = new TreatmentDAO();
        treatments = treatmentDao.loadTreatmentRecord();
        medicineDAO = new MedicineDAO();
        medicineList = medicineDAO.loadMedicineData();
        medicineManager = new MedicineManager();
        scanner = new Scanner(System.in);
    }

    public void modifyQueueStatusToDiagnosed(AppointmentQueue currentQueue) {
        currentQueue.setStatus("DIAGNOSED");
        queueDao.saveFile(queue);
    }

    public void displayTreatmentRecord() {
        System.out.println("\n========================================================================================================================================================== Treatment Record =========================================================================================================================================================");
        System.out.printf("| %-12s | %-12s | %-25s | %-12s | %-25s | %-12s | %-20s | %-30s | %-50s | %-16s | %-8s | %-30s | %-20s |\n",
                "TreatmentID", "PatientID", "Patient Name", "DoctorID", "Doctor Name", "Date", "Diagnosis", "Prescription", "Treatment Note", "Next Appointment", "Cost", "Medicine Codes", "Quantities");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (int i = 1; i <= treatments.getNumberOfEntries(); i++) {
            Treatment treatment = treatments.getEntry(i);

            String medicineCodesStr = String.join(",", treatment.getMedicineCodes());
            int quantities[] = treatment.getQuantities();
            StringBuilder quantitiesStrBuilder = new StringBuilder();
            for (int j = 0; j < quantities.length; j++) {
                quantitiesStrBuilder.append(quantities[j]);
                if (j < quantities.length - 1) {
                    quantitiesStrBuilder.append(",");
                }
            }
            String quantitiesStr = quantitiesStrBuilder.toString();

            System.out.printf("| %-12s | %-12s | %-25s | %-12s | %-25s | %-12s | %-20s | %-30s | %-50s | %-16s | %-8.2f | %-30s | %-20s |\n",
                    treatment.getTreatmentId(),
                    treatment.getPatient().getID(),
                    treatment.getPatient().getName(),
                    treatment.getDoctor().getDoctorID(),
                    treatment.getDoctor().getName(),
                    Utility.convertDateToString(treatment.getTreatmentDate()),
                    treatment.getDiagnosis(),
                    treatment.getPrescription(),
                    treatment.getTreatmentNotes(),
                    Utility.convertDateToString(treatment.getNextAppointment()),
                    treatment.getTreatmentCost(),
                    medicineCodesStr,
                    quantitiesStr
            );
        }
    }

    public void diagnose() {
        Iterator<AppointmentQueue> iterator = queue.getIterator();
        
        if (queue.isEmpty()) {
            System.out.println("No patients in the queue.");
            return;
        }

        boolean patientFound = false;

        while (iterator.hasNext()) {
            AppointmentQueue currentQueue = iterator.next();

            if(currentQueue.getStatus().equalsIgnoreCase("BOOKED")){
                patientFound = true;
                String treatmentId = generateUniqueTreatmentId();
                String diagnosis, prescription, treatmentNotes, nextAppointment;
                double treatmentCost = 0;
                String[] medicineCodes;
                int[] quantities;
                Patient patient = Utility.getPatientFromList(currentQueue.getPatientId());
                Doctor doctor = Utility.getDoctorById(currentQueue.getDoctorId());

                System.out.println("\n============================== Diagnosing Patient =============================================== \n"
                        + "Patient ID : " + currentQueue.getPatientId() + "\nPatient Name : " + patient.getName());

                System.out.print("Enter diagnosis: ");
                diagnosis = getNonEmptyInput("Diagnosis cannot be empty. Please enter diagnosis: ");

                System.out.print("Enter prescription: ");
                prescription = getNonEmptyInput("Prescription cannot be empty. Please enter prescription: ");

                System.out.print("Enter treatment notes: ");
                treatmentNotes = getNonEmptyInput("Treatment notes cannot be empty. Please enter notes: ");

                System.out.print("Enter next appointment date (e.g., dd-MM-yyyy): ");
                nextAppointment = getValidDate("Invalid date format. Please enter date as dd-MM-yyyy: ");

                System.out.print("Enter number of medicines prescribed: ");
                int numMedicines = getValidInt("Invalid input. Please enter a valid number of medicines: ");

                medicineCodes = new String[numMedicines];
                quantities = new int[numMedicines];

                for (int i = 0; i < numMedicines; i++) {
                    boolean isValid;
                    do{
                        isValid = true;
                        System.out.print("Enter medicine code #" + (i + 1) + ": ");
                        medicineCodes[i] = getNonEmptyInput("Medicine code cannot be empty. Please enter code: ");
                        Medicine med = medicineManager.findMedicineByCode(medicineCodes[i]);
                        if (med != null) {
                            System.out.print("Enter quantity for " + medicineCodes[i] + ": ");
                            quantities[i] = getValidInt("Invalid quantity. Please enter a positive integer: ");
                            if(quantities[i] > med.getQuantityInStock()){
                                System.out.println("The stock is not enough. Please try again...");
                                isValid = false;
                            }else{
                                treatmentCost += (double)(quantities[i] * med.getPricePerUnit());
                            }
                        }else{
                            System.out.println("Can't find the medicine code. Please try again...");
                            isValid = false;
                        }
                    }while(!isValid);
                }
                // Create new treatment
                Treatment newTreatment = new Treatment(
                        treatmentId,
                        patient,
                        doctor,
                        new Date(),
                        diagnosis,
                        prescription,
                        treatmentNotes,
                        Utility.convertStringToDate(nextAppointment),
                        treatmentCost,
                        medicineCodes,
                        quantities
                );

                // Add treatment
                treatments.add(newTreatment);
                treatmentDao.saveTreatmentRecord(treatments); // assuming this method saves to file

                // Update queue status
                modifyQueueStatusToDiagnosed(currentQueue);

                System.out.println("Diagnosis completed and treatment record added.");
                return;
            }
        }
        
        if (!patientFound) {
            System.out.println("\nNo patients with pending diagnosis in queue.");
        }
    }
   
    public void modify() {
        displayTreatmentRecord();
        System.out.print("Enter treatment ID to modify : ");
        String treatmentId = scanner.nextLine().trim();
        Treatment treatment = findTreatmentById(treatmentId);

        if (treatment == null) {
            System.out.println("Treatment ID not found.");
            return;
        }

        boolean done = false;
        while (!done) {
            System.out.println("\nModify Menu for Treatment ID: " + treatmentId);
            System.out.println("1. Diagnosis (" + treatment.getDiagnosis() + ")");
            System.out.println("2. Prescription (" + treatment.getPrescription() + ")");
            System.out.println("3. Treatment Notes (" + treatment.getTreatmentNotes() + ")");
            System.out.println("4. Next Appointment (" + Utility.convertDateToString(treatment.getNextAppointment()) + ")");
            System.out.println("5. Medicines");
            System.out.println("6. Save & Exit");
            System.out.print("Select an option:");
            int choice = getValidInt("Must be number please. Select an option: ");

            switch (choice) {
                case 1:
                    System.out.print("Enter new diagnosis (" + treatment.getDiagnosis() + ")");
                    treatment.setDiagnosis(getNonEmptyInput("Diagnosis cannot be empty. Please enter diagnosis: "));
                    break;
 
                case 2:
                    System.out.print("Enter new prescription (" + treatment.getPrescription() + "): ");
                    treatment.setPrescription(getNonEmptyInput("Prescription cannot be empty. Please enter prescription: "));
                    break;

                case 3:
                    System.out.print("Enter new treatment notes (" + treatment.getTreatmentNotes() + "): ");
                    treatment.setTreatmentNotes(getNonEmptyInput("Treatment notes cannot be empty. Please enter treatment notes: "));
                    break;

                case 4:
                    System.out.print("Enter new next appointment (" + Utility.convertDateToString(treatment.getNextAppointment()) + "): ");
                    Date newDate = Utility.convertStringToDate(
                        getValidDate("Invalid date format. Please enter next appointment in dd-MM-yyyy format: ")
                    );
                    treatment.setNextAppointment(newDate);
                    break;

                case 5:
                    System.out.print("Enter number of medicines: ");
                    int numMeds = getValidInt("Must be a number. Please enter number of medicines: ");
                    String[] medCodes = new String[numMeds];
                    int[] quantities = new int[numMeds];
                    double totalCost = 0.0;

                    for (int i = 0; i < numMeds; i++) {
                        boolean isValid;
                        do {
                            isValid = true;
                            System.out.print("Enter medicine code #" + (i + 1) + ": ");
                            medCodes[i] = getNonEmptyInput("Medicine code cannot be empty. Please enter medicine code: ");
                            Medicine med = medicineManager.findMedicineByCode(medCodes[i]);

                            if (med != null) {
                                System.out.print("Enter quantity for " + medCodes[i] + ": ");
                                quantities[i] = getValidInt("Must be a number. Please enter valid quantity: ");
                                if (quantities[i] > med.getQuantityInStock()) {
                                    System.out.println("Not enough stock. Please try again...");
                                    isValid = false;
                                } else {
                                    totalCost += quantities[i] * med.getPricePerUnit();
                                }
                            } else {
                                System.out.println("Medicine code not found. Please try again...");
                                isValid = false;
                            }
                        } while (!isValid);
                    }

                    treatment.setMedicineCodes(medCodes);
                    treatment.setQuantities(quantities);
                    treatment.setTreatmentCost(totalCost);
                    break;
                    
                case 6:
                    done = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        // Save updated record
        treatmentDao.saveTreatmentRecord(treatments);
        System.out.println("Treatment record updated successfully.");
    }


    public void delete() {
        displayTreatmentRecord();
        System.out.print("Enter treatment ID to delete: ");
        String treatmentId = scanner.nextLine();

        boolean found = false;

        for (int i = 1; i <= treatments.getNumberOfEntries(); i++) {
            Treatment treatment = treatments.getEntry(i);
            if (treatment.getTreatmentId().equalsIgnoreCase(treatmentId)) {
                found = true;

                // Confirm deletion
                System.out.print("Are you sure you want to delete this treatment? (yes/no): ");
                String confirm = scanner.nextLine();
                if (confirm.trim().equalsIgnoreCase("yes") || confirm.trim().equalsIgnoreCase("y")) {
                    treatments.remove(i);
                    treatmentDao.saveTreatmentRecord(treatments);
                    System.out.println("Treatment record deleted successfully.");
                } else {
                    System.out.println("Deletion cancelled.");
                }
                break;
            }
        }

        if (!found) {
            System.out.println("Treatment ID not found.");
        }
    }

    public String generateUniqueTreatmentId() {
        Random random = new Random();
        String treatmentId;
        boolean unique;

        do {
            StringBuilder idBuilder = new StringBuilder("TR");
            for (int i = 0; i < 3; i++) {
                int randomNumber = random.nextInt(10); // 0–9
                idBuilder.append(randomNumber);
            }

            treatmentId = idBuilder.toString();
            unique = true; // assume it's unique first

            // Check against all existing treatment IDs
            for (int i = 1; i <= treatments.getNumberOfEntries(); i++) {
                String existingId = treatments.getEntry(i).getTreatmentId();
                if (existingId.equals(treatmentId)) {
                    unique = false; // found duplicate, retry
                    break;
                }
            }

        } while (!unique);

        return treatmentId;
    }

    public Treatment findTreatmentById(String id) {
        Treatment treatment = null;
        for (int i = 1; i <= treatments.getNumberOfEntries(); i++) {
            if (treatments.getEntry(i).getTreatmentId().equals(id)) {
                treatment = treatments.getEntry(i);
            }
        }
        return treatment;
    }
    
    private String getNonEmptyInput(String errorMessage) {
        String input;
        do {
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.print(errorMessage);
            }
        } while (input.isEmpty());
        return input;
    }

    private double getValidDouble(String errorMessage) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(errorMessage);
            }
        }
    }

    private int getValidInt(String errorMessage) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= 0) {
                    return value;
                } else {
                    System.out.print("Value must be non-negative. Try again: ");
                }
            } catch (NumberFormatException e) {
                System.out.print(errorMessage);
            }
        }
    }

    private String getValidDate(String errorMessage) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                // check format before parsing
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                df.setLenient(false); // strict parsing
                df.parse(input);
                return input;
            } catch (ParseException e) {
                System.out.print(errorMessage);
            }
        }
    }
}
