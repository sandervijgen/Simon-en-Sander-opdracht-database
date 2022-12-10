# Beschrijving DDL schema van loperdatabase “De Vrolijke Zweters”

## Entiteit 1: Wedstrijd

De eerste entiteit in het schema die we zullen bespreken is wedstrijd. Deze bevat de properties wedstrijd id, plaats, afstand,inschrijvingsgeld, datum en beginuur. De wedstrijd id is een unieke integer per wedstrijd, er is gekozen voor een integer omdat er zo auto-increment op kan worden toegespast, waardoor fouten sterk verminderd worden en efficienty omhoog gaat. Voor plaats is er vanzelfsprekend ook een string gekozen, String's zijn veiliger omdat de verschillende interpretatie van bv de punt en de komma in verschillende landen voor problemen kan zorgen. Afstand is geïmplementeerd als een int, zo kunnen de wedstrijden makkelijk gesorteerd worden op afstand. Inschrijvingsgeld en begin uur zijn beide integers, hier worden namelijk nog bewerkingen mee gedaan als er bv twee inschrijvingen moeten worden opgetelt of de duur van de loop moet worden berekend. Datum is in de vorm van een string dit is gemakkelijker en er moet hier niet mee berekend worden.

## Entiteit 2: Medewerker

Naast medewerker Id bevat de entiteit medewerker ook nog andere properties zoals naam, functie en leeftijd. Naam en functie zijn strings. Voor leeftijd hebben we een int gekozen omdat hier mogelijk wel berekeningen mee zullen gebeuren, zoals rangschrikken. Hiernaast heeft het ook nog de propertie uurloon, waarmee hun vergoedingen voor de shiften zal worden berekend.

## Entiteit 3: Etappe

De properties van de entiteit etappe zijn etappe Id (Int), wedstrijd id (Int), afstand (int). Via de wedstrijd id wordt een etappe gelinkt aan een wedstrijd.

## Entiteit 4: Loper

Een loper heeft als property een loper id (Int) deze zal ook dienst doen als zijn uniek loopnummer waardoor het makkelijk is om deze loper op te zoeken. Hiernaast houden we ook zijn naam bij als string, deze is echter niet altijd uniek dus zal zijn id de primaire key gebruikt worden. Dit is ook het geval bij de id’s van de andere entiteiten. Hiernaast zijn leeftijd (int), geslacht (String), Gewicht (int), Fysieke toestand (String) en Club (String) ook properties binnen loper. Leeftijd en gewicht zijn integers omdat op basis van deze een rankschikking of een selectie binnen een bepaald bereik kan gemaakt worden. Bij de andere properties wordt enkel gekeken naar de waarde (bv is vrouw?) en is er dus een mogelijkheid om deze als string op te slaan. Hiernaast heeft hij nog punten (int) die hij kan verdienen per etappe waarop we later kunnen rangschikken in het algemene klassement.

## Relatie 1: Veel op veel tussen wedstrijd en medenwerker

De eerste relatie die we zullen bespreken is die tussen de entiteiten wedstrijd en medewerkers. Elke wedstrijd heeft 1 of meer wedstrijdmedewerkers en elke medewerker maakt deel uit van 1 of meer wedstrijden. Een wedstrijd kan niet plaatsvinden zonder medewerkers dus daarom de keuze 1 of meer. Een medewerker wordt altijd gelinkt aan minstens 1 wedstrijd, en krijg per wedstrijd een bepaalde shift waar bij vervolgens verloont voor wordt. Omdat veel op veel relaties niet gemodelleerd kunnen worden hebben we een koppeltabel moeten toevoegen. Deze entiteit zal wedstrijd-medewerker heten en zal dus al koppeltabel dienen tussen wedstrijd en medewerker. De entiteit kan gebruikt worden voor de beschrijving van één wedstrijd van één medewerker waar dan ook de uren(begin en eind uur (int)) en positie in staan. De verloning zal later gebeuren door het begin uur van het eind uur af te trekken en te vermeningvulldigen met het uurloon dat de entiteit medewerker zelf bijhoud. Deze tijd zal bijgehouden worden al integer beginnend bij minuten. Zo stemt 1515 overeen met kwart na 3. Elke wedstrijd heeft 1 of meer wedstrijd-medewerkers en zo bevoegd elke medewerker zich 1 of meer keer tot wedstrijd medewerker. Een wedstrijdmedewerker is telkens maar 1 medewerker deelnemend aan 1 wedstrijd.

## Relatie 2: Veel op één tussen etappe en wedstrijd

Elke wedstrijd bevat 1 of meer etappes en elke etappe maakt deel uit van 1 wedstrijd. Er is voor 1 of meer etappes per wedstrijd gekozen omdat bij een wedstrijd zonder etappes de gehele wedstrijd als de enige etappe beschouwt kan worden.

## Relatie 3: Veel op veel tussen etappe en loper

In het schema hebben we de entiteit loper, deze heeft een veel op veel relatie met de entiteit etappe. Dit zouden we in de implementatie oplossen met behulp van een koppeltabel etappe-loper. Op deze manier zou ook makkelijk de tijd per etappe voor een loper opgevraagd kunnen worden aan de hand van een property tijd binnen etappe-loper. Zowel etappe als loper zal een 1 op veel relatie hebben met etappe-loper, waarbij de etappe loper telkens maar 1 loper en 1 etappe bevat.

## Relatie 4: Veel op één tussen medewerker en loper

De lopers krijgen ook een bepaalde medewerker als aanspreekpunt. Deze medewerker kan dan uiteraard meerder lopers toegeschreven krijgen of geen enkele als hij een andere functie heeft. Een loper krijgt echter maar 1 medewerker toegewezen waardoor we een 1 op veel relatie hebben.

## Aanpak bijhouden klassement

We zouden het bijhouden van een klassement per wedstrijd doen door verschillende etappes met de zelfde wedstrijd Id met elkaar te linken. Zo kunnen de verschillende tijden van een loper per etappe worden opgeteld om de totaaltijd te bekomen en te rangschikken. Deze rangschikking kan vervolgens verder opgedeeld worden door bv een rangschikking voor enkel de mannen op te vragen.
Het algemene klassement is gebaseerd op de behaalde punten van de lopers. Het aantal punten dat een loper verdient per wedstrijd hangt af van zijn rangschikking in het desbetreffende wedstrijdklassement. Er is gekozen om de algemene rangschikking punten gebaseerd te maken omdat het moeilijk is om de som van de tijden van de wedstrijden als rangorde te nemen. Een loper kan immers een wedstrijd missen en hierdoor geen tijdswaarde hebben voor de desbetreffende wedstrijd.

## Conclusie

De vier entiteiten met bovenstaande relaties ertussen zouden voldoende moeten zijn om een gebruiksvriendelijke en efficiënte database te maken voor de loopwedstrijden. Lopers zijn erin terug te vinden op loopnummer, alsook onder andere hun punten in het klassement. De tijden die ze behaalde op de verschillende etappes zijn te vinden door hun id te linken met de etappe id en vervolgens naar de opgeslagen tijd in de entiteit etappe te gaan. Met een som van deze is de tijd per wedstrijd op te halen. Ook informatie over medewerkers is makkelijk op te zoeken met hun id en de link met wedstrijden waaraan ze meewerken.
