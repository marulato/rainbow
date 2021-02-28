CREATE TABLE CM_EMAIL_TRANSACTION
(
    ID            BIGINT PRIMARY KEY AUTO_INCREMENT,
    HOST          VARCHAR(64) NOT NULL,
    SEND_FROM     VARCHAR(64)   NOT NULL,
    RECIPIENTS    VARCHAR(4000) NOT NULL,
    CC            VARCHAR(4000),
    SUBJECT       VARCHAR(160)  NOT NULL,
    CONTENT       MEDIUMBLOB    NOT NULL,
    ATTACHMENT    JSON,
    STATUS        VARCHAR(6)    NOT NULL,
    STATUS_DESC   VARCHAR(1000),
    TRIGGERED_BY  VARCHAR(64),
    REFERENCE_TBL VARCHAR(64),
    REFERENCE_ID  BIGINT,
    CREATED_AT    DATETIME      NOT NULL,
    CREATED_DM    VARCHAR(64)   NOT NULL,
    CREATED_BY    VARCHAR(64)   NOT NULL,
    UPDATED_AT    DATETIME      NOT NULL,
    UPDATED_DM    VARCHAR(64)   NOT NULL,
    UPDATED_BY    VARCHAR(64)   NOT NULL
);