CREATE TABLE IF NOT EXISTS "Wedstrijd" (
	"WedstrijdId"	INTEGER NOT NULL UNIQUE,
	"Plaats"	TEXT NOT NULL,
	"Afstand"	INTEGER NOT NULL CHECK("Afstand" > 0),
	"InschrijvingsGeld"	INTEGER NOT NULL,
	"Datum"	TEXT NOT NULL,
	"BeginUur"	INTEGER NOT NULL CHECK("BeginUur" > 0),
	"Gelopen"   INTEGER NOT NULL,
	PRIMARY KEY("WedstrijdId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "WedstrijdMedewerker" (
	"WedstrijdMedewerkerId"	INTEGER NOT NULL UNIQUE,
	"WedstrijdId"	INTEGER NOT NULL,
	"MedewerkerId"	INTEGER NOT NULL,
	"BeginUur"	INTEGER NOT NULL CHECK("BeginUur" > 0),
	"EindUur"	INTEGER NOT NULL CHECK("EindUur" > 0),
	"Positie"	TEXT NOT NULL,
	PRIMARY KEY("WedstrijdMedewerkerId" AUTOINCREMENT),
	FOREIGN KEY("MedewerkerId") REFERENCES "Medewerker"("MedewerkerId"),
	FOREIGN KEY("WedstrijdId") REFERENCES "Wedstrijd"("WedstrijdId")
);
CREATE TABLE IF NOT EXISTS "Medewerker" (
	"MedewerkerId"	INTEGER NOT NULL UNIQUE,
	"Naam"	TEXT NOT NULL,
	"Functie"	TEXT NOT NULL,
	"Leeftijd"	INTEGER NOT NULL CHECK("Leeftijd" >= 15),
	"Uurloon"	INTEGER NOT NULL CHECK("Uurloon" >= 10),
	"GeldTegoed"	INTEGER NOT NULL,
	PRIMARY KEY("MedewerkerId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Etappe" (
	"EtappeId"	INTEGER NOT NULL UNIQUE,
	"WedstrijdId"	INTEGER NOT NULL,
	"Afstand"	INTEGER NOT NULL CHECK("Afstand" > 0),
	"BeginKm" INTEGER NOT NULL CHECK("BeginKm" >= 0),
	PRIMARY KEY("EtappeId" AUTOINCREMENT),
	FOREIGN KEY("WedstrijdId") REFERENCES "Wedstrijd"("WedstrijdId")
);
CREATE TABLE IF NOT EXISTS "Loper" (
	"LoperId"	INTEGER NOT NULL UNIQUE,
	"Naam"	TEXT NOT NULL,
	"Leeftijd"	INTEGER NOT NULL CHECK("Leeftijd" >= 3),
	"Geslacht"	TEXT NOT NULL,
	"Gewicht"	INTEGER NOT NULL CHECK("Gewicht" >= 30),
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
	"Tijd"	INTEGER NOT NULL CHECK("Tijd" >= 0),
	PRIMARY KEY("EtappeLoperId" AUTOINCREMENT),
	FOREIGN KEY("LoperId") REFERENCES "Loper"("LoperId"),
	FOREIGN KEY("EtappeId") REFERENCES "Etappe"("EtappeId")
);

