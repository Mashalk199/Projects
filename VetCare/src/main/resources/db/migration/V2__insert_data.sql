-- Clinics Table Data
INSERT INTO clinics ("clinic_name", "address", "price")
VALUES
    ('Alpha Pet Clinic', '123 A Street, A City', 125),
    ('Beta Pet Clinic', '456 B Street, B Town', 130),
    ('Gamma Pet Clinic', '789 C Street, C City', 190),
    ('Delta Pet Clinic', '999 D Street, D City', 95);

-- Veterinarians Table Data
INSERT INTO veterinarians ("first_name", "last_name", "phone_number", "email", "clinic_id")
VALUES
    ('Samantha', 'Whitley', '123456789', 'samantha.whitley@mail.com', '1'),
    ('Jonathan', 'Rhodes', '123456789', 'jonathan.rhodes@mail.com', '1'),

    ('Amanda', 'Gray', '123456789', 'amanda.gray@mail.com', '2'),
    ('Benjamin', 'Carter', '123456789', 'benjamin.carter@mail.com', '2'),

    ('Sarah', 'Mitchell', '123456789', 'sarah.mitchell@mail.com', '3'),
    ('Anthony', 'Wells', '123456789', 'anthony.wells@mail.com', '3'),

    ('Laura', 'Bennett', '123456789', 'laura.bennett@mail.com', '4'),
    ('Christopher', 'Hayes', '123456789', 'christopher.hayes@mail.com', '4');

-- Users Table Data
INSERT INTO users ("username", "password", "role", "first_name", "last_name", "email", "address", "vet_id")
VALUES
    ('John', '1234', 'admin', 'Mr', 'Butcher', 'test@mail.com', '12 Third Street', null),
    ('Bob', 'bob', 'standard', 'Mr', 'Baker', 'test@mail.com', '12 Third Street', null),
    ('Charlie', 'charlie', 'standard', 'Mx', 'Candlestickermaker', 'test@mail.com', '12 Third Street', null),
    ('Dania', '5678', 'standard', 'Ms', 'Fletcher', 'test@mail.com', '12 Third Street', null),
    ('sa', 'password', 'standard', 'Mrs', 'User', 'test@mail.com', '14 Oak Court', null),

    ('Sam_Whitley', 'vetpwd', 'vet', 'Samantha', 'Whitley', 'samanthawhitley@vetcare.space', '123 Clinic Court', '1'),
    ('Rhodey', 'vetpwd', 'vet', 'Jonathan', 'Rhodes', 'jonothanrhodes@vetcare.space', '123 Clinic Court', '2'),
    ('Real_Amanda', 'vetpwd', 'vet', 'Amanda', 'Gray', 'amandagray@vetcare.space', '123 Clinic Court', '3'),
    ('bencarter', 'vetpwd', 'vet', 'Benjamin', 'Carter', 'benjamincarter@vetcare.space', '123 Clinic Court', '4'),
    ('Sarah94', 'vetpwd', 'vet', 'Sarah', 'Carter', 'sarahcarter@vetcare.space', '123 Clinic Court', '5'),
    ('Welsnite', 'vetpwd', 'vet', 'Anthony', 'Wells', 'anthonywells@vetcare.space', '123 Clinic Court', '6'),
    ('LauraBennett', 'vetpwd', 'vet', 'Laura', 'Bennett', 'laurabennett@vetcare.space', '123 Clinic Court', '7'),
    ('Chrisss', 'vetpwd', 'vet', 'Christopher', 'Hayes', 'christopherhayes@vetcare.space', '123 Clinic Court', '8');

-- Pets Table Data
INSERT INTO pets ("owner_id", "name", "species", "gender", "age")
VALUES
    ('4', 'Billy', 'Dog', 'Female', '2'),
    ('2', 'Bob Jr', 'Dog', 'Male', '3'),
    ('3', 'Augustus', 'Dog', 'Female', '4'),
    ('2', 'Katrina', 'Dog', 'Male', '5'),
    ('1', 'Dobby', 'Cat', 'Male', '2'),
    ('5', 'TESTVAR', 'Turtle', 'Female', '4'),
    ('5', 'null', 'Rock', 'No', '4'),
    ('5', 'DROP TABLE IF EXISTS *', 'Slug', 'Cant tell', '4');

INSERT INTO appointments ("pet_id","vet_id","clinic_id","user_id","appointment_date")
VALUES
    ('3','1','1','3','2025-05-12 12:00'),
    ('3','1','1','3','2025-05-12 12:30'),
    ('2','3','1','2','2025-05-12 13:00'),
    ('3','3','3','3','2025-05-12 13:30'),
    ('3','3','2','3','2025-05-12 14:00'),
    ('3','3','2','3','2024-09-25 14:00'),
    ('5','3','2','1','2024-09-25 17:30'),
    ('4','3','2','2','2024-09-25 15:00'),
    ('4','3','2','2','2024-09-25 17:00'),
    ('2','3','2','2','2024-09-25 16:00');

INSERT INTO prescriptions ("medicine_name", "dosage", "frequency")
VALUES 
    ('Medicine a', '100mg', 'Twice daily'),
    ('Medicine b', '200mg', 'Once a day'),
    ('Medicine c', '300mg', 'Once every second day'),
    ('Medicine d', '400mg', 'Once every three days');

