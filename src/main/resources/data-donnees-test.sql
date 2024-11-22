INSERT INTO status (designation) VALUES
("disponible"),
("occupé"),
("absent");

-- mot de passe = root --
INSERT INTO utilisateur (email, password, administrateur, status_id) VALUES
("a@a.com",  "$2a$10$31nhEmGLow2iIug.qqq6RuG3GXv1fo6wXfojXNswxqYqwR8kUJUEm",1, 1) ,
("b@b.com",  "$2a$10$31nhEmGLow2iIug.qqq6RuG3GXv1fo6wXfojXNswxqYqwR8kUJUEm",0, 3);

INSERT INTO competence (nom) VALUES
("JAVA"),
("UML"),
("PHP");

INSERT INTO competence_utilisateur(utilisateur_id, competence_id) VALUES
(1,2),
(1,3),
(2,1),
(2,2);
