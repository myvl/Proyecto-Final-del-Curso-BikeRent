-- Permitir el uso completo de la base de datos
GRANT ALL ON DATABASE bicycle_rental TO developer;


 -- Permitir uso de esquemas y conexión
 GRANT USAGE ON SCHEMA public TO developer;

 -- Otorgar privilegios sobre todas las tablas
 GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO developer;

 -- Otorgar privilegios sobre todas las secuencias (necesario para columnas SERIAL)
 GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA public TO developer;

 -- Para que futuros objetos también estén cubiertos automáticamente
ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO developer;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO developer;


CREATE DATABASE bicycle_rental
WITH OWNER = developer
TEMPLATE = template1
ENCODING = 'UTF8'
LOCALE_PROVIDER = 'libc'
TABLESPACE = pg_default
CONNECTION LIMIT = -1;
IS_TEMPLATE = False;

GRANT ALL ON DATABASE bicycle_rental TO developer WITH GRANT OPTION;


-- Tabla de referencia: Métodos de pago
CREATE TABLE Ref_Payment_Methods (
    Payment_Method_Code VARCHAR(10) PRIMARY KEY,
    Payment_Method_Description VARCHAR(50) NOT NULL
);

-- Tabla de referencia: Estados de pago
CREATE TABLE Ref_Payment_status_code (
    Payment_status_Code VARCHAR(10) PRIMARY KEY,
    Payment_status_Description VARCHAR(50) NOT NULL
);

