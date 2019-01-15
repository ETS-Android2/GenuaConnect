# Product Backlog

## Epic 1: QR-Code scannen

> Als Admin möchte ich das Produkt nutzen um einen QR-Code scannen zu können, um eine Verbindung mit demn WLAN oder einem Gerät herzustellen.

Beschreibung:

Der Admin muss sich im Rechenzentrum mit dem bereitgestellten WLAN verbinden. Dies erledigt er, indem er ein QR-Code scannt. Außerdem kann der Admin mittels QR-Codes an den Geräten im Rechenzentrum Informationen dieser abfragen.

### Feature 1 *WLAN Verbindung herstellen*

> Als Admin möchte ich, dass die App in der Lage ist einen QR Code auf einem WLAN Gerät zu scannen und sich mit diesem zu verbinden.

Die App scannt einen QR-Code. Erkennt er diesen als WLAN QR-Code, so soll er sich mit diesem verbinden.

- Aufwandsschätzung: L

- Akzeptanztests:
    - Zugriff auf die Handykamera

    - QR-Codes können gescannt werden.

    - Smartphone ist mit dem WLAN verbunden.

    - Bei Verbindung werden die WLAN Parameter direkt im Bildschirm angezeigt und diese können immer im Abfragenmenü angezeigt werden lassen.

    - Bei sich ändernden WLAN-Zugangsdaten (Änderung Passwort, aber Access-Point-Name und MAC gleich) sind keine weiteren Benutzer-Interaktionen (außer QR-Code scannen) nötig.

    - Beim Wechsel in das gesicherte WLAN wird der Betrieb der Anwendung wieder im ursprünglichen Zustand aufgenommen.

	- Die Netzwerkparameter des WLANs (IPv4-, IPv6-Adresse, Netzmaske, DNS-Server-IP, Gateway-IP) inkl. DHCP-Zuweisung werden dargestellt.

    - falls WLAN-Netz nicht verschlüsselt ist, dann wird keine Verbindung hergestellt und es wird dem Nutzer eine Fehlermeldung angezeigt und es werden keine Daten übertragen.

### Feature 2 *Geräteverbindung herstellen*

> Als Admin möchte ich, dass die App in der Lage ist, mir die vordefinierten Informationen  anzuzeigen, indem ich einen QR-Code scanne.

- Aufwandsschätzung: L

- Akzeptanztests:
    - Zugriff auf die Handykamera

    - QR-Codes können gescannt werden.

    - Informationen werden abgerufen und direkt danach für paar Sekunden angezeigt im Bildschirm (Mit der Option direkt die Infos wegzuklicken) und dann im Abfragenmenü gespeichert.

    - Standard-Abfragen (1.3.6.1.2.1.1) werden vom überwachenden Gerät abgefragt und vom bedienenden Smartphone angezeigt.

    - Gestartete Abfragen werden periodisch erneut abgefragt.

#### Implementable Story 1 Zugriff auf die Handykamera

> Die App soll die Erlaubnis besitzen, auf die Kamerafunktionen zugreifen zu können. Danach wird beim erstmaligem öffnen gefragt.

- Aufwandsschätzung [] Storypoints

- Akzeptanztest:

#### Task 1 //TODO google ob teask ohne IS machbar und IS ohne Task machbar//

#### Implementable Story 2 Geräte QR-Code wird erkannt

> Als Nutzer möchte ich, dass der Scanner erkennt ob es sich um ein Geräte QR-Code handelt.

- Aufwandsschätzung [14] Storypoints

- Akzeptanztest:
	- Zugriff auf die Handykamera
	- QR-Codes können gescannt werden
	- QR-Code wird analysiert

#### Task 1 Kamerazugriff

> Die App soll die Erlaubnis erteilt bekommen, auf die Kamerafunktionen zugreifen zu können. Danach wird beim erstmaligem öffnen gefragt.

#### Task 2 QR-Code Analyse

> Der QR-Code wird eingelesen und dessen Inhalt wird auf die richtige Form geprüft.


### Feature 3 *Blitzlicht*

