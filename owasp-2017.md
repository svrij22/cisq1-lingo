# Vulnerability Analysis

## A1:2017 Injection

### Description
SQL Injection is de meest voorkomende vulnerability en staat op nummer 1 in de Owasp 10. 
Het is een techniek waarbij SQL code wordt geinjecteerd in een Query.
Meestal kan een aanvaller hierdoor data opvragen en aanpassen die niet van hem is.

### Risk
Bij veel oudere applicaties is dit risico groot.
Vooral als men de SQL Queries met strings uit form data aan elkaar plakt.
Echter gebruiken wij Hibernate met PreparedStatements en dit maakt het vrijwel onmogelijk om SQL injections te doen.

### Counter-measures
We gebruiken Hibernate om SQL injection onmogelijk te maken.

## A5:2017 Broken Authentication

### Description
Om binnen te komen in iemands account gebruiken aanvallers vaak Brute-Force attacks of Dictionary attacks.
Dit houdt in dat men honderden wachtwoord pogingen achter elkaar probeerd in de hoop dat een poging goed is.
De wachtwoorden komen uit een lijst of worden gegenereerd.

### Risk
Als een webapplicatie zwakke wachtwoorden toelaat en de inlogpogingen niet stopt kan het zijn dat er op iemand account wordt ingebroken.
Risicofactoren zijn Single-Factor Authentication, zwakke wachtwoorden en voor een Man-In-The-Middle attack geen session id rotation.

### Counter-measures
Om dit soort aanvallen tegen te gaan maken we gebruik van een LoginAttemptService.
Deze service houdt per IP adres bij hoeveel inlogpogingen er zijn gedaan. Na 24 uur reset het aantal inlogpogingen en kan een gebruiker weer proberen in te loggen. 

## A1:2017 Injection

### Broken Access Control
Als de access control niet goed is ingesteld of bij de client zelf ligt, is het soms mogelijk bij pagina's te komen waar je niet voor geauthoriseerd bent.
Pagina's die geen standaard authorisatie hebben of gebruikers die simpelweg een admin pagina kunnen zien vormen een risico.

### Risk
Een client zou pagina's kunnen zien of acties kunnen uitvoeren zonder geauthoriseerd te hoeven zijn. 

### Counter-measures
Elke pagina, method of path heeft standaard access control, en kan niet worden bekeken zonder meegegeven token.
De front-end redirect je naar de Login pagina als men nog niet is ingelogd. Alleen de login, register en leaderboard pagina zijn niet beveiligd.