-- Tabla de tiendas
CREATE TABLE Multi_Shop (
    Multi_Shop_ID SERIAL PRIMARY KEY,
    Contact_Name VARCHAR(100) NOT NULL,
    Location_Name VARCHAR(100) NOT NULL,
    Email_Address VARCHAR(100) CHECK (Email_Address ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
    Phone_Number VARCHAR(20),
    Address TEXT NOT NULL,
    Other_Details TEXT
);

-- Tabla de bicicletas
CREATE TABLE Bicycles (
    Bicycle_ID SERIAL PRIMARY KEY,
    Bicycle_Details TEXT NOT NULL
);

-- Tabla de clientes
CREATE TABLE Renters (
    Renter_ID SERIAL PRIMARY KEY,
    Registration_Date_Time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Last_Rental_Date_Time TIMESTAMP,
    Other_Details TEXT
);

-- Tabla de tarifas
CREATE TABLE Rental_Rates (
    Rental_Rates_ID SERIAL PRIMARY KEY,
    Daily_Rate DECIMAL(10,2) NOT NULL CHECK (Daily_Rate > 0),
    Hourly_Rate DECIMAL(10,2) NOT NULL CHECK (Hourly_Rate > 0)
);

-- Tabla de métodos de pago de clientes
CREATE TABLE Renters_Payment_Methods (
    Renter_Payment_Method_ID SERIAL PRIMARY KEY,
    Renter_ID INTEGER NOT NULL REFERENCES Renters(Renter_ID),
    Payment_Method_Code VARCHAR(10) NOT NULL REFERENCES Ref_Payment_Methods(Payment_Method_Code),
    Card_Details VARCHAR(100),
    Other_Details TEXT,
    CONSTRAINT unique_renter_payment UNIQUE(Renter_ID, Payment_Method_Code, Card_Details)
);

-- Tabla de bicicletas en tiendas
CREATE TABLE Bicycles_in_Shops (
    Multi_Shop_ID INTEGER NOT NULL REFERENCES Multi_Shop(Multi_Shop_ID),
    Bicycle_ID INTEGER NOT NULL REFERENCES Bicycles(Bicycle_ID),
    DateTime_In TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DateTime_Out TIMESTAMP,
    Other_Details TEXT,
    PRIMARY KEY (Multi_Shop_ID, Bicycle_ID, DateTime_In),
    CHECK (DateTime_Out IS NULL OR DateTime_Out > DateTime_In)
);

-- Tabla de alquileres
CREATE TABLE Rentals (
    Rental_ID SERIAL PRIMARY KEY,
    Bicycle_ID INTEGER NOT NULL REFERENCES Bicycles(Bicycle_ID),
    Payment_status_Code VARCHAR(10) NOT NULL REFERENCES Ref_Payment_status_code(Payment_status_Code),
    Rental_Rates_ID INTEGER NOT NULL REFERENCES Rental_Rates(Rental_Rates_ID),
    Renter_ID INTEGER NOT NULL REFERENCES Renters(Renter_ID),
    Renter_Payment_Method_ID INTEGER NOT NULL REFERENCES Renters_Payment_Methods(Renter_Payment_Method_ID),
    All_Day_Rental_YN CHAR(1) NOT NULL CHECK (All_Day_Rental_YN IN ('Y', 'N')),
    Booked_Start_Date_Time TIMESTAMP NOT NULL,
    Booked_End_Date_Time TIMESTAMP NOT NULL,
    Actual_Start_Date_Time TIMESTAMP,
    Actual_End_Date_Time TIMESTAMP,
    Rental_Payment_Due DECIMAL(10,2) NOT NULL CHECK (Rental_Payment_Due >= 0),
    Rental_Payment_Made DECIMAL(10,2) DEFAULT 0 CHECK (Rental_Payment_Made >= 0),
    Other_Details TEXT,
    CHECK (Booked_End_Date_Time > Booked_Start_Date_Time),
    CHECK (Actual_End_Date_Time IS NULL OR Actual_End_Date_Time > Actual_Start_Date_Time)
);

-- Insertar métodos de pago
INSERT INTO Ref_Payment_Methods (Payment_Method_Code, Payment_Method_Description) VALUES
('CASH', 'Efectivo'),
('VISA', 'Tarjeta Visa'),
('MC', 'MasterCard'),
('AMEX', 'American Express'),
('PAYPAL', 'PayPal');

-- Insertar estados de pago
INSERT INTO Ref_Payment_status_code (Payment_status_Code, Payment_status_Description) VALUES
('PEND', 'Pendiente'),
('PAID', 'Pagado'),
('FAIL', 'Fallido'),
('REFND', 'Reembolsado'),
('PART', 'Pago Parcial');

-- Insertar tiendas
INSERT INTO Multi_Shop (Contact_Name, Location_Name, Email_Address, Phone_Number, Address) VALUES
('Juan Pérez', 'Tienda Centro', 'centro@multishop.com', '555-1001', 'Av. Principal 123, Centro'),
('María Gómez', 'Tienda Norte', 'norte@multishop.com', '555-1002', 'Calle Norte 456, Zona Norte'),
('Carlos Ruiz', 'Tienda Sur', 'sur@multishop.com', '555-1003', 'Boulevard Sur 789, Zona Sur');

-- Insertar bicicletas
INSERT INTO Bicycles (Bicycle_Details) VALUES
('Bicicleta de montaña, roja, talla M'),
('Bicicleta urbana, azul, talla L'),
('Bicicleta de carretera, negra, talla S'),
('Bicicleta híbrida, verde, talla M'),
('Bicicleta plegable, gris, talla única');

-- Insertar tarifas
INSERT INTO Rental_Rates (Daily_Rate, Hourly_Rate) VALUES
(25.00, 5.00),
(30.00, 6.00),
(20.00, 4.00),
(35.00, 7.00),
(40.00, 8.00);

-- Insertar clientes
INSERT INTO Renters (Registration_Date_Time, Last_Rental_Date_Time) VALUES
('2024-01-15 10:00:00', '2024-04-20 18:30:00'),
('2024-02-10 14:30:00', '2024-04-22 16:45:00'),
('2024-03-05 09:15:00', NULL);

-- Insertar métodos de pago de clientes
INSERT INTO Renters_Payment_Methods (Renter_ID, Payment_Method_Code, Card_Details) VALUES
(1, 'VISA', '****-****-****-1234'),
(1, 'CASH', NULL),
(2, 'MC', '****-****-****-5678'),
(3, 'AMEX', '****-****-****-9012'),
(3, 'PAYPAL', 'cliente3@email.com');

-- Insertar bicicletas en tiendas
INSERT INTO Bicycles_in_Shops (Multi_Shop_ID, Bicycle_ID, DateTime_In) VALUES
(1, 1, '2024-04-01 09:00:00'),
(1, 2, '2024-04-01 09:00:00'),
(2, 3, '2024-04-02 10:00:00'),
(2, 4, '2024-04-02 10:00:00'),
(3, 5, '2024-04-03 11:00:00');

-- Insertar alquileres
INSERT INTO Rentals (
    Bicycle_ID, Payment_status_Code, Rental_Rates_ID, Renter_ID, Renter_Payment_Method_ID,
    All_Day_Rental_YN, Booked_Start_Date_Time, Booked_End_Date_Time,
    Actual_Start_Date_Time, Actual_End_Date_Time, Rental_Payment_Due, Rental_Payment_Made
) VALUES
(1, 'PAID', 1, 1, 1, 'Y', '2024-04-10 09:00:00', '2024-04-10 18:00:00', '2024-04-10 09:15:00', '2024-04-10 17:45:00', 25.00, 25.00),
(2, 'PAID', 2, 2, 3, 'N', '2024-04-12 14:00:00', '2024-04-12 16:00:00', '2024-04-12 14:05:00', '2024-04-12 15:50:00', 12.00, 12.00),
(3, 'PEND', 3, 3, 4, 'Y', '2024-04-15 10:00:00', '2024-04-15 19:00:00', '2024-04-15 10:10:00', NULL, 20.00, 10.00);


