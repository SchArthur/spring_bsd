INSERT INTO status (designation) VALUES
("disponible"),
("occupé"),
("absent");

INSERT INTO utilisateur (email, password, administrateur, status_id) VALUES
("a@a.com", 1, "root", 1) ,
("b@b.com", 0,  "azerty", 3);

INSERT INTO competence (nom) VALUES
("JAVA"),
("UML"),
("PHP");

INSERT INTO competence_utilisateur(utilisateur_id, competence_id) VALUES
(1,2),
(1,3),
(2,1),
(2,2);
