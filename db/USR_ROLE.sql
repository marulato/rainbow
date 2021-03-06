CREATE TABLE USR_ROLE
(
    ID          BIGINT PRIMARY KEY AUTO_INCREMENT,
    CODE        VARCHAR(16)  NOT NULL UNIQUE,
    NAME        VARCHAR(100) NOT NULL,
    DESCRIPTION VARCHAR(200),
    IS_SYSTEM   CHAR(1) NOT NULL,
    CREATED_AT  DATETIME     NOT NULL,
    CREATED_DM  VARCHAR(64)  NOT NULL,
    CREATED_BY  VARCHAR(64)  NOT NULL,
    UPDATED_AT  DATETIME     NOT NULL,
    UPDATED_DM  VARCHAR(64)  NOT NULL,
    UPDATED_BY  VARCHAR(64)  NOT NULL
);

INSERT INTO USR_ROLE
(ID, CODE, NAME, DESCRIPTION, CREATED_AT, CREATED_DM, CREATED_BY, UPDATED_AT, UPDATED_DM, UPDATED_BY, IS_SYSTEM)
VALUES(NULL, 'SPADMIN', 'Super Admin', NULL, NOW(), 'intranet', 'SYSTEM', NOW(), 'intranet', 'SYSTEM', 'Y');