INSERT INTO treatment_plan ("pet_id", "prescription_id", "date", "description")
VALUES 
    ('1', '1', '2020-01-01', 'Antibiotics & cough suppressants for two weeks or until better'),
    ('2', '1','2020-12-05', 'Vitamin supplements with every second meal'),
    ('2', '2','2021-01-02', 'Visit clinic for subcutaneously fluids, antibiotics for 3 weeks'),
    ('3', '3','2022-01-03', 'Exercise and diet changes immediately, Dirlotapide given fortnightly'),
    ('4', '3','2022-01-03', 'Exercise and diet changes immediately, Dirlotapide given fortnightly');

INSERT INTO active_prescriptions ("prescription_id", "pet_id", "expiration_date")
VALUES
    ('1', '1', '2024-03-14'),  -- Pet 1 (Dobby) owned by User 1 (John), active_prescription_id = 1
    ('2', '2', '2024-03-15'),  -- Pet 2 (Bob) owned by User 2 (Bob), active_prescription_id = 2
    ('3', '2', '2024-03-15'),  -- Pet 2 (Bob) owned by User 2 (Bob), active_prescription_id = 3
    ('3', '3', '2024-03-09'),  -- Pet 3 (Augustus) owned by User 3 (Charlie), active_prescription_id = 4
    ('4', '4', '2024-03-11'),  -- Pet 4 (Katrina) owned by User 2 (Bob), active_prescription_id = 5
    ('1', '5', '2024-03-19');  -- Pet 5 (Billy) owned by User 4 (Dania), active_prescription_id = 6

INSERT INTO delivery_information ("active_prescription_id", "user_id", "delivery_status", "delivery_address", "order_date", "ship_date", "estimated_delivery_date", "actual_delivery_date")
VALUES
    ('1', '1', 'Delivered', '21 Apple Street', '2024-01-10', '2024-01-11', '2024-01-14', '2024-01-14'),  -- Delivered to Pet 1 (Dobby), owned by User 1 (John), active_prescription_id = 1
    ('2', '2', 'Delivered', '22 Banana Street', '2024-01-11', '2024-01-12', '2024-01-15', '2024-01-15'),  -- Delivered to Pet 2 (Bob), owned by User 2 (Bob), active_prescription_id = 2
    ('3', '2', 'Delivered', '22 Banana Street', '2024-01-11', '2024-01-12', '2024-01-15', '2024-01-15'),  -- Delivered to Pet 2 (Bob), owned by User 2 (Bob), active_prescription_id = 3
    ('4', '3', 'Delivered', '23 Cherry Street', '2024-01-05', '2024-01-06', '2024-01-09', '2024-01-09'),  -- Delivered to Pet 3 (Augustus), owned by User 3 (Charlie), active_prescription_id = 4
    ('5', '2', 'Delivered', '1234 Elm Street', '2024-02-01', '2024-02-02', '2024-02-05', '2024-02-05'),  -- Delivered to Pet 4 (Katrina), owned by User 2 (Bob), active_prescription_id = 5
    ('6', '4', 'Delivered', '4321 Oak Avenue', '2024-01-15', '2024-01-16', '2024-01-19', '2024-01-19');  -- Delivered to Pet 5 (Billy), owned by User 4 (Dania), active_prescription_id = 6


INSERT INTO vaccinations ("vaccination_name", "pet_id", "vaccination_date", "description")
VALUES 
    ('Vaccination A', '1','2020-01-01', 'Vaccination description'),
    ('Vaccination B', '1','2020-12-05', 'Vaccination description'),
    ('Vaccination C', '2','2021-01-02', 'Vaccination description'),
    ('Vaccination D', '3','2022-01-03', 'Vaccination description'),
    ('Vaccination E', '3','2022-01-03', 'Vaccination description'),
    ('Vaccination F', '5','2022-01-03', 'Vaccination description');


INSERT INTO medical_records ("pet_id", "clinic_id", "date", "description", "active_prescription_id", "diagnosis", "treatment")
VALUES 
    -- DOBBY
    ('5', '3', '2022-05-26', 'test_description', '1', 'Some Illness', 'Some Treatment'),

    -- BOB JR
    ('2', '3','2020-11-07', 'test_description', '2', 'Healthy', ''),
    ('2', '1','2021-01-02', 'test_description', '3', 'Vomiting', 'Subcutaneously fluids and antibiotics'),

    -- AUGUSTUS
    ('3', '2','2022-01-03', 'test_description', '4', 'Obesity', 'Exercise and diet changes'),

    -- KATRINA
    ('4', '1','2023-01-04', 'test_description', '5', 'Healthy', ''),

    -- BILLY
    ('1', '4','2022-01-03', 'test_description', '6', 'Lack of nutrients', 'Exercise and diet changes');

INSERT INTO vet_access ("pet_id", "vet_id")
VALUES 

    -- sa (user_id 5) is the vet of user Bob (user_id 2)
    -- Bobs pets have ids of 2 and 4
    ('2', '3'),
    ('4', '3'),

    -- Charlie (user id 3) is the vet of John (user_id 1)
    ('5', '2');
    
