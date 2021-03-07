CREATE TABLE USR_USER
(
    ID                    INT PRIMARY KEY AUTO_INCREMENT,
    USERNAME              VARCHAR(64)  NOT NULL UNIQUE,
    DOMAIN                VARCHAR(32)  NOT NULL,
    DISPLAY_NAME          VARCHAR(64)  NOT NULL,
    PASSWORD              VARCHAR(512) NOT NULL,
    STATUS                CHAR(1),
    EFFECTIVE_DATE        DATE,
    EXPIRATION_DATE       DATE,
    IS_FIRST_LOGIN        CHAR(1),
    LOGIN_FAILED_TIMES    INT,
    LAST_LOGIN_SUCCESS_DT DATETIME,
    LAST_LOGIN_ATTEMPT_DT DATETIME,
    CREATED_AT            DATETIME     NOT NULL,
    CREATED_DM            VARCHAR(64)  NOT NULL,
    CREATED_BY            VARCHAR(64)  NOT NULL,
    UPDATED_AT            DATETIME     NOT NULL,
    UPDATED_DM            VARCHAR(64)  NOT NULL,
    UPDATED_BY            VARCHAR(64)  NOT NULL
);