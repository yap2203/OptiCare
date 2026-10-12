/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.control;

import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.adt.ArrayList;
import com.mycompany.clinic_management_system.dao.DoctorDao;
import com.mycompany.clinic_management_system.dao.DoctorScheduleDao;
import com.mycompany.clinic_management_system.entity.Doctor;
import com.mycompany.clinic_management_system.entity.DoctorSchedule;
import com.mycompany.clinic_management_system.utility.Navigation;
import com.mycompany.clinic_management_system.utility.Utility;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author zheng
 */
public class DoctorScheduleManager {

    private final ListInterface<DoctorSchedule> scheduleList;
    private final DoctorScheduleDao scheduleDao;
    private final ListInterface<Doctor> doctorList;
    private final DoctorDao doctorDao;
    private final Scanner scanner = new Scanner(System.in);

    public DoctorScheduleManager() {
        this.doctorDao = new DoctorDao();
        this.doctorList = doctorDao.loadDoctorsFromFile();
        this.scheduleDao = new DoctorScheduleDao();
        this.scheduleList = scheduleDao.loadSchedulesFromFile();
    }

    public void addSchedule() {
        System.out.print("Enter Doctor ID: ");
        String doctorID = scanner.nextLine();

        if (!doctorExists(doctorID)) {
            doctorList.clear();
            ListInterface<Doctor> updatedList = doctorDao.loadDoctorsFromFile();
            for (int i = 1; i <= updatedList.getNumberOfEntries(); i++) {
                doctorList.add(updatedList.getEntry(i));
            }
            if (!doctorExists(doctorID)) {
                System.out.println("Error: Doctor ID not found. Cannot add schedule.");
                return;
            }
        }

        System.out.print("Enter Day (Monday to Sunday): ");
        String day = scanner.nextLine();
        if (!isValidDay(day)) {
            System.out.println("Error: Invalid day.");
            return;
        }

        day = normalizeDay(day);

        // Check if the doctor already has a schedule on this day
        boolean exists = false;
        for (int i = 1; i <= scheduleList.getNumberOfEntries(); i++) {
            DoctorSchedule existing = scheduleList.getEntry(i);
            if (existing.getDoctorID().equalsIgnoreCase(doctorID)
                    && existing.getDay().equalsIgnoreCase(day)) {
                exists = true;
                break;
            }
        }

        if (exists) {
            System.out.println("Error: Doctor already has a schedule on " + day + ".");
            return;
        }

        DoctorSchedule schedule = new DoctorSchedule(doctorID, day);
        scheduleList.add(schedule);
        scheduleDao.saveScheduleListToFile(scheduleList);

        System.out.println("Schedule added and saved.");
    }

    public void showSchedules() {

        ArrayList<String> daysOfWeek = Utility.getDaysOfWeek();

        if (scheduleList.isEmpty()) {
            System.out.println("No schedules found.");
            return;
        }

        // Store which doctor works on which day
        Map<String, Set<String>> doctorDayMap = new TreeMap<>(); // TreeMap to sort doctor names

        for (int i = 1; i <= scheduleList.getNumberOfEntries(); i++) {
            DoctorSchedule sched = scheduleList.getEntry(i);

            if ("Leave".equalsIgnoreCase(sched.getStatus())) {
                continue;
            }

            String day = sched.getDay();

            // Add doctor if not exist
            doctorDayMap.putIfAbsent(sched.getDoctorID(), new HashSet<>());
            doctorDayMap.get(sched.getDoctorID()).add(day); // Mark that doctor works on this day
        }

        // Print Header
        System.out.println();
        System.out.println("=".repeat(30 + (daysOfWeek.getNumberOfEntries() * 13)));
        System.out.printf("%-15s", "Doctor Id");
        System.out.printf("|%-15s", "Doctor Name");
        for (int i = 1; i <= daysOfWeek.getNumberOfEntries(); i++) { // 1-based index
            String day = daysOfWeek.getEntry(i);
            System.out.printf("| %-10s", day);
        }
        System.out.println();
        System.out.println("=".repeat(30 + (daysOfWeek.getNumberOfEntries() * 13)));

        // Print rows per doctor
        for (Map.Entry<String, Set<String>> entry : doctorDayMap.entrySet()) {
            String doctorId = entry.getKey();
            Set<String> daysWorked = entry.getValue();

            System.out.printf("%-15s", doctorId);
            System.out.printf("|%-15s", Utility.getDoctorNameById(doctorId, doctorList));

            // loop through your custom dayOrder list
            for (int i = 1; i <= daysOfWeek.getNumberOfEntries(); i++) {
                String day = daysOfWeek.getEntry(i);
                String marker = daysWorked.contains(day) ? "    /" : "";
                System.out.printf("| %-10s", marker);
            }
            System.out.println();
        }
    }

