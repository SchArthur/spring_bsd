-- mot de passe = root --
INSERT INTO droit (nom) VALUES
("employe") ,
("redacteur") ,
("administrateur");

-- mot de passe = root --
INSERT INTO utilisateur (pseudo, password, droit_id) VALUES
("tata",  "$2a$10$31nhEmGLow2iIug.qqq6RuG3GXv1fo6wXfojXNswxqYqwR8kUJUEm",1) ,
("toto",  "$2a$10$31nhEmGLow2iIug.qqq6RuG3GXv1fo6wXfojXNswxqYqwR8kUJUEm",2) ,
("titi",  "$2a$10$31nhEmGLow2iIug.qqq6RuG3GXv1fo6wXfojXNswxqYqwR8kUJUEm",3);

INSERT INTO priorite (nom) VALUES
("Faible") ,
("Moyen") ,
("Urgent");

INSERT INTO tache (nom, createur_id, valide, priorite_id) VALUES
("apprendre spring", 3 , 1, 1) ,
("faire une pré-evaluation" , 3 , 0, 3) ,
("faire une evaluation" , 3 , 0, 1) ,
("avoir son diplome" , 2, 0, 2);

INSERT INTO tache_utilisateur (utilisateur_id, tache_id) VALUES
(1, 1),
(1 ,2),
(2 ,1),
(2 ,3);
