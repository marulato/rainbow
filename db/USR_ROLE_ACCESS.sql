CREATE TABLE USR_ROLE_ACCESS
(
    ID          BIGINT PRIMARY KEY AUTO_INCREMENT,
    ROLE_ID     BIGINT         NOT NULL,
    MODULE_ID   BIGINT         NOT NULL,
    FUNCTION_ID BIGINT         NOT NULL,
    HTTP_GET    CHAR(1),
    POST        CHAR(1),
    PUT         CHAR(1),
    PATCH       CHAR(1),
    HTTP_DELETE CHAR(1),
    IS_DEFAULT  CHAR(1)     NOT NULL,
    CREATED_AT  DATETIME    NOT NULL,
    CREATED_DM  VARCHAR(64) NOT NULL,
    CREATED_BY  VARCHAR(64) NOT NULL,
    UPDATED_AT  DATETIME    NOT NULL,
    UPDATED_DM  VARCHAR(64) NOT NULL,
    UPDATED_BY  VARCHAR(64) NOT NULL
);

ALTER TABLE USR_ROLE_ACCESS ADD CONSTRAINT USR_ROLE_ACCESS_FK1 FOREIGN KEY (ROLE_ID) REFERENCES USR_ROLE (ID);
ALTER TABLE USR_ROLE_ACCESS ADD INDEX USR_ROLE_ACCESS_IDX1 (MODULE_ID);
ALTER TABLE USR_ROLE_ACCESS ADD INDEX USR_ROLE_ACCESS_IDX2 (FUNCTION_ID);