DROP TABLE IF EXISTS salary;
DROP TABLE IF EXISTS persons;

CREATE TABLE IF NOT EXISTS persons (
   id integer PRIMARY KEY AUTOINCREMENT NOT NULL,
   name varchar(256) NOT NULL,
   CONSTRAINT c_person_uniq_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS salary (
   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
   person_id INTEGER not null,
   amount NUMERIC not null,
   FOREIGN KEY(person_id) REFERENCES persons(id)
);


INSERT INTO "persons" ("id", "name") VALUES (1, 'ivan');
INSERT INTO "persons" ("id", "name") VALUES (2, 'max');
INSERT INTO "persons" ("id", "name") VALUES (3, 'oleg');

INSERT INTO "salary"("person_id", "amount") VALUES (1, 100);
INSERT INTO "salary"("person_id", "amount") VALUES (2, 200);
INSERT INTO "salary"("person_id", "amount") VALUES (3, 300);