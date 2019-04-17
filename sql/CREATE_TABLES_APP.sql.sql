-- -----------------------------------------------------------------------------
-- LITRO DE LUZ RADIUS 2 ADMIN
-- TABLES APP
-- Version 1.0.0
-- Abr 3, 2019
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- Tabla zone
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS zone;

CREATE TABLE zone (
    id                      INT                 NOT NULL        AUTO_INCREMENT,
    name                    VARCHAR(64)         NOT NULL,

    PRIMARY KEY (id)
);

-- -----------------------------------------------------------------------------
-- Tabla sponsor
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS sponsor;

CREATE TABLE sponsor (
    id          INT             NOT NULL        AUTO_INCREMENT,
    name        VARCHAR(64)     NOT NULL,
    contact     VARCHAR(64)     DEFAULT '',
    phone       VARCHAR(32)     DEFAULT '',
    email       VARCHAR(64)     DEFAULT '',

    PRIMARY KEY (id)
);

-- -----------------------------------------------------------------------------
-- Tabla campaign
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS campaign;

CREATE TABLE campaign (
    id              INT             NOT NULL        AUTO_INCREMENT,
    description     VARCHAR(128)    DEFAULT '',
    banner_1        VARCHAR(128)    NOT NULL,
    banner_2        VARCHAR(128)    NOT NULL,
    start_date      DATE            DEFAULT NULL,
    end_date        DATE            DEFAULT NULL,
    sponsor         INT             NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (sponsor) REFERENCES sponsor (id)
);

-- -----------------------------------------------------------------------------
-- Tabla campaign_x_zone
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS campaign_x_zone;

CREATE TABLE campaign_x_zone (
    id          INT     NOT NULL        AUTO_INCREMENT,
    campaign    INT     NOT NULL,
    zone        INT     NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (campaign) REFERENCES campaign (id) ON DELETE CASCADE,
    FOREIGN KEY (zone) REFERENCES zone (id) ON DELETE CASCADE
);
-- -----------------------------------------------------------------------------
-- Tabla survey
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS survey;

CREATE TABLE survey (
    id              INT                 NOT NULL        AUTO_INCREMENT,
    description     VARCHAR(128)        DEFAULT '',
    campaign        INT                 NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (campaign) REFERENCES campaign (id) ON DELETE CASCADE
);

-- -----------------------------------------------------------------------------
-- Tabla question
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS question;

CREATE TABLE question (
    id          INT                 NOT NULL        AUTO_INCREMENT,
    type        VARCHAR(16)         DEFAULT '',
    question    VARCHAR(512)        NOT NULL,
    options     VARCHAR(4096)       NOT NULL,
    survey      INT                 NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (survey) REFERENCES survey (id) ON DELETE CASCADE
);

-- -----------------------------------------------------------------------------
-- Tabla answer
-- -----------------------------------------------------------------------------
DROP TABLE IF EXISTS answer;

CREATE TABLE answer (
    id              INT             NOT NULL        AUTO_INCREMENT,
    response        VARCHAR(512)    NOT NULL,
    answer_date     DATE            DEFAULT NULL,
    question        INT             NOT NULL,
    hotspot         INT             NOT NULL,
  
    PRIMARY KEY (id),
    FOREIGN KEY (question) REFERENCES question (id) ON DELETE CASCADE
);

