package com.mycompany.clinic_management_system.control;

import com.mycompany.clinic_management_system.adt.LinkedQueue;
import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.adt.QueueInterface;
import com.mycompany.clinic_management_system.dao.BookingDao;
import com.mycompany.clinic_management_system.dao.PaymentDAO;
import com.mycompany.clinic_management_system.dao.QueueDao;
import com.mycompany.clinic_management_system.dao.TreatmentDAO;
import com.mycompany.clinic_management_system.entity.AppointmentQueue;
import com.mycompany.clinic_management_system.entity.Booking;
import com.mycompany.clinic_management_system.entity.Patient;
import com.mycompany.clinic_management_system.entity.Payment;
import com.mycompany.clinic_management_system.entity.Treatment;
import com.mycompany.clinic_management_system.utility.Utility;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class PaymentManager {

    private QueueDao queueDao;
    private QueueInterface<AppointmentQueue> queue;
    private PaymentDAO paymentDao;
    private ListInterface<Payment> payments;
    private TreatmentDAO treatmentDao;
    private ListInterface<Treatment> treatments;
    private BookingDao bookingDao;
    private ListInterface<Booking> bookings;
    private Scanner scanner;

    public PaymentManager() {
        queueDao = new QueueDao();
        queue = queueDao.loadFromFile();
        paymentDao = new PaymentDAO();
        payments = paymentDao.loadPaymentRecord();
        treatmentDao = new TreatmentDAO();
        treatments = treatmentDao.loadTreatmentRecord();
        bookingDao = new BookingDao();
        bookings = bookingDao.loadFromFile();
        scanner = new Scanner(System.in);
    }

    public void displayPaymentRecord() {
        System.out.println("\n========================================================================= Payment Records =========================================================================");
        System.out.printf("| %-10s | %-10s | %-12s | %-20s | %-10s | %-12s | %-12s | %-12s |\n",
                "PaymentID", "TreatID", "PatientID", "Patient Name", "Amount", "Method", "Status", "Date");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (int i = 1; i <= payments.getNumberOfEntries(); i++) {
            Payment payment = payments.getEntry(i);

            System.out.printf("| %-10s | %-10s | %-12s | %-20s | %-10.2f | %-12s | %-12s | %-12s |\n",
                    payment.getPaymentId(),
                    payment.getTreatment().getTreatmentId(),
                    payment.getPatient().getID(),
                    payment.getPatient().getName(),
                    payment.getAmountPaid(),
                    payment.getPaymentMethod(),
                    payment.getPaymentStatus(),
                    Utility.convertDateToString(payment.getPaymentDate())
            );
        }
    }
    
    public void addPayment(AppointmentQueue queue, Treatment treatment){
        String paymentId = generateUniquePaymentId();
        Patient patient = Utility.getPatientFromList(queue.getPatientId());
        double amountPaid = treatment.getTreatmentCost() + Payment.DIANOGSIS_FEE;
        Payment newPayment = new Payment(paymentId, treatment, patient, amountPaid, "-", Payment.PAYMENT_PENDING, new Date());
        payments.add(newPayment);
        paymentDao.savePaymentRecord(payments);
    }
    
    public void pay(Patient patient) {
        if (queue.isEmpty()) {
            System.out.println("No patients in the queue.");
            return;
        }

        Iterator<AppointmentQueue> iterator = queue.getIterator();
        boolean patientFound = false;

        while (iterator.hasNext()) {
            AppointmentQueue currentQueue = iterator.next();

            // Only allow if the queue is PAYMENT and the patient matches
            if (currentQueue.getStatus().equalsIgnoreCase("PAYMENT") &&
                currentQueue.getPatientId().equals(patient.getID())) {

                patientFound = true;

                System.out.println("\n============================== Payment Processing ===============================================");
                System.out.println("Patient ID   : " + patient.getID());
                System.out.println("Patient Name : " + patient.getName());

                // Display all unpaid payments for this patient
                System.out.println("\nYour Pending Payments:");
                System.out.printf("| %-10s | %-10s | %-20s | %-10s | %-15s |\n",
                        "PaymentID", "TreatID", "Diagnosis", "Cost", "Diagnosis fee");
                System.out.println("-----------------------------------------------------------------------");

                for (int i = 1; i <= payments.getNumberOfEntries(); i++) {
                    Payment p = payments.getEntry(i);
                    if (p.getPatient().getID().equals(patient.getID()) &&
                        p.getPaymentStatus().equals(Payment.PAYMENT_PENDING)) {
                        System.out.printf("| %-10s | %-10s | %-20s | %-10.2f | %-15.2f |\n",
                                p.getPaymentId(),
                                p.getTreatment().getTreatmentId(),
                                p.getTreatment().getDiagnosis(),
                                p.getTreatment().getTreatmentCost(),
                                Payment.DIANOGSIS_FEE);
                    }
                }

                // Ask patient which PaymentID to pay
                System.out.print("\nEnter PaymentID you want to pay: ");
                String chosenId = scanner.nextLine().trim();

                Payment chosenPayment = null;
                for (int i = 1; i <= payments.getNumberOfEntries(); i++) {
                    Payment p = payments.getEntry(i);
                    if (p.getPaymentId().equalsIgnoreCase(chosenId) &&
                        p.getPatient().getID().equals(patient.getID()) &&
                        p.getPaymentStatus().equals(Payment.PAYMENT_PENDING)) {
                        chosenPayment = p;
                        break;
                    }
                }

                if (chosenPayment == null) {
                    System.out.println("Invalid PaymentID. You can only pay your own pending payments.");
                    return;
                }

                // Show payment details
                System.out.println("\nPayment Selected:");
                System.out.printf("PaymentID: %s\nTreatmentID: %s\nDiagnosis: %s\nAmount: RM %.2f\n",
                        chosenPayment.getPaymentId(),
                        chosenPayment.getTreatment().getTreatmentId(),
                        chosenPayment.getTreatment().getDiagnosis(),
                        chosenPayment.getAmountPaid());

                // Ask for confirmation
                System.out.print("Proceed with payment? (yes/no): ");
                String confirm = scanner.nextLine().trim();

                if (confirm.equalsIgnoreCase("yes")) {
                    // Ask payment method
                    System.out.print("Enter payment method (Cash/Card): ");
                    String method = scanner.nextLine().trim();

                    // Update payment record
                    chosenPayment.setPaymentMethod(method);
                    chosenPayment.setPaymentStatus(Payment.PAYMENT_PAID);
                    chosenPayment.setPaymentDate(new Date());

                    paymentDao.savePaymentRecord(payments);

                    System.out.println("Payment successful! Receipt generated.");
                    
                    // Dequeue this patient since payment is done
                    boolean removeQueue = removeByQueueId(currentQueue.getQueueId());
                    if(removeQueue){
                        System.out.println("Queue ID " + currentQueue.getQueueId() + " is removed from the queue");
                        queueDao.saveFile(queue);
                        //use for loop to find booking id that is equal to currentQueue.getBookingId()
                        for (int i = 1; i <= bookings.getNumberOfEntries(); i++) {
                            Booking booking = bookings.getEntry(i);
                            if (booking.getBookingId().equals(currentQueue.getBookingId())) {
                                booking.setStatus("PAID");
                                System.out.println("Booking ID " + booking.getBookingId() + " status updated to PAID.");
                                break;
                            }
                        }
                        bookingDao.saveFile(bookings);
                    }else{
                        System.out.println("Queue ID " + currentQueue.getQueueId() + " fails to remove");
                    }
                } else {
                    System.out.println("Payment cancelled.");
                }
                return;
            }
        }

        if (!patientFound) {
            System.out.println("You have no pending payments at the moment.");
        }
        return;
    }
    
    private String generateUniquePaymentId() {
        Random random = new Random();
        String paymentId;
        boolean unique;

        do {
            paymentId = "PAY" + (100 + random.nextInt(900)); // PAY100 - PAY999
            unique = true;

            for (int i = 1; i <= payments.getNumberOfEntries(); i++) {
                if (payments.getEntry(i).getPaymentId().equalsIgnoreCase(paymentId)) {
                    unique = false;
                    break;
                }
            }
        } while (!unique);

        return paymentId;
    }
    
    public boolean removeByQueueId(String queueId) {
        QueueInterface<AppointmentQueue> newQueue = new LinkedQueue<>();
        boolean removed = false;

        while (!this.queue.isEmpty()) {
            AppointmentQueue current = this.queue.dequeue(); // take from front
            if (current.getQueueId().equals(queueId)) {
                removed = true; // skip this one (effectively removed)
            } else {
                newQueue.enqueue(current); // keep others
            }
        }

        // move back all except the removed one
        while (!newQueue.isEmpty()) {
            this.queue.enqueue(newQueue.dequeue());
        }

        return removed;
    }
}