    public void updateSchedule() {
        System.out.print("Enter Doctor ID: ");
        String doctorID = scanner.nextLine().trim();

        if (!doctorExists(doctorID)) {
            System.out.println("Error: Doctor ID not found.");
            return;
        }

        System.out.print("Enter Current Day of Schedule to Update (Monday to Sunday): ");
        String currentDay = scanner.nextLine().trim();

        if (!isValidDay(currentDay)) {
            System.out.println("Error: Invalid day.");
            return;
        }
        currentDay = normalizeDay(currentDay);

        DoctorSchedule toUpdate = null;
        for (int i = 1; i <= scheduleList.getNumberOfEntries(); i++) {
            DoctorSchedule sched = scheduleList.getEntry(i);
            if (sched.getDoctorID().equals(doctorID) && sched.getDay().equalsIgnoreCase(currentDay)) {
                toUpdate = sched;
                break;
            }
        }

        if (toUpdate == null) {
            System.out.println("No schedule found for that day.");
            return;
        }

        System.out.print("Enter New Day for Schedule (Monday to Sunday): ");
        String newDay = scanner.nextLine().trim();

        if (!isValidDay(newDay)) {
            System.out.println("Error: Invalid new day.");
            return;
        }
        newDay = normalizeDay(newDay);

        // Check if the doctor already has a schedule on the new day (excluding the current entry)
        for (int i = 1; i <= scheduleList.getNumberOfEntries(); i++) {
            DoctorSchedule sched = scheduleList.getEntry(i);
            if (sched != toUpdate && sched.getDoctorID().equals(doctorID) && sched.getDay().equalsIgnoreCase(newDay)) {
                System.out.println("Error: Doctor already has a schedule on " + newDay + ".");
                return;
            }
        }

        //Update the day
        toUpdate.setDay(newDay);

        scheduleDao.saveScheduleListToFile(scheduleList);
        System.out.println("Schedule day updated successfully.");
    }

    public void deleteSchedule() {
        System.out.print("Enter Doctor ID: ");
        String doctorID = scanner.nextLine().trim();

        if (!doctorExists(doctorID)) {
            System.out.println("Error: Doctor ID not found.");
            return;
        }

        System.out.print("Enter Day of Schedule to Delete (Monday to Saturday): ");
        String day = scanner.nextLine().trim();

        if (!isValidDay(day)) {
            System.out.println("Error: Invalid day.");
            return;
        }

        day = normalizeDay(day);

        boolean found = false;
        for (int i = 1; i <= scheduleList.getNumberOfEntries(); i++) {
            DoctorSchedule sched = scheduleList.getEntry(i);
            if (sched.getDoctorID().equalsIgnoreCase(doctorID)
                    && sched.getDay().equalsIgnoreCase(day)) {
                scheduleList.remove(i);
                scheduleDao.saveScheduleListToFile(scheduleList);
                System.out.println("Schedule deleted successfully.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("No schedule found for Doctor ID " + doctorID + " on " + day + ".");
        }
    }

    public void applyLeave() {
        System.out.print("Enter Doctor ID: ");
        String doctorID = scanner.nextLine();

        if (!doctorExists(doctorID)) {
            System.out.println("Error: Doctor ID not found. Cannot apply leave.");
            return;
        }

        System.out.print("Enter Day (Monday to Sunday): ");
        String day = scanner.nextLine();
        if (!isValidDay(day)) {
            System.out.println("Error: Invalid day.");
            return;
        }

        day = normalizeDay(day);

        // Check if schedule exists for that doctorfor that day
        DoctorSchedule schedule = null;
        for (int i = 1; i <= scheduleList.getNumberOfEntries(); i++) {
            DoctorSchedule s = scheduleList.getEntry(i);
            if (s.getDoctorID().equals(doctorID) && s.getDay().equalsIgnoreCase(day)) {
                schedule = s;
                break;
            }
        }

        if (schedule == null) {
            System.out.println("Error: No existing schedule for Doctor " + doctorID + " on " + day + ". Cannot apply leave.");
            return;
        }

        // Mark status as leave
        schedule.setStatus("Leave");

        // Save updated list
        scheduleDao.saveScheduleListToFile(scheduleList);

        System.out.println("Leave applied and saved for Doctor " + doctorID + " on " + day + ".");
    }

    // ========== HELPER METHODS ==========
    private boolean doctorExists(String doctorID) {
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            if (doctorList.getEntry(i).getDoctorID().equalsIgnoreCase(doctorID)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidDay(String day) {
        ArrayList<String> dayOrder = Utility.getDaysOfWeek();
        if (day == null) {
            return false;
        }

        for (int i = 1; i <= dayOrder.getNumberOfEntries(); i++) {
            if (dayOrder.getEntry(i).equalsIgnoreCase(day)) {
                return true;
            }
        }
        return false;
    }

    private String normalizeDay(String day) {
        return day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
    }
}