> Als Admin möchte ich, dass ich bei Bedarf das Handyblitzlicht aktivieren kann, um einen QR-Code auch bei nicht optimalen Lichtverhältnissen scannen zu können.

- Aufwandsschätzung: XS

- Akzeptanztests:

	- Zugriff auf Handy-Blitzlicht.

	- Blitzlicht ist nach Betätigung des Buttons aktiv.

#### Implementable Story 1 Zugriff auf Handyblitzlicht

> TODO

- Aufwandsschätzung [4] Storypoints

- Akzeptanztest:
	- Zugriff auf das Handyblitzlicht wurde erteilt.

#### Task 1 Button aktiviert das Handyblitzlicht

> Der Nutzer der App kann einen Button bedienen um das Handyblitzlicht zu aktivieren.

- Aufwandsschätzung:

- Tatsächlich benötigte Zeit:


### Feature 4 *Multiscan*

> Als Admin möchte ich, dass ich den QR-Code für das WLAN und die Geräteverbindung nacheinander einscannen kann und mehrere Geräte nacheinander einscannen kann und diese mir im nachhinein anzeigen lassen kann.

- Aufwandsschätzung: L

- Akzeptanztest:

	- Man kann in einem Screen den QR-Code für das WIFI scannen, dessen Infos dann in dem Screen gezeigt werden und im selben Screen ohne Unterbrechung den QR-Code für mehrere Geräte des Rechenzentrum nacheinander scannen.

#### Implementable Story 1 Durchlaufenden scann von 

## Epic 2: Verwaltung von Abfragen


> Als Admin möchte ich bestimmte Abfragen kategorisiert und übersichtlich verwalten und benutzen können, um die App benuztzerfreundlicher zu machen.

Beschreibung:

Die Abfragen, die der Admin tätigen will, kann mit der App in ein Abfragemaske gepackt werden, damit Abfragen gleichen Typs effizienter getätigt werden können. Eine Abfragemaske kann vorher defeniert werden. Dies sind eine bestimmte Menge von Abfragen, die immer gleichzeitig getätigt werden. Eine einzelne Abfrage ist die versuchte Ermittlung von Informationen.

### Feature 1 *Abfragenschema erstellen*

> Als Admin möchte ich Abfragenschemata erstellen können, um gleiche Abfragen schneller tätigen zu können.

- Aufwandsschätzung: M

- Akzeptanztest:

	- Abfragen können verwaltet (neu angelegt, bearbeitet, angesehen und gelöscht) werden.

	- Abfragen enthalten mindestens eine Bezeichnung, eine OID (min. Dot-Notation) den 					erwarteten Rückgabewert-Typ.

	- Abfragen können keiner, einer oder mehreren Kategorien (Art der Hardware) zugeordnet werden.

	- Abfragen können gestartet werden und das Result angezeigt werden.

#### Implementable Story 1 Abfragemasken Verwalten

> Als Admin möchte ich Abfragemasken erstellen, löschen, bearbeiten und gestartet können.

- Aufwandsschätzung: [69] Story Points

- Akzeptanztests:
    - Abfragen enthalten mindestens eine Bezeichnung, eine OID (min. Dot-Notation) den 					erwarteten Rückgabewert-Typ.
    - Abfragen können verwaltet (neu angelegt, bearbeitet, angesehen und gelöscht) werden.


#### Task 1 Abfragemasken erstellen

> Als Nutzer möchte ich Abfragemasken erstellen können, um diese bei bedarf nutzen zu können.

- Aufwandsschätzung:

- Tatsächliche benötigte Zeit:

#### Task 2 Abfragemasken bearbeiten

> Als Nutzer möchte ich Abfragemasken bearbeiten könenn, um bei kleinen Änderung keine komplett neue Abfragemaske erstellen zu müssen.

- Aufwandsschätzung:

- Tatsächliche benötigte Zeit:

#### Task 3 Abfragemasken löschen

> Als Nutzer möchte ich Abfragemasken löschen können, um eine bessere Übersicht beibehalten zu können.

- Aufwandsschätzung:

