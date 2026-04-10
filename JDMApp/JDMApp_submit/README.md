# JDM Electronic Patient Dossier
### Juvenile Dermatomyositis PatientX Dataset

CLI-based Java application that loads and browses the PatientX longitudinal healthcare dataset.

---

## How to Run

**Requirements:** Java 11 or newer installed on your system.

1. Place the `PatientX/` folder in the same directory as `JDMApp.jar`
2. Run:

```
# Windows
run.bat

# Mac / Linux
bash run.sh

# Or directly
java -jar JDMApp.jar PatientX
```

---

## Features

| Option | Description |
|--------|-------------|
| 1      | **Patient Summary** — name, ID, dataset statistics, latest CMAS score |
| 2      | **Browse by Group** — pick a lab result group, then drill into individual results and their measurements |
| 3      | **CMAS History** — all 57 assessments listed newest-first |
| 4      | **Search** — find lab results by name keyword (e.g. CXCL, Sodium, TNFR) |

---

## Project Structure

```
src/main/java/jdm/
├── Main.java                   Entry point
├── model/
│   ├── Patient.java
│   ├── LabResultGroup.java
│   ├── LabResult.java
│   ├── Measurement.java
│   └── CmasEntry.java
├── repository/
│   └── DataLoader.java         Reads all CSVs into memory
├── service/
│   └── PatientService.java     Business logic & queries
├── util/
│   └── CsvReader.java          CSV parsing utility
└── cli/
    └── Menu.java               Interactive CLI menu
```

---

## Dataset Overview

| File                  | Contents                              |
|-----------------------|---------------------------------------|
| Patient.csv           | 1 patient (Patient X)                 |
| LabResultGroup.csv    | 26 groups (e.g. Blood Chemistry, cytokines) |
| LabResults(EN).csv    | 124 lab result types with English names |
| Measurement.csv       | 344 individual measurements over time |
| CMAS.csv              | 57 CMAS assessments (2017–2023)       |
