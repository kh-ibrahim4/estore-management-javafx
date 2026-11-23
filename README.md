# E-Store Management System

A desktop inventory manager for a small online store, built with **JavaFX** and **MySQL**. It provides a product catalogue with add, update and delete operations, a role-aware product view, and login and registration screens.

> University coursework project (Java / JavaFX).

## Features

- **Product management** – add, update and delete products (code, name, price, quantity, currency, date, image URL).
- **Live table view** – products are listed in a `TableView`; selecting a row loads its details and image preview into the form.
- **Role-aware listing** – an admin sees every product, a regular user sees only the products they added.
- **Input validation** – required fields and numeric values are checked before anything is written to the database.
- **Safe SQL** – all queries use `PreparedStatement`.
- **FXML + CSS UI** – views are separated from controllers and styled with a custom stylesheet.

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 24 |
| UI | JavaFX 24 (FXML, CSS) |
| Database | MySQL 8, JDBC (`mysql-connector-j`) |
| IDE / build | NetBeans (Ant) |

## Project structure

```
FX_eStoreProject/
├── src/
│   ├── app/                  # Controllers, models and helpers
│   │   ├── LoginController.java
│   │   ├── RegisterController.java
│   │   ├── StoreController.java
│   │   ├── Product.java, User.java, Session.java
│   │   └── ImageHelper.java, MD5Helper.java
│   ├── database/
│   │   ├── DBConnection.java # JDBC connection
│   │   └── estore_db.sql     # Database schema
│   ├── views/                # login.fxml, register.fxml, store.fxml
│   ├── styles/style.css
│   └── fx_estoreproject/     # Application entry point
├── nbproject/                # NetBeans project files
└── build.xml
```

## Getting started

### Requirements

- JDK 24
- [JavaFX SDK 24](https://openjfx.io/)
- MySQL server (for example through XAMPP)
- [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/kh-ibrahim4/estore-management-javafx.git
   ```
2. Create the database by running `FX_eStoreProject/src/database/estore_db.sql` in MySQL.
3. If your MySQL user is not `root` with an empty password, edit the connection URL in `src/database/DBConnection.java`.
4. Open `FX_eStoreProject` in NetBeans and add the JavaFX SDK and MySQL Connector/J jars to the project libraries.
5. Set the VM options to point at your JavaFX SDK:
   ```
   --module-path "/path/to/javafx-sdk-24/lib" --add-modules=javafx.controls,javafx.fxml
   ```
6. Run the project.

## Project status and known limitations

This is a learning project and is not finished:

- The login and registration screens are built, but their handlers are not yet connected to the database, so the store screen is not reached through a real sign-in.
- Password hashing uses MD5, which is not secure. It should be replaced with bcrypt or Argon2.
- Database credentials are hard-coded in `DBConnection.java`.

## Possible improvements

- Wire login and registration to the `users` table and pass the session to the store screen.
- Replace MD5 with a modern password hash.
- Move connection settings to a configuration file.
- Add product search and filtering.
- Migrate the build to Maven or Gradle so dependencies are resolved automatically.

## Author

**Khalil I. Al-Absi** – [GitHub](https://github.com/kh-ibrahim4) · [LinkedIn](https://www.linkedin.com/in/khalil-alabsi/)