- Tatsächliche benötigte Zeit:

### Feature 2 *Abfragenmenü*

> Als Admin möchte ich, dass ich die Art der Abfrage (Welche Hardware Kategorie) auswählen können, um die App nutzerfreundlicher zu machen.

- Aufwandsschätzung: M

- Akzeptanztest:

	- Die Informationen, die man durch die Abfragen erhalten hat, werden in Menüs abgespeichert, bei denen man einzelne Kategorien als Untermenüs auswählen kann (Art der Hardware)

	- Die einzelnen Hardwareinformationen werden in den zugehörigen Menüpunkten angezeigt.

	- Es gibt ein Menüpunkt zur Anzeige von Wlan Informationen.

	- Es gibt ein Menüpunkt zur Anzeige von Systemzuständen.

### Implementable Story 1 Abfragemasken verwalten

> Als Admin möchte ich Abfragemasken bearbeiten, hinzufügen und löschen können, um später Abfragen gebündelt an Geräte stellen zu können.


#### Task 1 OIDs bearbeiten

> Man kann OIDs manuell eintippen (min. Dot-Notation).


#### Task 2 OIDs hinzufügen

> TODO


#### Task 3 OIDs löschen

> TODO


### Implementable Story 2 Einzlne OIDs verwalten

> TODO

#### Task 1 OID bearbeiten

> TODO


#### Task 2 OID hinzufügen

> TODO


#### Task 3 OID löschen

> TODO


## Epic 3: Oberfläche

> Als Admin will ich eine gut strukturierte Oberfläche haben, die auch einfach und intuitiv zu bedienen ist, um die Nutzung der App zu vereinfachen.

Beschreibung:

Die Oberfläche sollte so einfach und intuitiv wie möglich gestaltet werden.

### Feature 1 *Start der App*

> Als Admin will ich, dass bei Start der App direkt die Kamera für die Scanfunktion an ist, um direkt die Hauptfunktion nutzen zu können.

- Aufwandsschätzung: L

- Akzeptanztest:

	- Es wird direkt die Kamera gestartet bei Start der App.

	- Die App muss beim ersten Start nach der Berechtigung für die Kamera fragen.

	- Oben Rechts gibt es den Button für "Flashlight".

	- In der unteren Hälfte gibt es den Button "QR-Code Scannen".

### Feature 2 *Sprachmenü*

> Als Admin möchte ich, dass ich in einem separaten Menü die Sprache ändern kann.

- Aufwandschätzung: S

- Akzeptanztest:

	- Man kann im Menü ein Menüpunkt Optionen auswählen.

	- Der Button Sprachauswahl ist in den "Optionen".

	- Es sollten die Sprachen Deutsch und Englisch auswählbar sein.

	- Bei Auswahl der Sprache sollte die Richtige Sprache angezeigt werden.

	- Es sollten zur Erleichterung nur die Beiden jeweiligen Flaggen angezeigt werden im Button 		(DE/EN).

### Feature 3 *Statusbarfarbe*

> Als Admin möchte ich, dass die Toolbarfarbe Standardhintergrundfarbe RAL 4007 ist, um den Nutzer die Möglichkeit zu geben die App zu personalisieren.

- Aufwandsschätzung: XS

- Akzeptanztest:

	- In den "Einstellungen" gibt es ein Menü für die Hintergrundfarbe. 

	- Standartmäßig ist die Farbe RAL 4007 als Hintergrundfarbe eingestellt weiß als Text eingestellt.

	- Es gibt noch die Option die Toolbarfarbe auf navy blue oder grau einzustellen.

### Feature 4 *Menü*

> Als Admin möchte ich, dass es ein Menü gibt indem ich die Sprache und die Benutzeroberfläche einstellen kann und auch die Abfragen nochmal ansehen kann.

- Aufwandsschätzung: S

- Akzeptanztest:

	- Der Button für das "Menü" ist oben Rechts beim "Start der App".

	- Im "Menü" gibt es den Button Für "Optionen", "verbundene Geräte", "WLAN-Verbindung", "Abfragenmenü" und "Hilfe".

