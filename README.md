# Bus Reservation System

This Project is written in **Java 8** and follows the principles of **Object-Oriented Programming (OOP)**.

---

## 📖 Project Description
The Bus Reservation System manages bus voyages and tickets.  
It allows creating or cancelling voyages, selling or refunding seats, and printing voyage information.  
At the end, the program also prints a **Z Report** that summarizes all voyages.

There are three types of buses:
- **Standard Bus (2+2)** → normal seats, refundable with a cut rate.
- **Premium Bus (1+2)** → has both standard and premium seats, refundable with a cut rate, premium seats are more expensive.
- **Minibus (2)** → only standard seats, but tickets are **not refundable**.

---

## ⚙️ Features
- **Initialize a Voyage** (`INIT_VOYAGE`)  
  Create a new bus voyage with given rows, prices, and refund rules.
- **Sell Ticket** (`SELL_TICKET`)  
  Sell one or more available seats for a voyage.
- **Refund Ticket** (`REFUND_TICKET`)  
  Refund one or more tickets (except minibus). Refund amount is reduced by the refund cut.
- **Print Voyage** (`PRINT_VOYAGE`)  
  Show voyage details, revenue, and seating plan (empty = `*`, sold = `X`).
- **Cancel Voyage** (`CANCEL_VOYAGE`)  
  Cancel a voyage, refund all sold tickets without any cut, and print the last state.
- **Z Report** (`Z_REPORT`)  
  Print all voyages in ascending order of IDs with their details.

---

## 📂 Input / Output
- The program reads commands from an **input file**.  
- It writes all results to an **output file** in the exact required format.  
- Example commands:  
  ```
  INIT_VOYAGE   Standard   7   Ankara   Istanbul   12   400   12
  SELL_TICKET   7   5_6
  PRINT_VOYAGE  7
  Z_REPORT
  ```

---

## 🚀 How to Run
Compile all Java files:
```bash
javac8 *.java
```

Run the system with input and output files:
```bash
java8 BookingSystem input.txt output.txt
```

---

## 🏗️ Object-Oriented Design
This project uses the four pillars of OOP:
- **Abstraction**: Common structure for voyages and buses.
- **Encapsulation**: Attributes of buses and voyages are kept private.
- **Inheritance**: Different bus types (Standard, Premium, Minibus) inherit from a base bus class.
- **Polymorphism**: Different behaviors for ticket selling, refunding, and pricing depending on the bus type.

---

## 📑 Notes
- Program must handle invalid commands and print errors in format:  
  ```
  ERROR: <ERROR_CAUSE>
  ```
- Minibus tickets are not refundable.
- Refund amounts are calculated based on the refund cut.
- Z Report is always printed at the end of the output.

---
