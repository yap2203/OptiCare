/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.dao;

import com.mycompany.clinic_management_system.adt.ArrayList;
import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.control.TreatmentManager;
import com.mycompany.clinic_management_system.entity.Payment;
import com.mycompany.clinic_management_system.entity.Patient;
import com.mycompany.clinic_management_system.entity.Treatment;
import com.mycompany.clinic_management_system.utility.Utility;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class PaymentDAO {

    private static final String PAYMENT_FILE = "data/paymentrecord.txt";
    private TreatmentManager treatmentManager = new TreatmentManager();
    public ListInterface<Payment> loadPaymentRecord() {
        ListInterface<Payment> paymentList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 8) {
                    String paymentId = parts[0].trim();
                    String treatmentId = parts[1].trim();
                    String patientId = parts[2].trim();
                    String patientName = parts[3].trim();
                    double amountPaid = Double.parseDouble(parts[4].trim());
                    String paymentMethod = parts[5].trim();
                    String paymentStatus = parts[6].trim();
                    Date paymentDate = Utility.convertStringToDate(parts[7].trim());

                    // reconstruct lightweight Treatment + Patient
                    Patient patient = Utility.getPatientFromList(patientId);
                    Treatment treatment = treatmentManager.findTreatmentById(treatmentId);

                    Payment payment = new Payment(paymentId, treatment, patient,
                            amountPaid, paymentMethod, paymentStatus, paymentDate);

                    paymentList.add(payment);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading payment file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number in payment file: " + e.getMessage());
        }

        return paymentList;
    }

    public void savePaymentRecord(ListInterface<Payment> paymentList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENT_FILE))) {
            for (int i = 1; i <= paymentList.getNumberOfEntries(); i++) {
                Payment payment = paymentList.getEntry(i);

                writer.write(String.format("%s|%s|%s|%s|%.2f|%s|%s|%s",
                        payment.getPaymentId(),
                        payment.getTreatment().getTreatmentId(),
                        payment.getPatient().getID(),
                        payment.getPatient().getName(),
                        payment.getAmountPaid(),
                        payment.getPaymentMethod(),
                        payment.getPaymentStatus(),
                        Utility.convertDateToString(payment.getPaymentDate())
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving payment file: " + e.getMessage());
        }
    }
}
