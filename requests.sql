CREATE TABLE rateLists (
    idRateList INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    base VARCHAR(256) NOT NULL,
    retDate DATE NOT NULL,
    r_Id INTEGER
                      );

CREATE TABLE rates (
    idRate INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(256) NOT NULL,
    value DOUBLE NOT NULL
);

ALTER TABLE rateLists
    ADD FOREIGN KEY (r_Id) REFERENCES rates (idRate);

INSERT INTO rates(name, value) VALUES ('EUR', '1.000');

SELECT * FROM rates;

SELECT name FROM rates WHERE name = '';

