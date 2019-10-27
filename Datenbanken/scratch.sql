SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS Course;
DROP TABLE IF EXISTS LocToCourse;
DROP TABLE IF EXISTS PersonToModule;
DROP TABLE IF EXISTS CourseToPerson;

DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Module;
DROP TABLE IF EXISTS TeachingLocation;
DROP TABLE IF EXISTS InReturn;
SET FOREIGN_KEY_CHECKS = 1;

COMMIT;

# Studiengang vielleicht noch?
CREATE TABLE Person
(
    Person_ID       INT(10) AUTO_INCREMENT PRIMARY KEY,
    Username        VARCHAR(25)  NOT NULL,
    Password        VARCHAR(25)  NOT NULL,
    EMail           VARCHAR(255) NOT NULL,
    Contact_Private VARCHAR(15)      NULL,
    Location_Lat    DOUBLE      NOT NULL,
    Location_Long   DOUBLE      NOT NULL
);

CREATE TABLE Module
(
    Module_ID   INT(10) AUTO_INCREMENT PRIMARY KEY,
    Title       VARCHAR(255) NOT NULL,
    Description VARCHAR(255) NULL
);

CREATE TABLE TeachingLocation
(
    TLocID      INT(2) AUTO_INCREMENT PRIMARY KEY,
    Title       VARCHAR(255) NOT NULL,
    Description VARCHAR(255) NULL
);

CREATE TABLE InReturn
(
    ReturnID    INT(2) AUTO_INCREMENT PRIMARY KEY,
    Title       VARCHAR(255) NOT NULL,
    Description VARCHAR(255) NULL
);

CREATE TABLE Course
(
    CourseID         INT(10) AUTO_INCREMENT PRIMARY KEY,
    Title            VARCHAR(255) NOT NULL,
    Description      VARCHAR(255) NULL,
    CreationDate     TIMESTAMP, /* Trigger? */
    NumberOfStudents INT(1)       NOT NULL,
    State            BOOLEAN      NOT NULL, /* Status Offen oder Geschlossen */
    Ad_Loc_Lat       INT(30)      NOT NULL,
    Ad_Loc_Long      INT(30)      NOT NULL,
    Usage_Private    BOOLEAN      NOT NULL,
    CreatorID        INT(10)      NOT NULL,
    ReturnID         INT(2)       NOT NULL,
    ModulesID        INT(2)       NOT NULL,


    CONSTRAINT fk_PersonTutID
        FOREIGN KEY (CreatorID)
            REFERENCES Person (Person_ID)
            ON DELETE CASCADE,

    CONSTRAINT fk_InReturnID
        FOREIGN KEY (ReturnID)
            REFERENCES InReturn (ReturnID)
            ON DELETE CASCADE,

    CONSTRAINT fk_ModulesID
        FOREIGN KEY (ModulesID)
            REFERENCES Module (Module_ID)
            ON DELETE CASCADE


);


CREATE TABLE LocToCourse
(
    LocationID INT(2) NOT NULL,
    CourseID   INT(2) NOT NULL,
    PRIMARY KEY (LocationID, CourseID),
    FOREIGN KEY (LocationID) REFERENCES TeachingLocation (TLocID),
    FOREIGN KEY (CourseID)   REFERENCES Course (CourseID)
);

CREATE TABLE PersonToModule
(
    PersonID INT(10) NOT NULL,
    ModuleID INT(2)  NOT NULL,
    PRIMARY KEY (PersonID, ModuleID),
    FOREIGN KEY (PersonID) REFERENCES Person (Person_ID),
    FOREIGN KEY (ModuleID) REFERENCES Module (Module_ID)
);


CREATE TABLE CourseToPerson
(
    CourseID INT(2)  NOT NULL,
    PersonID INT(10) NOT NULL,
    Teaching BOOLEAN NULL, /* Trigger ?*/
    PRIMARY KEY (CourseID, PersonID),
    FOREIGN KEY (CourseID) REFERENCES Course (CourseID),
    FOREIGN KEY (PersonID) REFERENCES Person (Person_ID)

);


#----- Test data Module
INSERT INTO Module(title, description) VALUE ('Programmierung INF', 'Programmierung für AP1, AP2');
INSERT INTO Module(title, description) VALUE ('Programmierung ING', 'Programmierung für Informatik');
INSERT INTO Module(title, description) VALUE ('Mathematik AI, MI, ITM',
                                              'Mathematik für die Studiengänge Informatik, Medieninformatik und IT-Management');
INSERT INTO Module(title, description) VALUE ('Mathematik WI', 'Mathematik für den Studiengang Wirtschaftsinformatik');
INSERT INTO Module(title, description) VALUE ('Mathematik ING',
                                              'Mathematik für die Studiengänge der Ingeneurswissenschaften');

#----- Test data TeachingLocation
INSERT INTO TeachingLocation(Title, Description) VALUE ('Beim Schüler', 'Unterricht findet beim Schüler zu Hause statt');

INSERT INTO TeachingLocation(Title, Description) VALUE ('Beim Lehrer', 'Unterricht findet beim Lehrer zu Hause statt');

INSERT INTO TeachingLocation(Title, Description) VALUE ('Online', 'Unterricht findet über ein Online Medium statt');

INSERT INTO TeachingLocation(Title, Description) VALUE ('Campus Gummersbach', 'Unterricht findet am Campus Gummersbach statt');

#----- Test data InReturns
INSERT INTO InReturn(Title, Description)
    VALUE ('Kaffee', 'Ausgabe eines Kaffee');

INSERT INTO InReturn(Title, Description)
    VALUE ('Mensa', 'Ausgabe eines Mensa Essens nach Wahl');

INSERT INTO InReturn(Title, Description)
    VALUE ('Nachhilfe', 'Nachhilfe in einem Modul nach Wahl');

INSERT INTO InReturn(Title)
    VALUE ('10/Stunde');

INSERT INTO InReturn(Title)
    VALUE ('15/Stunde');

INSERT INTO InReturn(Title)
    VALUE ('20/Stunde');
