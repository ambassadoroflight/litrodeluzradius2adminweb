-- -----------------------------------------------------------------------------
-- LITRO DE LUZ RADIUS 2 ADMIN
-- TABLES APP
-- Version 1.0.0
-- Abr 3, 2019
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- Tabla survey
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS survey;

CREATE TABLE survey (
    id              INT(11)             NOT NULL        AUTO_INCREMENT,
    description     VARCHAR(128)        DEFAULT '',

    PRIMARY KEY(id)
);

-- -----------------------------------------------------------------------------
-- Tabla question
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS question;

CREATE TABLE question (
    id          INT(11)             NOT NULL        AUTO_INCREMENT,
    survey      INT(11)             NOT NULL,
    type        CHAR(16)            DEFAULT '',
    question    VARCHAR(512)        NOT NULL,
    options     VARCHAR(2048)       DEFAULT '',

    PRIMARY KEY(id)
);

-- -----------------------------------------------------------------------------
-- Tabla answer
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS answer;

CREATE TABLE answer (
    id              INT(11)             NOT NULL        AUTO_INCREMENT,
    question        INT(11)             NOT NULL,
    response        VARCHAR(512)        NOT NULL,
    answer_date     DATETIME,
    answer_ddate    DATE,
    hotspot         INT                 NOT NULL,

    PRIMARY KEY(id)
);