/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.control;

import com.mycompany.clinic_management_system.adt.*;
import com.mycompany.clinic_management_system.dao.*;
import com.mycompany.clinic_management_system.entity.*;
import com.mycompany.clinic_management_system.utility.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author ASUS
 */
public class ReportManager {

    private final DispenseRecordDAO dispenseDao;
    private final MedicineDAO medicineDao;
    private final DoctorDao doctorDao;
    private final BookingDao bookingDao;
    private final DoctorScheduleDao doctorScheduleDao;
    private final PatientDao patientDao;
    private final TreatmentDAO treatmentDAO;

    public ReportManager() {
        this.dispenseDao = new DispenseRecordDAO();
        this.medicineDao = new MedicineDAO();
        this.doctorDao = new DoctorDao();
        this.bookingDao = new BookingDao();
        this.doctorScheduleDao = new DoctorScheduleDao();
        this.patientDao = new PatientDao();
        this.treatmentDAO = new TreatmentDAO();
    }

    // Helper: Draw a line
    private void printLine(int length) {
        System.out.println("=".repeat(length));
    }

    // Helper: Center text in a fixed width
    private String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }

    // Helper: Check If Doctor is booked on a specific day
    private static boolean isBooked(Doctor doctor, String day, ListInterface<Booking> bookings) {
        for (int i = 1; i <= bookings.getNumberOfEntries(); i++) {
            Booking booking = bookings.getEntry(i);

            if (booking.getDoctorId().equals(doctor.getDoctorID())
                    && booking.getDay().equalsIgnoreCase(day)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generate and display daily dispense report for a user-input date.
     */
    public void generateDailyDispenseReport() {
        ListInterface<DispenseRecord> records = dispenseDao.loadDispenseData();
        if (records.isEmpty()) {
            System.out.println("\nNo dispense records found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("\n\nEnter date for dispense record(yyyy-MM-dd): ");
        String inputDate = scanner.nextLine().trim();

        // Validate date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(inputDate);  // check if valid
        } catch (ParseException e) {
            System.out.println("❌ Invalid date format. Please use yyyy-MM-dd.");
            return;
        }
        System.out.println("\n\n");
        printLine(90);
        System.out.println(centerText("DAILY DISPENSE REPORT", 90));
        System.out.println(centerText("Date: " + inputDate, 90));
        printLine(90);

        String header = String.format("%-12s %-20s %-10s %-6s %-12s %-20s",
                "Patient ID", "Medicine Name", "Code", "Qty", "Doctor ID", "Date & Time");
        System.out.println(header);
        printLine(90);

        boolean found = false;

        for (int i = 1; i <= records.getNumberOfEntries(); i++) {
            DispenseRecord record = records.getEntry(i);
            String recordDate = record.getDispenseDateTime().split(" ")[0];
            if (inputDate.equals(recordDate)) {
                found = true;
                System.out.printf("%-12s %-20s %-10s %-6d %-12s %-20s%n",
                        record.getPatientId(),
                        record.getMedicineName(),
                        record.getMedicineCode(),
                        record.getQuantity(),
                        record.getDoctorId(),
                        record.getDispenseDateTime()
                );
            }
        }

        if (!found) {
            System.out.println(centerText("No dispense records found for " + inputDate + ".", 90));
        }
        printLine(90);
        System.out.println();
    }

    /**
     * Generate and display low stock medicine report.
     */
    public void generateLowStockReport(int threshold) {
        ListInterface<Medicine> medicines = medicineDao.loadMedicineData();
        if (medicines.isEmpty()) {
            System.out.println("\nNo medicine records found.");
            return;
        }

        printLine(85);
        System.out.println(centerText("LOW STOCK REPORT", 85));
        System.out.println(centerText("Threshold: " + threshold + " units", 85));
        printLine(85);

        String header = String.format("%-8s %-20s %-6s %-25s %-10s",
                "Code", "Medicine Name", "Qty", "Dosage", "Price (RM)");
        System.out.println(header);
        printLine(85);

        boolean found = false;

        for (int i = 1; i <= medicines.getNumberOfEntries(); i++) {
            Medicine med = medicines.getEntry(i);
            if (med.getQuantityInStock() <= threshold) {
                found = true;
                System.out.printf("%-8s %-20s %-6d %-25s %-10.2f%n",
                        med.getMedicineCode(),
                        med.getMedicineName(),
                        med.getQuantityInStock(),
                        med.getDosage(),
                        med.getPricePerUnit()
                );
            }
        }

        if (!found) {
            System.out.println(centerText("No medicines are below or equal to the threshold.", 85));
        }
        printLine(85);
        System.out.println();
    }

    /**
     * Generate and display doctor workload report.
     */
    public void generateDoctorWorkloadReport() {
        ListInterface<Doctor> doctors = doctorDao.loadDoctorsFromFile();
        ListInterface<Booking> bookings = bookingDao.loadFromFile();

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        printLine(70);
        System.out.println(centerText("DOCTOR WORKLOAD REPORT", 70));
        System.out.println(centerText("Date: " + today, 70));
        printLine(70);

        // Table header
        System.out.printf("%-10s %-25s %-10s%n", "Doctor ID", "Name", "Patients");
        printLine(70);

        for (int i = 1; i <= doctors.getNumberOfEntries(); i++) {
            Doctor doctor = doctors.getEntry(i);
            int count = 0;

            // Count bookings for this doctor
            for (int j = 1; j <= bookings.getNumberOfEntries(); j++) {
                Booking booking = bookings.getEntry(j);
                if (booking.getDoctorId().equals(doctor.getDoctorID())) {
                    count++;
                }
            }

            System.out.printf("%-10s %-25s %-10d%n",
                    doctor.getDoctorID(), doctor.getName(), count);
        }

        printLine(70);
        System.out.println(centerText("End of Report", 70));
        System.out.println();
    }

    /**
     * Generate and display Doctor Availability report.
     */
    public void generateDoctorAvailabilityReport() {
        ListInterface<Doctor> doctors = doctorDao.loadDoctorsFromFile();
        ListInterface<Booking> bookings = bookingDao.loadFromFile();
        ListInterface<String> daysOfWeek = Utility.getDaysOfWeek(); // <-- now return ListInterface<String>

        if (doctors.isEmpty()) {
            System.out.println("\nNo doctors found.");
            return;
        }

        // --- Calculate width dynamically based on number of days ---
        int tableWidth = 30 + (daysOfWeek.getNumberOfEntries() * 10) + 15;

        printLine(tableWidth);
        System.out.println(centerText("DOCTOR AVAILABILITY REPORT", tableWidth));
        printLine(tableWidth);

        // --- Header ---
        System.out.printf("%-10s %-20s", "DoctorID", "Name");
        for (int i = 1; i <= daysOfWeek.getNumberOfEntries(); i++) {
            String day = daysOfWeek.getEntry(i);
            System.out.printf(" %-10s", day.substring(0, 3)); // Mon, Tue...
        }
        System.out.printf(" %-15s%n", "Total");
        printLine(tableWidth);

        // --- Rows ---
        for (int d = 1; d <= doctors.getNumberOfEntries(); d++) {
            Doctor doctor = doctors.getEntry(d);
            int dutyCount = 0;

            System.out.printf("%-10s %-20s", doctor.getDoctorID(), doctor.getName());

            for (int i = 1; i <= daysOfWeek.getNumberOfEntries(); i++) {
                String day = daysOfWeek.getEntry(i);
                String status = isBooked(doctor, day, bookings) ? "Duty" : "Free";
                if (status.equals("Duty")) {
                    dutyCount++;
                }
                System.out.printf(" %-10s", status);
            }

            System.out.printf(" %-15d%n", dutyCount);
        }

        printLine(tableWidth);
        System.out.println("Legend: Free = Not Booked, Duty = Has Patient Appointment");
        printLine(tableWidth);
    }

    //patient module report
    public void generatePatietnDemographicReport() {
        ListInterface<Patient> patientList = patientDao.loadFromFile();
        int[][] demographic = new int[5][2];
        int totalMale = 0, totalFemale = 0;

        for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
            Patient patient = patientList.getEntry(i);
            int ageGroup = getAgeGroup(patient.getAge());
            int genderGroup = patient.getGender().equalsIgnoreCase("m") ? 1 : 0;
            demographic[ageGroup][genderGroup]++;
        }
        String[] ageGroups = {"0 - 12", "13 - 18", "19 - 35", "36 - 60", "60+"};
        printLine(65);

        System.out.println(centerText("Patient Demographic Report", 65));
        printLine(65);

        System.out.printf("%-15s %-15s %-15s %-20s\n", "Age Group", "Male", "Female", "Total");
        printLine(65);

        for (int i = 0; i < ageGroups.length; i++) {
            int male = demographic[i][0];
            int female = demographic[i][1];
            totalMale += male;
            totalFemale += female;
            int total = male + female;
            System.out.printf("%-15s %-15d %-15d %-20d\n", ageGroups[i], male, female, total);
        }
        printLine(65);
        System.out.printf("%-15s %-15d %-15d %-20d\n", "Grand Total", totalMale, totalFemale, totalMale + totalFemale);
    }

    public int getAgeGroup(int age) {
        if (age <= 12) {
            return 0;
        } else if (age <= 18) {
            return 1;
        } else if (age <= 35) {
            return 2;
        } else if (age <= 60) {
            return 3;
        } else {
            return 4;
        }
    }

    public void generatePatientFrequentVisitReport() {
        ListInterface<Booking> bookingList = bookingDao.loadFromFile();
        ListInterface<Patient> patientList = patientDao.loadFromFile();
        Map<String, Integer> visitCount = new HashMap<>();

        for (int i = 1; i <= bookingList.getNumberOfEntries(); i++) {
            Booking booking = bookingList.getEntry(i);
            if (booking.getStatus().equalsIgnoreCase("cancelled")) {
                continue;
            }
            String patietnId = booking.getPatientId();
            visitCount.put(patietnId, visitCount.getOrDefault(patietnId, 0) + 1);
        }

        printLine(50);

        System.out.println(centerText("Frequent Visit Report", 50));
        printLine(50);

        System.out.printf("%-15s %-20s %-6s\n", "Patient ID", "Name", "Visits");
        printLine(50);

        String topPatientId = null;
        int maxVisit = 0;

        for (Map.Entry<String, Integer> entry : visitCount.entrySet()) {
            String patientId = entry.getKey();
            int count = entry.getValue();

            String name = "unknown";
            for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
                Patient p = patientList.getEntry(i);
                if (p.getID().equals(patientId)) {
                    name = p.getName();
                    break;
                }
            }

            System.out.printf("%-15s %-20s %-6d\n", patientId, name, count);

            if (count > maxVisit) {
                maxVisit = count;
                topPatientId = patientId;
            }

        }
        printLine(50);
        if (topPatientId != null) {
            System.out.println("Top Frequent Visitor: " + topPatientId + " (" + maxVisit + " visits)");
        }
    }

    public void generateAverageTreatmentCostReport() {
        ListInterface<Treatment> treatments = treatmentDAO.loadTreatmentRecord();

        if (treatments.isEmpty()) {
            System.out.println("No treatments found.");
            return;
        }

        // Maps for summing cost and counting treatments
        Map<String, Double> doctorTotalCost = new HashMap<>();
        Map<String, Integer> doctorTreatmentCount = new HashMap<>();
        Map<String, String> doctorNames = new HashMap<>();

        for (int i = 1; i <= treatments.getNumberOfEntries(); i++) {
            Treatment t = treatments.getEntry(i);
            String doctorId = t.getDoctor().getDoctorID();
            String doctorName = t.getDoctor().getName();
            double cost = t.getTreatmentCost();

            doctorTotalCost.put(doctorId, doctorTotalCost.getOrDefault(doctorId, 0.0) + cost);
            doctorTreatmentCount.put(doctorId, doctorTreatmentCount.getOrDefault(doctorId, 0) + 1);
            doctorNames.put(doctorId, doctorName);
        }

        // Find doctor with highest average cost
        String premiumDoctorId = null;
        double highestAvgCost = 0.0;

        for (String docId : doctorTotalCost.keySet()) {
            double avgCost = doctorTotalCost.get(docId) / doctorTreatmentCount.get(docId);
            if (avgCost > highestAvgCost) {
                highestAvgCost = avgCost;
                premiumDoctorId = docId;
            }
        }

        // Print report
        System.out.println("\n========================================");
        System.out.println("     Average Treatment Cost Report");
        System.out.println("========================================");
        System.out.printf("%-10s %-20s %-20s %-15s %-20s%n",
                "DoctorID", "Doctor Name", "Total Cost (RM)", "Treatments", "Avg Cost (RM)");
        System.out.println("-------------------------------------------------------------------------------");

        for (String docId : doctorTotalCost.keySet()) {
            double totalCost = doctorTotalCost.get(docId);
            int treatmentCount = doctorTreatmentCount.get(docId);
            double avgCost = totalCost / treatmentCount;

            System.out.printf("%-10s %-20s RM %-17.2f %-15d RM %-15.2f%n",
                    docId, doctorNames.get(docId), totalCost, treatmentCount, avgCost);
        }

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Premium Doctor: " + doctorNames.get(premiumDoctorId)
                + " (Avg Cost: RM " + String.format("%.2f", highestAvgCost) + ")");
    }

    public void generateDiagnosisFrequencyReport() {
        ListInterface<Treatment> treatments = treatmentDAO.loadTreatmentRecord();

        if (treatments.isEmpty()) {
            System.out.println("No treatments found.");
            return;
        }

        // Count diagnosis occurrences
        Map<String, Integer> diagnosisCount = new HashMap<>();

        for (int i = 1; i <= treatments.getNumberOfEntries(); i++) {
            Treatment t = treatments.getEntry(i);
            String diagnosis = t.getDiagnosis();

            diagnosisCount.put(diagnosis, diagnosisCount.getOrDefault(diagnosis, 0) + 1);
        }

        // Find most common diagnosis
        String mostCommonDiagnosis = null;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : diagnosisCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostCommonDiagnosis = entry.getKey();
            }
        }

        // Print report
        System.out.println("\n========================================");
        System.out.println("       Diagnosis Frequency Report");
        System.out.println("========================================");
        System.out.printf("%-30s %-15s%n", "Diagnosis", "Occurrences");
        System.out.println("------------------------------------------------");

        for (Map.Entry<String, Integer> entry : diagnosisCount.entrySet()) {
            System.out.printf("%-30s %-15d%n", entry.getKey(), entry.getValue());
        }

        System.out.println("------------------------------------------------");
        System.out.println("Most Common Diagnosis: " + mostCommonDiagnosis
                + " (" + maxCount + " cases)");
    }

    public void generateDailyPaymentReport() {
        PaymentDAO paymentDao = new PaymentDAO();
        ListInterface<Payment> payments = paymentDao.loadPaymentRecord();

        if (payments.isEmpty()) {
            System.out.println("\nNo payment records found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter date for payment report (dd-MM-yyyy): ");
        String inputDate = scanner.nextLine().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        try {
            Date parsedDate = new SimpleDateFormat("d-M-yyyy").parse(inputDate); // accepts 1 or 2 digits
            inputDate = new SimpleDateFormat("dd-MM-yyyy").format(parsedDate);   // reformat to dd-MM-yyyy
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use dd-MM-yyyy.");
            return;
        }


        printLine(90);
        System.out.println(centerText("DAILY PAYMENT REPORT", 90));
        System.out.println(centerText("Date: " + inputDate, 90));
        printLine(90);

        String header = String.format("%-12s %-12s %-12s %-10s %-12s %-12s",
                "Payment ID", "Patient ID", "Treatment ID", "Amount", "Method", "Status");
        System.out.println(header);
        printLine(90);

        boolean found = false;
        double totalCollected = 0.0;

        for (int i = 1; i <= payments.getNumberOfEntries(); i++) {
            Payment p = payments.getEntry(i);
            String payDate = new SimpleDateFormat("dd-MM-yyyy").format(p.getPaymentDate());

            if (payDate.equals(inputDate)) {
                found = true;
                System.out.printf("%-12s %-12s %-12s RM %-8.2f %-12s %-12s%n",
                        p.getPaymentId(),
                        p.getPatient().getID(),
                        p.getTreatment().getTreatmentId(),
                        p.getAmountPaid(),
                        p.getPaymentMethod(),
                        p.getPaymentStatus()
                );

                if (p.getPaymentStatus().equalsIgnoreCase(Payment.PAYMENT_PAID)) {
                    totalCollected += p.getAmountPaid();
                }
            }
        }

        if (!found) {
            System.out.println(centerText("No payments found for " + inputDate, 90));
        }

        printLine(90);
        System.out.println("Total Collected (PAID): RM " + String.format("%.2f", totalCollected));
        printLine(90);
    }

}
