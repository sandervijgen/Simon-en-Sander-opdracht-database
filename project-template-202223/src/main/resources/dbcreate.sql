CREATE TABLE IF NOT EXISTS "Wedstrijd" (
	"WedstrijdId"	INTEGER NOT NULL UNIQUE,
	"Plaats"	TEXT NOT NULL,
	"Afstand"	INTEGER NOT NULL,
	"InschrijvingsGeld"	INTEGER NOT NULL,
	"Datum"	TEXT NOT NULL,
	"BeginUur"	INTEGER NOT NULL,
	"Gelopen"   INTEGER NOT NULL,
	PRIMARY KEY("WedstrijdId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "WedstrijdMedewerker" (
	"WedstrijdMedewerkerId"	INTEGER NOT NULL UNIQUE,
	"WedstrijdId"	INTEGER NOT NULL,
	"MedewerkerId"	INTEGER NOT NULL,
	"BeginUur"	INTEGER NOT NULL,
	"Einduur"	INTEGER NOT NULL,
	"Positie"	TEXT NOT NULL,
	PRIMARY KEY("WedstrijdMedewerkerId" AUTOINCREMENT),
	FOREIGN KEY("MedewerkerId") REFERENCES "Medewerker"("MedewerkerId"),
	FOREIGN KEY("WedstrijdId") REFERENCES "Wedstrijd"("WedstrijdId")
);
CREATE TABLE IF NOT EXISTS "Medewerker" (
	"MedewerkerId"	INTEGER NOT NULL UNIQUE,
	"Naam"	TEXT NOT NULL,
	"Functie"	TEXT NOT NULL,
	"Leeftijd"	INTEGER NOT NULL,
	"Uurloon"	INTEGER NOT NULL,
	PRIMARY KEY("MedewerkerId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Etappe" (
	"EtappeId"	INTEGER NOT NULL UNIQUE,
	"WedstrijdId"	INTEGER NOT NULL,
	"Afstand"	INTEGER NOT NULL,
	"BeginKm" INTEGER NOT NULL,
	PRIMARY KEY("EtappeId" AUTOINCREMENT),
	FOREIGN KEY("WedstrijdId") REFERENCES "Wedstrijd"("WedstrijdId")
);
CREATE TABLE IF NOT EXISTS "Loper" (
	"LoperId"	INTEGER NOT NULL UNIQUE,
	"Naam"	TEXT NOT NULL,
	"Leeftijd"	INTEGER NOT NULL,
	"Geslacht"	TEXT NOT NULL,
	"Gewicht"	INTEGER NOT NULL,
	"Fysiek"	TEXT NOT NULL,
	"Club"	TEXT NOT NULL,
	"ContactMedewerkerId"	INTEGER NOT NULL,
	"Punten"	INTEGER NOT NULL,
	PRIMARY KEY("LoperId" AUTOINCREMENT),
	FOREIGN KEY("ContactMedewerkerId") REFERENCES "Medewerker"("MedewerkerId")
);
CREATE TABLE IF NOT EXISTS "EtappeLoper" (
	"EtappeLoperId"	INTEGER NOT NULL UNIQUE,
	"LoperId"	INTEGER NOT NULL,
	"EtappeId"	INTEGER NOT NULL,
	"Tijd"	INTEGER NOT NULL,
	PRIMARY KEY("EtappeLoperId" AUTOINCREMENT),
	FOREIGN KEY("LoperId") REFERENCES "Loper"("LoperId"),
	FOREIGN KEY("EtappeId") REFERENCES "Etappe"("EtappeId")
);

