DROP DATABASE IF EXISTS MoneyApp;
CREATE DATABASE MoneyApp CHARACTER SET utf8mb4 COLLATE utf8mb4_hungarian_ci;

USE MoneyApp;

CREATE TABLE ACCOUNTS(ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(40), BALANCE INT);
CREATE TABLE EXPENDITURES(ID INT PRIMARY KEY AUTO_INCREMENT, ACCOUNT VARCHAR(40), AMOUNT INT, CATEGORY VARCHAR(40), DATE VARCHAR(40));
CREATE TABLE INCOMES(ID INT PRIMARY KEY AUTO_INCREMENT, ACCOUNT VARCHAR(40), AMOUNT INT, CATEGORY VARCHAR(40), DATE VARCHAR(40));
CREATE TABLE ALERTS(ID INT PRIMARY KEY AUTO_INCREMENT, MSG VARCHAR(60), DATE VARCHAR(40));

INSERT INTO ACCOUNTS (NAME, BALANCE) VALUES ("OTP", 3000);
INSERT INTO EXPENDITURES (ACCOUNT, AMOUNT, CATEGORY, DATE) VALUES ("OTP", 200, "test", CURRENT_DATE());
INSERT INTO INCOMES (ACCOUNT, AMOUNT, CATEGORY, DATE) VALUES ("OTP", 200, "test", CURRENT_DATE());
INSERT INTO ALERTS (MSG, DATE) VALUES ("TEST ALERT!", CURRENT_DATE());

DROP TRIGGER IF EXISTS alert_trigger;
DELIMITER //
CREATE TRIGGER alert_trigger BEFORE UPDATE ON ACCOUNTS FOR EACH ROW
BEGIN
    IF (NEW.BALANCE <= 0) THEN
        INSERT INTO ALERTS (MSG, DATE)
            VALUES ("WARNING! Your account goes minus!", CURRENT_DATE());
    END IF;
END;//
DELIMITER ;

/*DROP PROCEDURE IF EXISTS HotBackup
CREATE PROCEDURE HotBackup()
BEGIN
    SET @fileName = "E:/Shared/Cloud/SanDisk/Prog/My prog files/TransactionMonitor/";
    SET @dbName = "MoneyApp";
    SET @fileDate = CONVERT(CURRENT_DATE(), VARCHAR(20));
    SET @fileName = @fileName + @dbName + '-' + @fileDate + ".bak";
    BACKUP DATABASE @dbName TO DISK = @fileName;
END;*/
