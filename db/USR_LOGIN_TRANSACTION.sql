CREATE TABLE USR_LOGIN_TRANSACTION
(
    ID           BIGINT PRIMARY KEY AUTO_INCREMENT,
    USER_ID      BIGINT         NOT NULL,
    USERNAME     VARCHAR(64),
    STATUS       CHAR(1),
    LOGIN_STATUS CHAR(1),
    IP           VARCHAR(64),
    USER_AGENT   VARCHAR(1000),
    CREATED_AT   DATETIME    NOT NULL,
    CREATED_DM   VARCHAR(64) NOT NULL,
    CREATED_BY   VARCHAR(64) NOT NULL,
    UPDATED_AT   DATETIME    NOT NULL,
    UPDATED_DM   VARCHAR(64) NOT NULL,
    UPDATED_BY   VARCHAR(64) NOT NULL
);

ALTER TABLE USR_LOGIN_TRANSACTION ADD CONSTRAINT USR_LOGIN_TRANSACTION_FK1 FOREIGN KEY (USER_ID) REFERENCES USR_USER (ID);