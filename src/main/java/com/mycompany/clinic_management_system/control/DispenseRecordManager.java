/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.control;

import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.adt.QueueInterface;
import com.mycompany.clinic_management_system.dao.DispenseRecordDAO;
import com.mycompany.clinic_management_system.dao.QueueDao;
import com.mycompany.clinic_management_system.dao.TreatmentDAO;
import com.mycompany.clinic_management_system.entity.DispenseRecord;
import com.mycompany.clinic_management_system.entity.Medicine;
import com.mycompany.clinic_management_system.entity.AppointmentQueue;
import com.mycompany.clinic_management_system.entity.Treatment;
import com.mycompany.clinic_management_system.utility.Utility;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author ASUS
 */
public class DispenseRecordManager {

    private final Scanner scanner = new Scanner(System.in);
    private final DispenseRecordDAO recordDAO;
    private final TreatmentDAO treatmentDAO;
    private final QueueDao queueDao;
    private final MedicineManager medicineManager;
    private ListInterface<DispenseRecord> dispenseRecords;
    private ListInterface<Treatment> treatments;
    private PaymentManager paymentManager;

    public DispenseRecordManager() {
        recordDAO = new DispenseRecordDAO();
        treatmentDAO = new TreatmentDAO();
        queueDao = new QueueDao();
        medicineManager = new MedicineManager();
        this.dispenseRecords = recordDAO.loadDispenseData();
        this.treatments = treatmentDAO.loadTreatmentRecord();
        paymentManager = new PaymentManager();
    }

    public void displayDispenseRecords() {
        this.dispenseRecords = recordDAO.loadDispenseData();
        System.out.println("\n====================================== Dispense Records ======================================");
        System.out.printf("| %-8s | %-8s | %-30s | %-5s | %-16s | %-8s |\n",
                "Patient", "MedCode", "MedName", "Qty", "DateTime", "Doctor");
        System.out.println("----------------------------------------------------------------------------------------------");

        for (int i = 1; i <= dispenseRecords.getNumberOfEntries(); i++) {
            DispenseRecord record = dispenseRecords.getEntry(i);
            System.out.printf("| %-8s | %-8s | %-30s | %-5d | %-16s | %-8s |\n",
                    record.getPatientId(), record.getMedicineCode(),
                    record.getMedicineName(), record.getQuantity(),
                    record.getDispenseDateTime(), record.getDoctorId());
        }
        System.out.println("==============================================================================================\n");
    }

    public void displayTodaysPrescriptions() {
        this.treatments = treatmentDAO.loadTreatmentRecord();

        if (treatments.isEmpty()) {
            System.out.println("\nNo treatment records found.");
            return;
        }

        boolean found = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");  // Match your file format

        Date today = new Date();

        System.out.println("\n=============== Today's Prescriptions (" + today + ") ===============\n");

        for (int i = 1; i <= treatments.getNumberOfEntries(); i++) {
            Treatment treatment = treatments.getEntry(i);

            if (sdf.format(treatment.getTreatmentDate()).equals(sdf.format(today))) {
                found = true;
                displayTreatment(treatment);
            }
        }
        if (!found) {
            System.out.println("No prescriptions recorded for today.");
        }
        System.out.println("\n====================================================================================\n");
    }

    public void viewAndDispense() {
        QueueInterface<AppointmentQueue> queue = queueDao.loadFromFile();
        Iterator<AppointmentQueue> iterator = queue.getIterator();

        boolean patientFound = false;

        while (iterator.hasNext()) {
            AppointmentQueue q = iterator.next();

            if (q.getStatus().equalsIgnoreCase("DIAGNOSED")) {
                Treatment treatment = findTreatmentByPatientId(q.getPatientId());

                if (treatment != null) {
                    displayTreatment(treatment);

                    System.out.print("\nDispense medicine to this patient? (Y/N): ");
                    String input = scanner.nextLine().trim().toUpperCase();

                    if (input.equals("Y")) {
                        boolean success = dispenseMedicines(treatment);

                        if (success) {
                            q.setStatus("PAYMENT");
                            queueDao.saveFile(queue);
                            System.out.println("Status updated to PAYMENT.");
                            paymentManager.addPayment(q, treatment);
                        } else {
                            System.out.println("Dispensing failed due to stock issues.");
                        }
                    } else {
                        System.out.println("Returning to menu...");
                    }

                    patientFound = true;
                    break;
                } else {
                    System.out.println("No treatment found for Patient ID: " + q.getPatientId());
                }
            }
        }

        if (!patientFound) {
            System.out.println("\nNo patients with pending prescriptions in queue.");
        }
    }

