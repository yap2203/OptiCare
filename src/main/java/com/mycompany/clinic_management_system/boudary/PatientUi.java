package com.mycompany.clinic_management_system.boudary;

/**
 *
 * @author huany
 */
import com.mycompany.clinic_management_system.control.*;
import com.mycompany.clinic_management_system.entity.Patient;
import com.mycompany.clinic_management_system.utility.Navigation;
import com.mycompany.clinic_management_system.utility.Utility;
import java.util.*;

public class PatientUi {

    Scanner scanner = new Scanner(System.in);

    private static final PatientManager patientManager = new PatientManager();
    private static final QueueManager queueManager = new QueueManager();
    private static final ReportManager reportManager = new ReportManager();

    // === MAIN MENU ===
    public static void menu() {
        Navigation.stack.push(patientMainMenu);

        boolean exit = false;

        while (!Navigation.stack.isEmpty() && !exit) {
            Runnable currentMenu = Navigation.stack.peek();
            if (currentMenu != null) {
                currentMenu.run();
            } else {
                exit = true;
            }
        }
        System.out.println("Returning to main menu...");
    
    }

    public static void editMenu() {

        String[] title = {"1.IC", "2.Name", "3.Gender", "4.birthday", "5.Age", "0.back"};
        boolean exit = false;
        for (int i = 0; i < title.length; i++) {
            System.out.println(title[i]);
        }
        while (!Navigation.stack.isEmpty() && !exit) {
            Runnable currentMenu = Navigation.stack.peek();
            if (currentMenu != null) {
                currentMenu.run();
            } else {
                exit = true;
            }
        }

        System.out.println("Returning to main menu...");
    }

    private static final Runnable patientManagementMenu = () -> {
        String[] options = {
            "1. Add Patient",
            "2. Show Patient Profile",
            "3. Pay Bill",
            "0. Back"
        };

        Runnable[] actions = {
            () -> patientManager.add(),
            () -> {
                Patient patient = patientManager.checkValidPatient();
                if (patient != null) {
                    Navigation.stack.push(patientProfile(patient));
                }
            },
            () -> {
                Patient patient = patientManager.checkValidPatient();
                if (patient != null) {
                    PaymentManager payment = new PaymentManager();
                    payment.pay(patient);
                }
            },
            () -> Navigation.goBack()
        };

        Utility.customMenu(options, "Patient Management", "Enter your choice: ", actions);
    };
    
    private static final Runnable appointmentMenu = () -> {
        String[] options = {
            "1. Book a Slot",
            "2. Check Your Appointment",
            "3. Check Booking History",
            "4. Queue Management",
            "0. Back"
        };

        Runnable[] actions = {
            () -> queueManager.booking(),
            () -> queueManager.checkAppointment(),
            () -> queueManager.bookingHistory(),
            () -> queueManager.queueManagement(),
            () -> Navigation.goBack()
        };

        Utility.customMenu(options, "Appointment Menu", "Enter your choice: ", actions);
    };

    private static final Runnable reportMenu = () -> {
        String[] options = {
            "1. Show Patient Demographic Report",
            "2. Show Frequent Visit Report",
            "0. Back"
        };

        Runnable[] actions = {
            () -> reportManager.generatePatietnDemographicReport(),
            () -> reportManager.generatePatientFrequentVisitReport(),
            () -> Navigation.goBack()
        };

        Utility.customMenu(options, "Report Menu", "Enter your choice: ", actions);
    };

    private static final Runnable patientMainMenu = () -> {
        String[] options = {
            "1. Patient Management",
            "2. Appointment",
            "3. Report",
            "0. Back"
        };

        Runnable[] actions = {
            () -> Navigation.stack.push(patientManagementMenu),
            () -> Navigation.stack.push(appointmentMenu),
            () -> Navigation.stack.push(reportMenu),
            () -> Navigation.goBack()
        };

        Utility.customMenu(options, "Patient Menu", "Enter your choice: ", actions);
    };

    private static Runnable patientProfile(Patient patient) {
        return () -> {
            System.out.println("========= Patient Profile =========");
            System.out.println(String.format(
                    "Name: %s\nGender: %s\nIC: %s\nBirthday: %s\nAge: %d\nPatient ID: %s",
                    patient.getName(),
                    patient.getGender(),
                    patient.getIc(),
                    patient.getBirthday(),
                    patient.getAge(),
                    patient.getID()
            ));

            String[] options = {
                "1. Edit Patient Profile",
                "2. Delete Patient",
                "0. Back"
            };

            Runnable[] actions = {
                () -> patientManager.edit(patient),
                () -> patientManager.delete(patient),
                () -> Navigation.goBack()
            };

            Utility.customMenu(options, "Patient Profile", "Enter your choice: ", actions);
        };
    }

    public String inputName() {

        System.out.print("Enter Your Full Name:");
        String name = scanner.nextLine();
        return name;

    }

    public String inputGender() {

        System.out.print("Enter Your Gender(M(Male),F(Female)):");
        String gender = scanner.nextLine();
        return gender;
    }

    public String inputIC() {
        System.out.print("Enter Your IC No (e.g. 111111-11-1111):");
        String ic = scanner.nextLine();
        return ic;
    }

    public String inputPatientId() {
        System.out.print("Enter Your patient id:");
        String id = scanner.nextLine();
        return id;
    }

    public String inputBirthday() {
        System.out.print("Enter Your Birthday(e.g. 02/05/2050):");
        String ic = scanner.nextLine();
        return ic;
    }

    public String inputAge() {
        System.out.print("Enter Your Age:");
        String age = scanner.nextLine();
        return age;
    }
}
