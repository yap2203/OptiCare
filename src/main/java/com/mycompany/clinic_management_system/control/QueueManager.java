/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.control;

import com.mycompany.clinic_management_system.adt.*;
import com.mycompany.clinic_management_system.dao.*;
import com.mycompany.clinic_management_system.entity.*;
import com.mycompany.clinic_management_system.boudary.*;
import com.mycompany.clinic_management_system.utility.*;
import java.time.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.time.temporal.TemporalAdjusters;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author huany
 */
public class QueueManager {

    Scanner scanner = new Scanner(System.in);
    private QueueInterface<AppointmentQueue> queue = new LinkedQueue();
    private final PatientUi patientUi = new PatientUi();
    private final BookingUi bookingUi = new BookingUi();
    private final QueueDao queueDao = new QueueDao();
    private final DoctorScheduleDao doctorSchedule = new DoctorScheduleDao();
    private final BookingDao bookingDao = new BookingDao();

    public void booking() {
        ListInterface<DoctorSchedule> scheduleList = doctorSchedule.loadSchedulesFromFile();
        ListInterface<Booking> bookingList = bookingDao.loadFromFile();
        DoctorScheduleManager doctorScheduleManager = new DoctorScheduleManager();
        QueueInterface<AppointmentQueue> queueFromfile = getQueue();
        ListInterface<DoctorSchedule> selectedDoctorSche;
        Doctor doctorSelected;
        String id;
        String ic;
        String day;
        String doctorId;
        Boolean isDoctorWork;
        Patient patient;
        String timeSlot;
        if (queueFromfile != null) {
            queue = queueFromfile;
        }

        do {
            id = patientUi.inputPatientId();
            patient = Utility.getPatientFromList(id);
            if (patient == null) {
                System.out.println("Patient not found!!Please process to register");
            }
        } while (patient == null);
        do {
            ic = patientUi.inputIC();
            if (!ic.equals(patient.getIc())) {
                System.out.println("please Enter the correct ic");
            }
        } while (!ic.equals(patient.getIc()));

        do {
            doctorScheduleManager.showSchedules();
            doctorId = bookingUi.inputDoctorId();
            doctorSelected = Utility.getDoctorById(doctorId);
            if (doctorSelected == null) {
                System.out.println("No doctor found.Please try again");

            }
            selectedDoctorSche = getScheduleByid(doctorId, scheduleList);
            if (selectedDoctorSche == null || selectedDoctorSche.getNumberOfEntries() == 0) {
                System.out.println("No schedule found for this doctor. Please choose another.");
                doctorSelected = null;

            }
        } while (doctorSelected == null);
        do {
            isDoctorWork = true;
            bookingUi.doctorSchedule(selectedDoctorSche, queue);
            DayOfWeek todayDayOfWeek = LocalDate.now().getDayOfWeek();
            String today = todayDayOfWeek.toString().substring(0, 1).toUpperCase()
                    + todayDayOfWeek.toString().substring(1).toLowerCase();

            System.out.println("Today is: " + today);
            System.out.print("Enter a day (Monday - Sunday): ");
            day = scanner.nextLine();

            DayOfWeek enteredDayOfWeek;
            try {
                enteredDayOfWeek = DayOfWeek.valueOf(day.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid day entered. Try again.");
                isDoctorWork = false;
                continue;
            }

//            if (enteredDayOfWeek.getValue() <= todayDayOfWeek.getValue()) {
//                System.out.println("You can only select a day after today.");
//                isDoctorWork = false;
//                continue;
//            }
            for (int i = 1; i <= selectedDoctorSche.getNumberOfEntries(); i++) {
                if (day.equalsIgnoreCase(selectedDoctorSche.getEntry(i).getDay())) {
                    isDoctorWork = true;
                    break;
                }
                isDoctorWork = false;
            }

            if (!isDoctorWork) {
                System.out.println("No slot available in selected day");

            }
        } while (!isDoctorWork);

        String date = setDate(day);
        Boolean isTimeSlotError = false;
        int num = 0;

        do {
            System.out.print("Select a time slot (E.g. 8.00,17.00): ");
            timeSlot = scanner.nextLine();

            if (!timeSlot.matches("^(8|9|1[0-7])\\.[0-5][0-9]$")) {
                System.out.println("Please enter a valid timeslot in HH.mm format (8.00 to 17.00).");
                isTimeSlotError = true;
                continue;
            }

            double timeValue = Double.parseDouble(timeSlot);
            if (timeValue < 8.00 || timeValue > 17.00) {
                System.out.println("Please enter a valid time between 8.00 and 17.00.");
                isTimeSlotError = true;
                continue;
            }
            int numberOfPatientInSlot = getNumberOfPatientInQueue(timeSlot, day, doctorId, queue);
            if (numberOfPatientInSlot > 3) {
                System.out.println("The time slot is full(max 4)");
                isTimeSlotError = true;
                continue;
            }
            num = ((int) (timeValue * 100)) + numberOfPatientInSlot + 1;
            isTimeSlotError = false;
        } while (isTimeSlotError);
        String queueId = "Q" + (int) (Math.random() * 1000);
        String bookingId = "B" + (int) (Math.random() * 1000);
        String todayDate = getTodayDate();
        Booking newBooking = new Booking(
                bookingId,
                date,
                day,
                "Booked",
                todayDate,
                patient.getID(),
                doctorSelected.getDoctorID()
        );
        AppointmentQueue newQueue = new AppointmentQueue(
                timeSlot,
                queueId,
                doctorSelected.getDoctorID(),
                patient.getID(),
                num,
                "Booked",
                day,
                bookingId
        );
        bookingList.add(newBooking);
        queue.enqueue(newQueue);
        bookingDao.saveFile(bookingList);
        queueDao.saveFile(queue);
        System.out.println("Booking successful!");
        System.out.println("day(this week): " + day);
        System.out.println("Time: " + timeSlot);
        System.out.println("Your Booking ID: " + bookingId);
    }

    public void checkAppointment() {
        queue = queueDao.loadFromFile();
        ListInterface<Booking> bookingList = bookingDao.loadFromFile();
        Iterator<AppointmentQueue> iterator = queue.getIterator();
        ListInterface<AppointmentQueue> queueListByPatient = new ArrayList<>();
        BookingUi bookingUi = new BookingUi();
        String id;
        String queueId;
        String ic;
        Patient patient;
        Boolean isSuccess = false;
        do {
            id = patientUi.inputPatientId();
            patient = Utility.getPatientFromList(id);
            if (patient == null) {
                System.out.println("Patient not found!!Please process to register");
            }
        } while (patient == null);
        do {
            ic = patientUi.inputIC();
            if (!ic.equals(patient.getIc())) {
                System.out.println("please Enter the correct ic");
            }
        } while (!ic.equals(patient.getIc()));

        while (iterator.hasNext()) {
            AppointmentQueue currentQueue = iterator.next();
            if (currentQueue.getPatientId().equals(id)) {
                queueListByPatient.add(currentQueue);
            }
        }
        do {
            bookingUi.appoinmentUi(queueListByPatient);
            System.out.print("Enter A queue id to cancel,if no enter '0' to exit: ");
            queueId = scanner.nextLine();
            if (queueId.equals("0")) {
                break;
            }
            iterator = queue.getIterator();
            while (iterator.hasNext()) {
                AppointmentQueue currentQueue = iterator.next();
                if (currentQueue.getQueueId().equalsIgnoreCase(queueId)) {
                    for (int i = 1; i <= bookingList.getNumberOfEntries(); i++) {
                        if (bookingList.getEntry(i).getBookingId().equalsIgnoreCase(currentQueue.getBookingId())) {
                            if (currentQueue.getStatus().equalsIgnoreCase("Booked")) {
                                bookingList.getEntry(i).setStatus("Cancelled");
                                isSuccess = queue.remove(currentQueue);
                                break;
                            } else {
                                System.out.println("This slot cannot be cancelled");
                            }
                        }
                    }

                }
            }
            if (isSuccess) {
                System.out.println("Appointment cancel successfully");
            } else {
                System.out.println("Appointment cancel unsuccessfully. The queue id might not correctly. Please try again");
            }
        } while (!isSuccess);
        bookingDao.saveFile(bookingList);
        queueDao.saveFile(queue);
    }

    public void bookingHistory() {
        ListInterface<Booking> bookingList = bookingDao.loadFromFile();
        ListInterface<Booking> bookingByPatietnId = new ArrayList<>();
        BookingUi bookingUi = new BookingUi();
        String id;
        String ic;
        Patient patient;
        do {
            id = patientUi.inputPatientId();
            patient = Utility.getPatientFromList(id);
            if (patient == null) {
                System.out.println("Patient not found!!Please process to register");
            }
        } while (patient == null);
        do {
            ic = patientUi.inputIC();
            if (!ic.equals(patient.getIc())) {
                System.out.println("please Enter the correct ic");
            }
        } while (!ic.equals(patient.getIc()));

        for (int i = 1; i <= bookingList.getNumberOfEntries(); i++) {
            if (bookingList.getEntry(i).getPatientId().equals(id)) {
                bookingByPatietnId.add(bookingList.getEntry(i));
            }
        }
        bookingUi.bookingHistoryUi(bookingByPatietnId);

    }

    public String setDate(String day) {
        LocalDate today = LocalDate.now();

        DayOfWeek targetDay = DayOfWeek.valueOf(day.toUpperCase());

        LocalDate date = today.with(TemporalAdjusters.nextOrSame(targetDay));

        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return formattedDate;
    }

    public String getTodayDate() {
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return formattedDate;
    }

    public QueueInterface<AppointmentQueue> getQueue() {
        return queueDao.loadFromFile();
    }

    public ListInterface<DoctorSchedule> getScheduleByid(String doctorId, ListInterface<DoctorSchedule> scheduleList) {
        Map<String, ListInterface<DoctorSchedule>> scheduleMap = new HashMap<>();
        for (int i = 1; i <= scheduleList.getNumberOfEntries(); i++) {
            DoctorSchedule schedule = scheduleList.getEntry(i);

            if ("Leave".equalsIgnoreCase(schedule.getStatus())) {
                continue;
            }
            String id = schedule.getDoctorID();

            if (!scheduleMap.containsKey(id)) {
                scheduleMap.put(id, new ArrayList<>());
            }

            scheduleMap.get(id).add(schedule);
        }

        return scheduleMap.getOrDefault(doctorId, new ArrayList<>());

    }

    public int getNumberOfPatientInQueue(String timeSlot, String day, String doctorId, QueueInterface<AppointmentQueue> queue) {
        int count = 0;
        if (queue == null) {
            return 1;
        }
        Iterator<AppointmentQueue> iterator = queue.getIterator();
        while (iterator.hasNext()) {
            AppointmentQueue currentQueue = iterator.next();
            if (currentQueue.getDoctorId().equals(doctorId) && currentQueue.getDay().equals(day) && currentQueue.getTimeSlot().equals(timeSlot)) {
                count++;
            }
        }
        return count;
    }

    public QueueInterface<AppointmentQueue> changeFrontStatus(QueueInterface<AppointmentQueue> queue, String status) {
        if (queue == null || queue.isEmpty()) {
            return queue;
        }
        queue.getFront().setStatus(status);
        return queue;
    }

    public void displayQueue() {
        queue = getQueue();
        Iterator<AppointmentQueue> iterator = queue.getIterator();

        if (queue.isEmpty()) {
            System.out.println("No appointments in the queue.");
            return;
        }

        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s%n",
                "QueueID", "BookingID", "DoctorID", "PatientID", "TimeSlot", "Day", "No.", "Status");

        while (iterator.hasNext()) {
            AppointmentQueue q = iterator.next();
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10d %-10s%n",
                    q.getQueueId(),
                    q.getBookingId(),
                    q.getDoctorId(),
                    q.getPatientId(),
                    q.getTimeSlot(),
                    q.getDay(),
                    q.getNum(),
                    q.getStatus());
        }
    }

    public void queueManagement() {
        Utility.customMenu(
                new String[]{
                    "1.Display Queue",
                    "2.Delete Queue",
                    "0.Back"
                },
                "Queue Management",
                "Enter your choice: ",
                new Runnable[]{
                    () -> {
                        displayQueue();
                    },
                    () -> {
                        displayQueue();
                        deleteQueue();
                    },
                    () -> Navigation.goBack()
                }
        );
    }

    public void deleteQueue() {
        System.out.print("Choose which queue you want to delete: ");
        String queueId = scanner.nextLine();
        boolean removedQueue = removeByQueueId(queueId);
        if (removedQueue) {
            System.out.println("Queue id " + queueId + " has been removed");
            queueDao.saveFile(queue);
        } else {
            System.out.println("Queue id " + queueId + " not found");
        }
    }

    public boolean removeByQueueId(String queueId) {
        queue = queueDao.loadFromFile();
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
