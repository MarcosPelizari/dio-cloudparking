CREATE TABLE PARKING (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    LICENSE VARCHAR(255) NOT NULL,
    STATE VARCHAR(255) NOT NULL,
    MODEL VARCHAR(255) NOT NULL,
    COLOR VARCHAR(255) NOT NULL,
    ENTRY_DATE TIMESTAMP NULL,
    EXIT_DATE TIMESTAMP NULL,
    BILL DECIMAL(10, 2) NOT NULL
);