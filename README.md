# Beschrijving DDL schema van loperdatabase “De Vrolijke Zweters”
## Entiteit 1: Wedstrijd
De eerste entiteit in het schema die we zullen bespreken is wedstrijd. Deze bevat de properties wedstrijd id, plaats, afstand en medewerkers id. De wedstrijd id is een unieke string per wedstrijd, er is gekozen voor een string omdat er geen wiskundige operaties op uitgevoerd zullen worden. Voor plaats is er vanzelfsprekend ook een string gekozen, deze zijn veiliger omdat de verschillende interpretatie van bv de punt en de komma in verschillende landen voor problemen kan zorgen. Afstand is geïmplementeerd als een int , zo kunnen de wedstrijden makkelijk gesorteerd worden op afstand. Een wedstrijd heeft een lijst van al de medewerkers in de vorm van al hun medewerkers id’s, dit is ook een string om dezelfde reden als wedstrijd id.
## Entiteit 2: Medewerker
Naast medewerker Id bevat de entiteit medewerker ook nog andere properties zoals naam, functie en leeftijd. Naam en functie zijn strings. Voor leeftijd hebben we een int gekozen omdat hier mogelijk wel berekeningen mee zullen gebeuren, zoals rangschrikken. 
## Entiteit 3: Etappe
De properties van de entiteit etappe zijn etappe Id (string), wedstrijd id (string), afstand (int), tijd (int) en loper id (string). Via de verschillende id’s wordt een etappe gelinkt aan een wedstrijd en aan een loper. Deze laatste link betreft een veel op veel relatie dus zal hier een koppeltabel geïmplementeerd moeten worden, dit wordt later nog verduidelijkt.
## Entiteit 4: Loper
Een loper heeft als property een loper id (string) deze zal ook dienst doen als zijn uniek loopnummer waardoor het makkelijk is om deze loper op te zoeken. Hiernaast houden we ook zijn naam bij als string, deze is echter niet altijd uniek dus zal zijn id de primaire key gebruikt worden. Dit is ook het geval bij de id’s van de andere entiteiten. Hiernaast zijn leeftijd (int), geslacht (String), Gewicht (int), Fysieke toestand (String) en Club (String) ook properties binnen loper. Leeftijd en gewicht zijn integers omdat op basis van deze een rankschikking of een selectie binnen een bepaald bereik kan gemaakt worden. Bij de andere properties wordt enkel gekeken naar de waarde (bv is vrouw?) en is er dus een mogelijkheid om deze als string op te slaan. Hiernaast heeft hij nog punten (int) die hij kan verdienen per etappe waarop we later kunnen rangschikken in het algemene klassement. 
## Relatie 1: Veel op veel tussen wedstrijd en medenwerker
De eerste relatie die we zullen bespreken is die tussen de entiteiten wedstrijd en medewerkers. Elke wedstrijd heeft 1 of meer wedstrijdmedewerkers en elke medewerker maakt deel uit van 0 of meer wedstrijden. Een wedstrijd kan niet plaatsvinden zonder medewerkers dus daarom de keuze 1 of meer. Een medewerker moet echter niet gebonden zijn aan een wedstrijd, zo kan hij een algemene job hebben zoals mediamanager. Een medewerker met bijvoorbeeld de functie seingever is wel gelinkt aan alle wedstrijden waar hij aan werkt. Deze relatie is dus 0 of meer. Omdat veel op veel relaties niet gemodelleerd kunnen worden zullen we later een koppeltabel moeten toevoegen. Deze entiteit zal wedstrijd-medewerker heten en zal dus al koppeltabel dienen tussen wedstrijd en medewerker. De entiteit kan gebruikt worden voor de beschrijving van één wedstrijd van één medewerker waar dan ook de uren, vergoeding en plaats in staan. 

## Relatie 2: Veel op één tussen etappe en loper
Elke wedstrijd bevat 1 of meer etappes en elke etappe maakt deel uit van 1 wedstrijd. Er is voor 1 of meer etappes per wedstrijd gekozen omdat bij een wedstrijd zonder etappes de gehele wedstrijd als de enige etappe beschouwt kan worden. 
## Relatie 3: Veel op veel tussen etappe en loper
In het schema hebben we de entiteit loper, deze heeft een veel op veel relatie met de entiteit etappe. Dit zouden we in de implementatie oplossen met behulp van een koppeltabel etappe-loper. Op deze manier zou ook makkelijk de tijd per etappe voor een loper opgevraagd kunnen worden aan de hand van een property tijd binnen etappe-loper. De koppeltabellen mochten we echter achterwege laten voor deze opdracht dus staat de property tijd momenteel binnen de entiteit etappe. Een etappe kan 1 of meerdere lopers hebben, wanneer een etappe geen lopers bevat zal deze waarschijnlijk afgelast worden. Een loper moet ook voor minstens 1 etappe ingeschreven zijn. Inschrijven in het klassement gaat immers gepaard met het inschrijven in een wedstrijd en daardoor alle etappes van deze wedstrijd (minimaal 1 zoals eerder vermeld).
## Relatie 4: Veel op één tussen medewerker en loper
De lopers krijgen ook een bepaalde medewerker als aanspreekpunt. Deze medewerker kan dan uiteraard meerder lopers toegeschreven krijgen of geen enkele als hij een andere functie heeft. Een loper krijgt echter maar 1 medewerker toegewezen waardoor we een 1 op veel relatie hebben. 
## Aanpak bijhouden klassement
We zouden het bijhouden van een klassement per wedstrijd doen door verschillende etappes met de zelfde wedstrijd Id met elkaar te linken. Zo kunnen de verschillende tijden van een loper per etappe worden opgeteld om de totaaltijd te bekomen en te rangschikken. Deze rangschikking kan vervolgens verder opgedeeld worden door bv een rangschikking voor enkel de mannen op te vragen. 
Het algemene klassement is gebaseerd op de behaalde punten van de lopers. Het aantal punten dat een loper verdient per wedstrijd hangt af van zijn rangschikking in het desbetreffende wedstrijdklassement. Er is gekozen om de algemene rangschikking punten gebaseerd te maken omdat het moeilijk  is om de som van de tijden van de wedstrijden als rangorde te nemen. Een loper kan immers een wedstrijd missen en hierdoor geen tijdswaarde hebben voor de desbetreffende wedstrijd. 
## Conclusie
De vier entiteiten met bovenstaande relaties ertussen zouden voldoende moeten zijn om een gebruiksvriendelijke en efficiënte database te maken voor de loopwedstrijden. Lopers zijn erin terug te vinden op loopnummer, alsook onder andere hun punten in het klassement. De tijden die ze behaalde op de verschillende etappes zijn te vinden door hun id te linken met de etappe id en vervolgens naar de opgeslagen tijd in de entiteit etappe te gaan. Met een som van deze is de tijd per wedstrijd op te halen. Ook informatie over medewerkers is makkelijk op te zoeken met hun id en de link met wedstrijden waaraan ze meewerken.
