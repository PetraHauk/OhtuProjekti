# Huomio:
# muista, näin koodin kautta lisätty käyttäjän salasana ei ole hashed!!
päivitä sen kun pääset kirjautuu sisään.

DROP DATABASE IF EXISTS hotelli_db;

# Tietokannan luominen UTF-8-merkistötuella
CREATE DATABASE hotelli_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE hotelli_db;

# Hotelli-taulu
CREATE TABLE hotelli (
    
    hotelli_id INT PRIMARY KEY AUTO_INCREMENT,
     kaupunki VARCHAR(100) CHARACTER SET utf8mb4,
     nimi VARCHAR(100) CHARACTER SET utf8mb4,
     osoite VARCHAR(255) CHARACTER SET utf8mb4,
     puh VARCHAR(20),
     maa VARCHAR(50) CHARACTER SET utf8mb4
);

# Käyttäjä-taulu
CREATE TABLE kayttaja (
      kayttaja_id INT PRIMARY KEY AUTO_INCREMENT,
      etunimi VARCHAR(100) CHARACTER SET utf8mb4,
      sukunimi VARCHAR(100) CHARACTER SET utf8mb4,
      sposti VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
      puh VARCHAR(20),
      rooli VARCHAR(50) CHARACTER SET utf8mb4 NOT NULL,
      salasana VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL
);

# Asiakas-taulu
CREATE TABLE asiakas (
     asiakas_id INT PRIMARY KEY AUTO_INCREMENT,
     etunimi VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
     sukunimi VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
     sposti VARCHAR(100) CHARACTER SET utf8mb4 NOT NULL,
     puh VARCHAR(20),
     henkilo_maara INT(5),
     huomio VARCHAR(500) CHARACTER SET utf8mb4
);

# Huone-taulu
CREATE TABLE huone (
    huone_id INT PRIMARY KEY AUTO_INCREMENT,
    huone_nro INT NOT NULL,
    huone_tyyppi_fi VARCHAR(100) CHARACTER SET utf8mb4,
    huone_tyyppi_en VARCHAR(100) CHARACTER SET utf8mb4,
    huone_tyyppi_ru VARCHAR(100) CHARACTER SET utf8mb4,
    huone_tyyppi_zh VARCHAR(100) CHARACTER SET utf8mb4,
    huone_tila_fi VARCHAR(50) CHARACTER SET utf8mb4,
    huone_tila_en VARCHAR(50) CHARACTER SET utf8mb4,
    huone_tila_ru VARCHAR(50) CHARACTER SET utf8mb4,
    huone_tila_zh VARCHAR(50) CHARACTER SET utf8mb4,
    huone_hinta DECIMAL(10, 2) NOT NULL,
    hotelli_id INT,
    FOREIGN KEY (hotelli_id) REFERENCES hotelli(hotelli_id)
);

# Lasku-taulu
CREATE TABLE lasku (
    lasku_id INT PRIMARY KEY AUTO_INCREMENT,
    maksu_status VARCHAR(50) CHARACTER SET utf8mb4 NOT NULL,
    varaus_muoto VARCHAR(50) CHARACTER SET utf8mb4 NOT NULL,
    valuutta VARCHAR(20) CHARACTER SET utf8mb4,
    asiakas_id INT,
    FOREIGN KEY (asiakas_id) REFERENCES asiakas(asiakas_id)
);

# Huoneen varaus -taulu
CREATE TABLE huone_varaus (
    
      varaus_id INT PRIMARY KEY AUTO_INCREMENT,
      alku_pvm DATE NOT NULL,
      loppu_pvm DATE NOT NULL,
      huone_id INT,
      lasku_id INT,
      FOREIGN KEY (huone_id) REFERENCES huone(huone_id),
      FOREIGN KEY (lasku_id) REFERENCES lasku(lasku_id)
);

# hotelli
INSERT INTO `hotelli` (`kaupunki`, `nimi`, `osoite`, `puh`, `maa`)
VALUES ('Helsinki', 'EkaHoteli', 'parastie 29', '09576345', 'Suomi');

INSERT INTO `hotelli` (`kaupunki`, `nimi`, `osoite`, `puh`, `maa`)
VALUES ('Espoo', 'TokaHoteli', 'toisentie 89', '0265662', 'Suomi');


# kayttaja, tällä tavalla ei voi lisää hashed salasana, ohjelman kautta voi.
INSERT INTO `kayttaja` (`etunimi`, `sukunimi`, `sposti`, `puh`, `rooli`, `salasana`)
VALUES ('Milla','Koskiainen', 'milla.koskiainen@hotelli.com', '03762387168', 'admin', 'milla');

INSERT INTO `kayttaja` (`etunimi`, `sukunimi`, `sposti`, `puh`, `rooli`, `salasana`)
VALUES ('Lisa','Paranen', 'lisa.paranen@hotelli.com', '03762387168', 'normaali', 'lisa');

INSERT INTO `kayttaja` (`etunimi`, `sukunimi`, `sposti`, `puh`, `rooli`, `salasana`)
VALUES ('Aanna','Ansku', 'anna.ansku@hotelli.com', '03762387168', 'normaali', 'anna');

INSERT INTO `kayttaja` (`etunimi`, `sukunimi`, `sposti`, `puh`, `rooli`, `salasana`)
VALUES ('Admin','admin', 'admin@hotelli.com', '03762387168', 'Admin', 'admin');

# asiakas
INSERT INTO `asiakas` (`etunimi`, `sukunimi`, `sposti`, `puh`, `henkilo_maara`, `huomio`)
VALUES ('Matti', 'Meikäläinen', 'matti@sposti.fi', '09123456', 1, 'ei erikoista');

