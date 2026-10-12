/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.clinic_management_system.dao;

import com.mycompany.clinic_management_system.adt.ArrayList;
import com.mycompany.clinic_management_system.adt.ListInterface;
import com.mycompany.clinic_management_system.entity.DoctorSchedule;
import java.awt.List;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.IsoFields;

/**
 *
 * @author zheng
 */
public class DoctorScheduleDao {

    private static final String SCHEDULE_FILE_PATH = "data/Schedule_Data.txt";

    public void validateScheduleWeek() {
        LocalDate lastUpdated = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(SCHEDULE_FILE_PATH))) {
            String firstLine = reader.readLine();
            if (firstLine != null && firstLine.startsWith("Schedule Last Updated:")) {
                String dateStr = firstLine.replace("Schedule Last Updated:", "").trim();
                lastUpdated = LocalDate.parse(dateStr);
            }
        } catch (IOException e) {
            System.out.println("No existing schedule file found, will start fresh.");
            return;
        }

        if (lastUpdated != null) {
            LocalDate today = LocalDate.now();

            int savedWeek = lastUpdated.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            int todayWeek = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

            if (savedWeek != todayWeek || lastUpdated.getYear() != today.getYear()) {
                resetLeaveStatus();
            } 
        }
    }

    public void resetLeaveStatus() {
        Path filePath = Path.of(SCHEDULE_FILE_PATH);
        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                // Keep header lines as-is
                if (lineNumber <= 3) {
                    lines.add(line);
                    continue;
                }

                if (line.trim().isEmpty()) {
                    lines.add(line);
                    continue;
                }

                // Split doctor schedule line: DoctorID|Day|Status
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String doctorID = parts[0].trim();
                    String day = parts[1].trim();
                    String status = parts[2].trim();

                    if ("Leave".equalsIgnoreCase(status)) {
                        status = "Available"; // reset leave
                    }

                    lines.add(doctorID + "|" + day + "|" + status);
                } else {
                    lines.add(line); // keep unexpected lines as-is
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading schedule file: " + e.getMessage());
            return;
        }

        // Write back updated file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            for (int i = 1; i <= lines.getNumberOfEntries(); i++) {
                writer.write(lines.getEntry(i));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing schedule file: " + e.getMessage());
        }
    }

    public void updateScheduleHeaderDate() {
        Path filePath = Path.of(SCHEDULE_FILE_PATH);

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            ArrayList<String> lines = new ArrayList<>(); // your custom list

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            if (lines.isEmpty()) {
                return;
            }

            // Update first line to today
            lines.replace(1, "Schedule Last Updated: " + LocalDate.now());

            // Write back to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                for (int i = 1; i <= lines.getNumberOfEntries(); i++) {
                    writer.write(lines.getEntry(i));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating schedule header: " + e.getMessage());
        }
    }

    public ListInterface<DoctorSchedule> loadSchedulesFromFile() {
        ListInterface<DoctorSchedule> scheduleList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(SCHEDULE_FILE_PATH))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip header lines
                if (lineNumber <= 4) {
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Remove trailing pipe
                if (line.endsWith("|")) {
                    line = line.substring(0, line.length() - 1);
                }

                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    String doctorID = parts[0].trim();
                    String day = parts[1].trim();
                    String status = parts[2].trim();

                    DoctorSchedule schedule = new DoctorSchedule(doctorID, day, status);
                    scheduleList.add(schedule);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading schedule file: " + e.getMessage());
        }

        return scheduleList;
    }

    public void saveScheduleListToFile(ListInterface<DoctorSchedule> scheduleList) {
        Path filePath = Path.of(SCHEDULE_FILE_PATH);
        StringBuilder content = new StringBuilder();

        LocalDate today = LocalDate.now();
        content.append("Schedule Last Updated: ").append(today).append("\n");
        content.append("===================================\n");
        content.append("DoctorID | Day | Status\n");
        content.append("===================================\n");

        for (int i = 1; i <= scheduleList.getNumberOfEntries(); i++) {
            DoctorSchedule sched = scheduleList.getEntry(i);
            content.append(String.format("%s|%s|%s%n",
                    sched.getDoctorID(), sched.getDay(), sched.getStatus()));
        }

        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, content.toString().getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Schedule list saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving schedule list: " + e.getMessage());
        }
    }

}
