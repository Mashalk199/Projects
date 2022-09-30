PRAGMA foreign_keys = OFF;
drop table if exists PopulationStatistics;
PRAGMA foreign_keys = ON;

CREATE TABLE PopulationStatistics (
    lga_code16        INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex               CHAR (1) NOT NULL,
    age               TEXT NOT NULL,
    count             INTEGER NOT NULL,
    PRIMARY KEY (lga_code16, indigenous_status, sex, age)
    FOREIGN KEY (lga_code16) REFERENCES LGAs(lga_code16)
);

PRAGMA foreign_keys = OFF;
drop table if exists LabourStatistics;
PRAGMA foreign_keys = ON;

CREATE TABLE LabourStatistics (
    lga_code16        INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex               CHAR (1) NOT NULL,
    labour               TEXT NOT NULL,
    count             INTEGER NOT NULL,
    PRIMARY KEY (lga_code16, indigenous_status, sex, labour)
    FOREIGN KEY (lga_code16) REFERENCES LGAs(lga_code16)
);

PRAGMA foreign_keys = OFF;
drop table if exists SchoolStatistics;
PRAGMA foreign_keys = ON;

CREATE TABLE SchoolStatistics (
    lga_code16        INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex               CHAR (1) NOT NULL,
    yearlevel               TEXT NOT NULL,
    count             INTEGER NOT NULL,
    PRIMARY KEY (lga_code16, indigenous_status, sex, yearlevel)
    FOREIGN KEY (lga_code16) REFERENCES LGAs(lga_code16)
);

PRAGMA foreign_keys = OFF;
drop table if exists HigherStatistics;
PRAGMA foreign_keys = ON;

CREATE TABLE HigherStatistics (
    lga_code16        INTEGER NOT NULL,
    indigenous_status TEXT NOT NULL,
    sex               CHAR (1) NOT NULL,
    qualification               TEXT NOT NULL,
    count             INTEGER NOT NULL,
    PRIMARY KEY (lga_code16, indigenous_status, sex, qualification)
    FOREIGN KEY (lga_code16) REFERENCES LGAs(lga_code16)
);