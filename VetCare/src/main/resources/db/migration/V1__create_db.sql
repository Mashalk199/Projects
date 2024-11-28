-- Clinics Table
DROP TABLE IF EXISTS clinics;
CREATE TABLE clinics (
    "clinic_id" INT AUTO_INCREMENT PRIMARY KEY,
    "clinic_name" VARCHAR(100),
    "address" VARCHAR(255),
    "price" FLOAT
);


-- Veterinarians Table
DROP TABLE IF EXISTS veterinarians;
CREATE TABLE veterinarians (
    "vet_id" INT AUTO_INCREMENT PRIMARY KEY,
    "first_name" VARCHAR(50),
    "last_name" VARCHAR(50),
    "phone_number" VARCHAR(15),
    "email" VARCHAR(100),
    "clinic_id" INT,
    FOREIGN KEY ("clinic_id") REFERENCES clinics("clinic_id") ON DELETE CASCADE
);

-- Users Table
DROP TABLE IF EXISTS users;
CREATE TABLE users (
    "user_id" INT AUTO_INCREMENT PRIMARY KEY,
    "username" VARCHAR(100) UNIQUE,
    "password" VARCHAR(255),
    "role" VARCHAR(250),
    "first_name" VARCHAR(50),
    "last_name" VARCHAR(50),
    "email" VARCHAR(100),
    "address" VARCHAR(255),
    "vet_id" INT, 
    FOREIGN KEY ("vet_id") REFERENCES veterinarians("vet_id") ON DELETE CASCADE
);

-- Current user table TODO standardise
DROP TABLE IF EXISTS "current_user";
CREATE TABLE "current_user" (
    "user_id" INT PRIMARY KEY,
    FOREIGN KEY ("user_id") REFERENCES users("user_id") ON DELETE SET NULL
);


-- Articles Table
DROP TABLE IF EXISTS articles;
CREATE TABLE articles (
    "article_id" INT AUTO_INCREMENT PRIMARY KEY,
    "title" VARCHAR(255),
    "content" TEXT,
    "publish_date" DATE,
    "author_id" INT,
    FOREIGN KEY ("author_id") REFERENCES users("user_id") ON DELETE SET NULL
);

-- Pets Table
DROP TABLE IF EXISTS pets;
CREATE TABLE pets (
    "pet_id" INT AUTO_INCREMENT PRIMARY KEY,
    "owner_id" INT,
    "name" VARCHAR(50),
    "species" VARCHAR(50),
    "gender" VARCHAR(10),
    "age" INT,
    FOREIGN KEY ("owner_id") REFERENCES users("user_id") ON DELETE CASCADE
);

-- Prescriptions Table
DROP TABLE IF EXISTS prescriptions;
CREATE TABLE prescriptions (
    "prescription_id" INT AUTO_INCREMENT PRIMARY KEY,
    "medicine_name" VARCHAR(100),
    "dosage" VARCHAR(100),
    "frequency" VARCHAR(100)
);

-- Active Prescriptions Table
DROP TABLE IF EXISTS active_prescriptions;
CREATE TABLE active_prescriptions (
    "active_prescription_id" INT AUTO_INCREMENT PRIMARY KEY,
    "prescription_id" INT,
    "expiration_date" Date,
    "pet_id" int,
    FOREIGN KEY ("pet_id") REFERENCES pets("pet_id") ON DELETE CASCADE,
    FOREIGN KEY ("prescription_id") REFERENCES prescriptions("prescription_id") ON DELETE CASCADE
);

-- Medical Records Table
DROP TABLE IF EXISTS medical_records;
CREATE TABLE medical_records (
    "record_id" INT AUTO_INCREMENT PRIMARY KEY,
    "pet_id" INT,
    "clinic_id" INT,
    "date" DATE,
    "description" VARCHAR(250),
    "active_prescription_id" INT,
    "diagnosis" TEXT,
    "treatment" TEXT,
    FOREIGN KEY ("pet_id") REFERENCES pets("pet_id") ON DELETE CASCADE,
    FOREIGN KEY ("clinic_id") REFERENCES clinics("clinic_id") ON DELETE CASCADE,
    FOREIGN KEY ("active_prescription_id") REFERENCES active_prescriptions("active_prescription_id") ON DELETE SET NULL
);

-- Vaccination Table
DROP TABLE IF EXISTS vaccinations;
CREATE TABLE vaccinations (
    "vaccination_id" INT AUTO_INCREMENT PRIMARY KEY,
    "vaccination_name" VARCHAR(100),
    "pet_id" INT,
    "vaccination_date" DATE,
    "description" VARCHAR(100),
    FOREIGN KEY ("pet_id") REFERENCES pets("pet_id") ON DELETE CASCADE
);

-- Delivery Information Table
DROP TABLE IF EXISTS delivery_information;
CREATE TABLE delivery_information (
    "delivery_id" INT AUTO_INCREMENT PRIMARY KEY,
    "active_prescription_id" INT,
    "user_id" INT,
    "delivery_status" VARCHAR(50),  -- Ordered, Shipped, Delivered, Canceled
    "delivery_address" VARCHAR(255),
    "order_date" DATE,              -- The date when the prescription was requested
    "ship_date" DATE,               -- The date when the prescription was shipped
    "estimated_delivery_date" DATE, -- Estimated delivery date
    "actual_delivery_date" DATE,    -- Actual delivery date (filled when the prescription is delivered)
    FOREIGN KEY ("active_prescription_id") REFERENCES active_prescriptions("active_prescription_id") ON DELETE CASCADE,
    FOREIGN KEY ("user_id") REFERENCES users("user_id") ON DELETE CASCADE
);

-- Appointments Table
DROP TABLE IF EXISTS appointments;
CREATE TABLE appointments (
    "appointment_id" INT AUTO_INCREMENT PRIMARY KEY,
    "pet_id" INT,
    "vet_id" INT,
    "user_id" INT,
    "clinic_id" INT,
    "appointment_date" TIMESTAMP,
    FOREIGN KEY ("user_id") REFERENCES users("user_id") ON DELETE CASCADE,
    FOREIGN KEY ("pet_id") REFERENCES pets("pet_id") ON DELETE CASCADE,
    FOREIGN KEY ("vet_id") REFERENCES veterinarians("vet_id") ON DELETE CASCADE,
    FOREIGN KEY ("clinic_id") REFERENCES clinics("clinic_id") ON DELETE CASCADE
);

-- Treatment Plan Table
DROP TABLE IF EXISTS treatment_plan;
CREATE TABLE treatment_plan (
    "treatment_plan_id" INT AUTO_INCREMENT PRIMARY KEY,
    "pet_id" INT,
    "prescription_id" INT,
    "date" DATE,
    "description" VARCHAR(100),
    FOREIGN KEY ("pet_id") REFERENCES pets("pet_id") ON DELETE CASCADE,
    FOREIGN KEY ("prescription_id") REFERENCES prescriptions("prescription_id") ON DELETE CASCADE
);

-- Vet Access Table
DROP TABLE IF EXISTS vet_access;
CREATE TABLE vet_access (
    "pet_id" INT,
    "vet_id" INT,
    PRIMARY KEY ("pet_id", "vet_id"),
    FOREIGN KEY ("pet_id") REFERENCES pets("pet_id") ON DELETE CASCADE,
    FOREIGN KEY ("vet_id") REFERENCES veterinarians("vet_id") ON DELETE CASCADE
);