    private Treatment findTreatmentByPatientId(String patientId) {
        this.treatments = treatmentDAO.loadTreatmentRecord();

        for (int i = 1; i <= treatments.getNumberOfEntries(); i++) {
            Treatment t = treatments.getEntry(i);
            if (t.getPatient() != null && t.getPatient().getID().equalsIgnoreCase(patientId)) {
                return t;
            }
        }
        return null;
    }

    private void displayTreatment(Treatment treatment) {
        System.out.println("\n====================== Prescription ======================");
        System.out.println("Treatment ID     : " + treatment.getTreatmentId());
        System.out.println("Patient ID       : " + treatment.getPatient().getID());
        System.out.println("Patient Name     : " + treatment.getPatient().getName());
        System.out.println("Doctor ID        : " + treatment.getDoctor().getDoctorID());
        System.out.println("Doctor Name      : " + treatment.getDoctor().getName());
        System.out.println("Date             : " + Utility.convertDateToString(treatment.getTreatmentDate()));
        System.out.println("Diagnosis        : " + treatment.getDiagnosis());
        System.out.println("Prescription     : " + treatment.getPrescription());
        System.out.println("Treatment Notes  : " + treatment.getTreatmentNotes());
        System.out.println("Next Appointment : " + Utility.convertDateToString(treatment.getNextAppointment()));
        System.out.println("Treatment Cost   : RM " + String.format("%.2f", treatment.getTreatmentCost()));

        String[] medCodes = treatment.getMedicineCodes();
        int[] medQtys = treatment.getQuantities();

        System.out.println("Medicines Prescribed:");
        for (int i = 0; i < medCodes.length; i++) {
            System.out.printf("   - %s (Qty: %d)%n", medCodes[i], medQtys[i]);
        }
        System.out.println("==========================================================");
    }

    private boolean dispenseMedicines(Treatment treatment) {
        medicineManager.refresh();
        String[] medCodes = treatment.getMedicineCodes();
        int[] medQtys = treatment.getQuantities();
        boolean allDispensed = true;

        for (int i = 0; i < medCodes.length; i++) {
            Medicine med = medicineManager.findMedicineByCode(medCodes[i]);
            if (med != null) {
                int available = med.getQuantityInStock();
                int required = medQtys[i];
                if (available < required) {
                    System.out.printf("Not enough stock for %s (Required: %d, Available: %d)\n",
                            medCodes[i], required, available);
                    allDispensed = false;
                } else {
                    med.setQuantityInStock(available - required);
                }
            } else {
                System.out.println("Medicine not found: " + medCodes[i]);
                allDispensed = false;
            }
        }

        if (allDispensed) {
            medicineManager.saveMedicineData();
            this.dispenseRecords = recordDAO.loadDispenseData();
            for (int i = 0; i < medCodes.length; i++) {
                Medicine med = medicineManager.findMedicineByCode(medCodes[i]);

                DispenseRecord record = new DispenseRecord(
                        treatment.getPatient().getID(),
                        medCodes[i],
                        med.getMedicineName(),
                        medQtys[i],
                        Utility.getCurrentDateTimeString(),
                        treatment.getDoctor().getDoctorID()
                );
                dispenseRecords.add(record);
            }
            recordDAO.saveDispenseRecords(dispenseRecords);
            System.out.println("Medicines dispensed successfully.");
            return true;
        }

        return false;
    }
}
