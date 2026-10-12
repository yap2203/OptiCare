# OptiCare — Clinic Management System

OptiCare is a Java console-based clinic management system developed as an academic group project at TAR UMT. It supports patient records, appointment bookings, consultation queues, doctor schedules, treatments, payments, and pharmacy operations.

The project demonstrates object-oriented programming, custom generic data structures, file handling, input validation, and exception handling.

## Technologies

- **Java** — application logic and console interface
- **Maven** — project build management
- **Custom data structures** — array list, linked queue, and stack
- **File I/O** — text files and Java object serialization for persistent records

## Features

### Patient and Appointment Management
- Add, edit, and delete patient records.
- Manage appointment bookings and consultation queues.
- Validate patient information, including identification numbers and dates.

### Doctor Management
- Manage doctor records and weekly schedules.
- View doctor availability.
- Generate doctor workload and availability reports.

### Treatment and Payment Management
- Record patient treatments and diagnoses.
- Manage treatment payments.
- Generate treatment cost, diagnosis frequency, and daily payment reports.

### Pharmacy Management
- Manage medicine records and stock.
- Record medicine dispensing.
- Generate daily dispensing and low-stock reports.

### Reporting
- Patient demographic and frequent-visit reports
- Doctor workload and availability reports
- Treatment, payment, and pharmacy reports

## Java Concepts Demonstrated

- **Encapsulation:** Entity classes represent patients, doctors, medicines, and other records.
- **Interfaces and generics:** Custom list, queue, and stack interfaces support reusable implementations.
- **Data structures:** An array-based list, linked queue, and array-based stack support application operations.
- **Sorting:** Comparator-based sorting is implemented in the custom array list.
- **File persistence:** Records are saved and loaded using text files and object serialization.
- **Validation and exception handling:** User input and file operations are checked and handled throughout the application.
- **Separation of responsibilities:** Console interfaces, business logic, entities, and data access are organised into separate packages.

## Project Structure

| Location | Purpose |
|---|---|
| `src/main/java/com/mycompany/clinic_management_system/` | Application source code and entry point |
| `adt/` | Custom data structures and interfaces |
| `boudary/` | Console menus and user interfaces |
| `control/` | Business logic and report generation |
| `dao/` | File-based data access |
| `entity/` | Domain models |
| `utility/` | Shared helpers and navigation |
| `data/` | Stored application records |
| `pom.xml` | Maven configuration |

The `boudary` package name follows the existing source code.

## Getting Started

### Requirements

- JDK 22 or newer
- Apache Maven
- Git, or a downloaded copy of the repository

The Maven configuration targets Java 22.

### Clone the Repository

```bash
git clone https://github.com/yap2203/OptiCare.git
cd OptiCare
```

### Compile

```bash
mvn compile
```

### Run

```bash
java -cp target/classes com.mycompany.clinic_management_system.Clinic_management_system
```

Run the application from the repository root so it can locate the `data/` folder. Operations may update the stored records.

### Run in an IDE

1. Open the repository as a Maven project in a Java IDE.
2. Configure the project to use JDK 22 or newer.
3. Set the working directory to the repository root.
4. Run `Clinic_management_system.java`.

## Project Context

OptiCare was developed collaboratively as an academic group project. This repository presents the combined application and its implementation of Java programming concepts.

The application uses a console interface and local file storage. It is an educational project rather than a production clinic system.
