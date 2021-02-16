--alter session set "_oracle_script"=true;
CREATE USER Jordan IDENTIFIED BY jordan;
GRANT CREATE SESSION TO Jordan;
GRANT CREATE TABLE TO Jordan;

GRANT UNLIMITED TABLESPACE TO Jordan;

    CREATE TABLE Jordan.CAR (
           "ID" NUMBER PRIMARY KEY,
           "BRAND" VARCHAR2(255),
           "COLOR" VARCHAR2(255),
           "FUELCAPACITY" NUMBER,
           "SEATS" NUMBER
    );

COMMIT;