INSERT INTO `asiakas` (`etunimi`, `sukunimi`, `sposti`, `puh`, `henkilo_maara`, `huomio`)
VALUES ('Mia', 'Makki', 'mia@sposti.fi', '0923456', 2, 'Rauhallinen huone');

INSERT INTO `asiakas` (`etunimi`, `sukunimi`, `sposti`, `puh`, `henkilo_maara`, `huomio`)
VALUES ('Anna', 'Ansku', 'anna@sposti.fi', '06872398', 3, 'ei erikoista');

INSERT INTO `asiakas` (`etunimi`, `sukunimi`, `sposti`, `puh`, `henkilo_maara`, `huomio`)
VALUES ('Anna', 'Mallinen', 'anna.mallinen@example.com', '0687473898', 3, 'ei erikoista');




# huone
INSERT INTO `huone` (`huone_nro`, `huone_tyyppi_fi`, `huone_tyyppi_en`, `huone_tyyppi_ru`, `huone_tyyppi_zh`,
                     `huone_tila_fi`, `huone_tila_en`, `huone_tila_ru`, `huone_tila_zh`, `huone_hinta`, `hotelli_id`)
VALUES ('101', 'Yhden hengen huone', 'Single room', 'Одноместный номер', '单人房',
        'Vapaa', 'Free', 'Свободно', '空闲', '100.00', 1);

INSERT INTO `huone` (`huone_nro`, `huone_tyyppi_fi`, `huone_tyyppi_en`, `huone_tyyppi_ru`, `huone_tyyppi_zh`,
                     `huone_tila_fi`, `huone_tila_en`, `huone_tila_ru`, `huone_tila_zh`, `huone_hinta`, `hotelli_id`)
VALUES ('102', 'Kahden hengen huone', 'Double room', 'Двухместный номер', '双人房',
        'Varattu', 'Reserved', 'Забронировано', '已预订', '150.00', 1);

INSERT INTO `huone` (`huone_nro`, `huone_tyyppi_fi`, `huone_tyyppi_en`, `huone_tyyppi_ru`, `huone_tyyppi_zh`,
                     `huone_tila_fi`, `huone_tila_en`, `huone_tila_ru`, `huone_tila_zh`, `huone_hinta`, `hotelli_id`)
VALUES ('202', 'Kolmen hengen huone', 'Triple room', 'Трехместный номер', '三人房',
        'Varattu', 'Reserved', 'Сдержанный', '已预订', '160.00', 1);

INSERT INTO `huone` (`huone_nro`, `huone_tyyppi_fi`, `huone_tyyppi_en`, `huone_tyyppi_ru`, `huone_tyyppi_zh`,
                     `huone_tila_fi`, `huone_tila_en`, `huone_tila_ru`, `huone_tila_zh`, `huone_hinta`, `hotelli_id`)
VALUES ('203', 'Perhehuone', 'Family room', 'Семейный номер', '家庭房', 'Varattu', 'Reserved', 'Сдержанный', '已预订', '260.00', 1);

INSERT INTO `huone` (`huone_nro`, `huone_tyyppi_fi`, `huone_tyyppi_en`, `huone_tyyppi_ru`, `huone_tyyppi_zh`,
                     `huone_tila_fi`, `huone_tila_en`, `huone_tila_ru`, `huone_tila_zh`, `huone_hinta`, `hotelli_id`)
VALUES ('204', 'Sviitti', 'Suite', 'люкс', '套房', 'Varattu', 'Reserved', 'Сдержанный', '已预订', '300.00', 1);

INSERT INTO `huone` (`huone_nro`, `huone_tyyppi_fi`, `huone_tyyppi_en`, `huone_tyyppi_ru`, `huone_tyyppi_zh`,
                     `huone_tila_fi`, `huone_tila_en`, `huone_tila_ru`, `huone_tila_zh`, `huone_hinta`, `hotelli_id`)
VALUES ('205', 'Sviitti', 'Suite', 'люкс', '套房', 'Siivous', 'Cleaning', 'Уборка', '打扫中', '200.00', 1);


# lasku
INSERT INTO `lasku` (`maksu_status`, `varaus_muoto`, `valuutta`, `asiakas_id`)
VALUES ('Maksamatta', 'All_inslusive', 'EUR', 1);

INSERT INTO `lasku` (`maksu_status`, `varaus_muoto`, `valuutta`, `asiakas_id`)
VALUES ('Maksamatta', 'Bed & Breakfast', 'USD', 2);

INSERT INTO `lasku` (`maksu_status`, `varaus_muoto`, `valuutta`, `asiakas_id`)
VALUES ('Maksettu', 'Only room', 'EUR', 3);


# varaus
INSERT INTO `huone_varaus` (`alku_pvm`, `loppu_pvm`, `huone_id`, `lasku_id`)
VALUES ('2024-11-12', '2024-11-15', 1, 1);

INSERT INTO `huone_varaus` (`alku_pvm`, `loppu_pvm`, `huone_id`, `lasku_id`)
VALUES ('2024-12-12', '2024-12-14', 2, 1);

INSERT INTO `huone_varaus` (`alku_pvm`, `loppu_pvm`, `huone_id`, `lasku_id`)
VALUES ('2024-10-12', '2024-10-14', 1, 2);

INSERT INTO `huone_varaus` (`alku_pvm`, `loppu_pvm`, `huone_id`, `lasku_id`)
VALUES ('2024-12-12', '2024-12-14', 3, 2);

INSERT INTO `huone_varaus` (`alku_pvm`, `loppu_pvm`, `huone_id`, `lasku_id`)
VALUES ('2024-12-15', '2024-12-19', 2, 3);




