#----- Test data User
INSERT INTO Person (Username, Password, EMail, Contact_Private, Location_Lat, Location_Long)
    VALUE ('Hans-Peter', 'adminadmin', 'hans-peter@hotmail.de', '015000000001', 51.054084, 7.263641);
INSERT INTO Person (Username, Password, EMail, Contact_Private, Location_Lat, Location_Long)
    VALUE ('TonaldDump', 'adminadmin', 'dump@web.de', '015000000002', 50.991451, 7.128116);
INSERT INTO Person (Username, Password, EMail, Location_Lat, Location_Long)
    VALUE ('HoneyBunny', 'adminadmin', 'carrot@gmail.de', 50.937360, 6.947844);
INSERT INTO Person (Username, Password, EMail, Location_Lat, Location_Long)
    VALUE ('Pumpkin', 'adminadmin', 'happyhalloween@hotmail.de', 51.028465, 7.563614);
INSERT INTO Person (Username, Password, EMail, Contact_Private, Location_Lat, Location_Long)
    VALUE ('John', 'myQueen', 'therealsnow@web.de', '015000000003', 51.131820, 7.602279);
INSERT INTO Person (Username, Password, EMail, Contact_Private, Location_Lat, Location_Long)
    VALUE ('Obi-Wan', 'gitsboni', 'mighty@jedi.de', '015000000004', 51.131820, 7.602279);

#----- Test data
INSERT INTO course(Title, CreationDate, NumberOfStudents, State, Ad_Loc_Lat, Ad_Loc_Long, Usage_Private, CreatorID,
                   ReturnID,
                   ModulesID)
    VALUE ('Coole Mathe Nachhilfe', NOW(), TRUE, 1, 51.028465, 7.563614, TRUE, 2, 2, 3);

INSERT INTO course(Title, CreationDate, NumberOfStudents, State, Ad_Loc_Lat, Ad_Loc_Long, Usage_Private, CreatorID,
                   ReturnID,
                   ModulesID)
    VALUE ('Programmieren für Dummies', NOW(), TRUE, 1, 51.028465, 7.563614, TRUE, 6, 5, 1);

#----- Test data Teaching Location to Course
INSERT INTO loctocourse(LocationID, CourseID) VALUE (2, 1);
INSERT INTO loctocourse(LocationID, CourseID) VALUE (3, 1);

INSERT INTO loctocourse(LocationID, CourseID) VALUE (1, 2);
INSERT INTO loctocourse(LocationID, CourseID) VALUE (2, 2);
INSERT INTO loctocourse(LocationID, CourseID) VALUE (3, 2);
INSERT INTO loctocourse(LocationID, CourseID) VALUE (4, 2);

#----- Test data Person to Module
INSERT INTO persontomodule(PersonID, ModuleID) VALUE (1, 1);
INSERT INTO persontomodule(PersonID, ModuleID) VALUE (1, 2);
INSERT INTO persontomodule(PersonID, ModuleID) VALUE (1, 3);
INSERT INTO persontomodule(PersonID, ModuleID) VALUE (2, 1);
INSERT INTO persontomodule(PersonID, ModuleID) VALUE (2, 3);
INSERT INTO persontomodule(PersonID, ModuleID) VALUE (3, 4);
INSERT INTO persontomodule(PersonID, ModuleID) VALUE (4, 4);
INSERT INTO persontomodule(PersonID, ModuleID) VALUE (5, 1);
INSERT INTO persontomodule(PersonID, ModuleID) VALUE (5, 3);
INSERT INTO persontomodule(PersonID, ModuleID) VALUE (6, 1);

#----- Test data Course to Person
INSERT INTO coursetoperson(CourseID, PersonID, Teaching) VALUE (1, 2, TRUE);
INSERT INTO coursetoperson(CourseID, PersonID, Teaching) VALUE (2, 6, FALSE);
