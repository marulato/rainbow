CREATE TABLE CM_MODULE
(
    ID          INT PRIMARY KEY AUTO_INCREMENT,
    CODE        VARCHAR(32)  NOT NULL UNIQUE,
    NAME        VARCHAR(100) NOT NULL,
    DESCRIPTION VARCHAR(200),
    CREATED_AT  DATETIME     NOT NULL,
    CREATED_DM  VARCHAR(64)  NOT NULL,
    CREATED_BY  VARCHAR(64)  NOT NULL,
    UPDATED_AT  DATETIME     NOT NULL,
    UPDATED_DM  VARCHAR(64)  NOT NULL,
    UPDATED_BY  VARCHAR(64)  NOT NULL
);