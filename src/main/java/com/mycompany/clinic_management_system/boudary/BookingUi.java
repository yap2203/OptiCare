/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.boudary;

/**
 *
 * @author huany
 */
import com.mycompany.clinic_management_system.adt.*;
import com.mycompany.clinic_management_system.entity.*;
import com.mycompany.clinic_management_system.utility.Utility;
import java.util.*;

public class BookingUi {

    Scanner scanner = new Scanner(System.in);

    public String inputTimeSlot() {
        System.out.print("Enter a time slot (e.g. 8.00):");
        String timeSlot = scanner.nextLine();
        return timeSlot;
    }

    public String inputDoctorId() {
        System.out.print("Enter Doctor ID:");
        String id = scanner.nextLine();
        return id;
    }

    public void bookingHistoryUi(ListInterface<Booking> bookingList) {
        System.out.printf("%-12s %-12s %-10s %-12s %-15s %-20s\n",
                "Booking ID", "Date", "Day", "Status", "Booking Date", "Doctor Name");
        System.out.println("-------------------------------------------------------------------------------------------");
        if (bookingList != null && !bookingList.isEmpty()) {
            for (int i = 1; i <= bookingList.getNumberOfEntries(); i++) {
                Doctor doctor = Utility.getDoctorById(bookingList.getEntry(i).getDoctorId());
                System.out.printf("%-12s %-12s %-10s %-12s %-15s %-20s\n",
                        bookingList.getEntry(i).getBookingId(),
                        bookingList.getEntry(i).getDate(),
                        bookingList.getEntry(i).getDay(),
                        bookingList.getEntry(i).getStatus(),
                        bookingList.getEntry(i).getBookingDate(),
                        doctor.getName());
            }
        } else {
            System.out.println("No record found!!");
        }

    }

    public void appoinmentUi(ListInterface<AppointmentQueue> queueList) {
        System.out.println("Your appointmetn this week");
        System.out.printf("%-12s %-12s %-10s %-12s %-15s %-20s %-15s\n",
                "Queue ID", "Booking ID", "Day", "Status", "Your Number", "Doctor Name", "Time Slot");
        System.out.println("------------------------------------------------------------------------------------------------------");
        if (queueList != null && !queueList.isEmpty()) {
            for (int i = queueList.getNumberOfEntries(); i > 0; --i) {
                AppointmentQueue aq = queueList.getEntry(i);

                Doctor doctor = Utility.getDoctorById(aq.getDoctorId());

                System.out.printf("%-12s %-12s %-10s %-12s %-15d %-20s %-15s\n",
                        aq.getQueueId(),
                        aq.getBookingId(),
                        aq.getDay(),
                        aq.getStatus(),
                        aq.getNum(),
                        doctor.getName(),
                        aq.getTimeSlot()
                );
            }
        } else {
            System.out.println("No record found!!");
        }
    }

    public void doctorSchedule(ListInterface<DoctorSchedule> scheduleList, QueueInterface<AppointmentQueue> queue) {
        String[] timeSlot = {"8.00", "9.00", "10.00", "11.00", "12.00",
            "13.00", "14.00", "15.00", "16.00", "17.00"};
        String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        final int MAX_CAPACITY = 4;

        // Build a set of working days for the doctor
        Set<String> workingDays = new HashSet<>();
        for (int i = 1; i <= scheduleList.getNumberOfEntries(); i++) {
            DoctorSchedule sched = scheduleList.getEntry(i);
            workingDays.add(sched.getDay());
        }

        // Print header row (Days)
        System.out.printf("%-10s", "Time Slot");
        for (String day : dayNames) {
            System.out.printf("| %-10s", day);
        }
        System.out.println();
        System.out.println("=".repeat(10 + (dayNames.length * 13)));

        // Loop over each time slot
        for (String slot : timeSlot) {
            System.out.printf("%-10s", slot); // Time slot label

            for (String day : dayNames) {
                int available = 0;

                // If doctor works on that day
                if (workingDays.contains(day)) {
                    int bookedCount = 0;

                    // Count bookings from queue for that day + timeslot
                    Iterator<AppointmentQueue> iterator = queue.getIterator();
                    while (iterator.hasNext()) {
                        AppointmentQueue appt = iterator.next();
                        if (appt.getDay().equalsIgnoreCase(day)
                                && appt.getTimeSlot().equals(slot)) {
                            bookedCount++;
                        }
                    }

                    available = Math.max(0, MAX_CAPACITY - bookedCount);
                }

                System.out.printf("| %-10d", available);
            }
            System.out.println();
        }

    }
}
