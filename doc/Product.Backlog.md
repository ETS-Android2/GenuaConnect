# Product Backlog

## Epic 1: QR-Code scannen

> Als Admin möchte ich das Produkt nutzen um einen QR-Code scannen zu können, um eine Verbindung mit demn WLAN oder einem Gerät herzustellen.

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

#### Implementable Story 1 WLAN-Parameter
> Als Admin möchte ich, die WLAN Parameter erhalten können und mich dadurch mit dem Wlan Netz verbinden zu können.

- Aufwandsschätzung [23] Storypoints
- Akzeptanztest:
	- Bei sich ändernden WLAN-Zugangsdaten (Änderung Passwort, aber Access-Point-Name und MAC gleich) sind keine weiteren Benutzer-Interaktionen (außer QR-Code scannen) nötig.
	- Beim Wechsel in das gesicherte WLAN wird der Betrieb der Anwendung wieder im ursprünglichen Zustand aufgenommen.
	- Die Netzwerkparameter des WLANs (IPv4-, IPv6-Adresse, Netzmaske, DNS-Server-IP, Gateway-IP) inkl. DHCP-Zuweisung werden dargestellt

##### Task 1 Änderung der Zugangsdaten
Sollten sich die Wlan-Zugangsdaten ändern, so wird keine weiter interaktion vom Benutzer benötigt außer einen erneuten scann des QR-Codes.

- Aufwandsschätzung:

- Tatsächlich benötigte Zeit:

#### Implementable Story 2 Handykameranutzung
> Als Nutzer möchte ich, dass beim Starten der App die Kamera funktionsfähig ist um einen QR-Code scannen zu können.

- Aufwandsschätzung [4] Storypoints
- Akzeptanztest:
	- Funktionen der Kamera für die App freigeschalten
	- Beim start der App wird dem Nutzer direkt ermöglicht einen QR-Code zu scannen

##### Task 1 Kamerafunktion wird direkt gestartet
Beim start der App soll direkt die Kamera mit der QR-Code funktion geöffnet werden.

- Aufwandsschätzung:

- Tatsächlich benötigte Zeit:

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

> Als Nutzer möchte ich, dass die App zugriff auf die Handykamera hat, damit man einen QR-Code scannen kann.


- Aufwandsschätzung [3] Storypoints

- Akzeptanztest:
	- App hat permission erteilt bekommt
	- App kann Kamerafunktionen aufrufen

##### Task 1 Permission wurden erteilt
Die App hat die Berechtigung auf die Kamera zuzugreifen.

- Aufwandsschätzung:
- Tatsächlich benötigte Zeit:

#### Implementable Story 2 Geräte QR-Code wird erkannt

> Als Nutzer möchte ich, dass der Scanner erkennt ob es sich um ein Geräte QR-Code handelt.

- Aufwandsschätzung [46] Storypoints

- Akzeptanztest:
	- Zugriff auf die Handykamera
	- QR-Codes können gescannt werden
	- QR-Code wird analysiert

##### Task 1 Kamerazugriff

Die App soll die Erlaubnis besitzen, auf die Kamerafunktionen zugreifen zu können. Danach wird beim erstmaligem öffnen gefragt.

- Aufwandsschätzung: 30min

- Tatsächlich benötigte Zeit: 25min


##### Task 2 QR-Code Analyse

Der QR-Code wird eingelesen und dessen Inhalt wird auf die richtige Form geprüft.

- Aufwandsschätzung: 3h

- Tatsächlich benötigte Zeit: 4h

### Feature 3 *Blitzlicht*

> Als Admin möchte ich, dass ich bei Bedarf das Handyblitzlicht aktivieren kann, um einen QR-Code auch bei nicht optimalen Lichtverhältnissen scannen zu können.

- Aufwandsschätzung: XS

- Akzeptanztests:

	- Zugriff auf Handy-Blitzlicht.

	- Blitzlicht ist nach Betätigung des Buttons aktiv.

#### Implementable Story 1 Zugriff auf Handyblitzlicht

> Als Nutzer möchte ich, dass die App auf das Handyblitzlicht zufreifen kann, damit man bei schlechten Lichtverhältnissen das Handylicht einschalten kann, um das Scannen von QR-Codes zu erleichtern.

- Aufwandsschätzung [4] Storypoints

- Akzeptanztest:
	- Zugriff auf das Handyblitzlicht wurde erteilt.

#### Task 1 Button aktiviert das Handyblitzlicht

Der Nutzer der App kann einen Button bedienen um das Handyblitzlicht zu aktivieren.

- Aufwandsschätzung: 20min

- Tatsächlich benötigte Zeit: 15min


### Feature 4 *Multiscan*

> Als Admin möchte ich, dass ich den QR-Code für das WLAN und die Geräteverbindung nacheinander einscannen kann und mehrere Geräte nacheinander einscannen kann und diese mir im nachhinein anzeigen lassen kann.

- Aufwandsschätzung: L

- Akzeptanztest:

	- Man kann in einem Screen den QR-Code für das WIFI scannen, dessen Infos dann in dem Screen gezeigt werden und im selben Screen ohne Unterbrechung den QR-Code für mehrere Geräte des Rechenzentrum nacheinander scannen.

#### Implementable Story 1 Durchlaufender scann von QR-Codes

> Als Nutzer möchte ich ununterbrochen QR-Codes einscannen können, um einen optimalen Arbeitsfluss zu erreichen.

- Aufwandsschätzung [69] Storypoints

- Akzeptanztest:
	- kein weiterer input wird benötigt um einen weitern QR-Code scannen zu können.

##### Task 1 Scannen
Wird eine QR-Code von der App erkannt und gelesen, dann wird dies signalisiert, allerdings soll ein flow nicht verhindert werden und dem Nutzer zu ermöglichen mehrere QR-Codes in Folge zu scannen.

- Aufwandsschätzung:

- Tatsächlich benötigte Zeit:

## Epic 2: Verwaltung von Abfragen


> Als Admin möchte ich bestimmte Abfragen kategorisiert und übersichtlich verwalten und benutzen können, um die App benuztzerfreundlicher zu machen.

Die Abfragen, die der Admin tätigen will, kann mit der App in ein Abfragemaske gepackt werden, damit Abfragen gleichen Typs effizienter getätigt werden können. Eine Abfragemaske kann vorher defeniert werden. Dies sind eine bestimmte Menge von Abfragen, die immer gleichzeitig getätigt werden. Eine einzelne Abfrage ist die versuchte Ermittlung von Informationen.

### Feature 1 *Abfragemasken verwalten*

> Als Admin möchte ich Abfragenschemata erstellen können, um gleiche Abfragen schneller tätigen zu können.

- Aufwandsschätzung: M

- Akzeptanztest:

	- Abfragen können verwaltet (neu angelegt, bearbeitet, angesehen und gelöscht) werden.

	- Abfragen enthalten mindestens eine Bezeichnung, eine OID (min. Dot-Notation) den 					erwarteten Rückgabewert-Typ.

	- Abfragen können keiner, einer oder mehreren Kategorien (Art der Hardware) zugeordnet werden.

	- Abfragen können gestartet werden und das Result angezeigt werden.

#### Implementable Story 1 Inhalt der Abfragemasken bearbeiten

> Als Admin möchte ich Abfragemasken erstellen, löschen, bearbeiten und gestartet können.

- Aufwandsschätzung: [69] Story Points

- Akzeptanztests:
    - Abfragen enthalten mindestens eine Bezeichnung, eine OID (min. Dot-Notation) den 					erwarteten Rückgabewert-Typ.
    - Abfragen können verwaltet (neu angelegt, bearbeitet, angesehen und gelöscht) werden.


##### Task 1 OIDs bearbeiten

Dot-Notation der OID und die dazugehörigen Bezeichnung kann bearbeitet werden.

- Aufwandsschätzung: 1h

- Tatsächliche benötigte Zeit: 35min

##### Task 2 OIDs hinzufügen

Dot-Notation der OID kann hinzugefügt werden.

- Aufwandsschätzung: 1h

- Tatsächliche benötigte Zeit: 20min

##### Task 3 OIDs löschen

> Als Nutzer möchte ich Abfragemasken löschen können, um eine bessere Übersicht beibehalten zu können.

- Aufwandsschätzung: 30min

- Tatsächliche benötigte Zeit: 15min

#### Implementable Story 2 Namen der Abfragemasken bearbeiten

> Als Admin möchte ich die Namen der Abfragemasken bearbeiten können, um auf einen Blick sehen zu können um welche Abfragemaske es sich handelt.

- Aufwandsschätzung [18] Storypoints

- Akzeptanztest:
	- Name kann verändert werden
	- geänderter Name wird beibehalten
	- Name kann nicht zu einem Name geändert werden der bereits vorhanden ist

##### Task 1 Namen ändern
Der Name kann nach belieben geändert und angepasst werden.

- Aufwandsschätzung: 2h

- Tatsächlich benötigte Zeit: 1h


### Feature 2 *Abfragenmenü*

> Als Admin möchte ich, dass ich die Art der Abfrage (Welche Hardware Kategorie) auswählen können, um die App nutzerfreundlicher zu machen.

- Aufwandsschätzung: M

- Akzeptanztest:

	- Die Informationen, die man durch die Abfragen erhalten hat, werden in Menüs abgespeichert, bei denen man einzelne Kategorien als Untermenüs auswählen kann (Art der Hardware)

	- Die einzelnen Hardwareinformationen werden in den zugehörigen Menüpunkten angezeigt.

	- Es gibt ein Menüpunkt zur Anzeige von Wlan Informationen.

	- Es gibt ein Menüpunkt zur Anzeige von Systemzuständen.

#### Implementable Story 1 Abfragemasken verwalten

> Als Admin möchte ich Abfragemasken bearbeiten, hinzufügen und löschen können, um später Abfragen gebündelt an Geräte stellen zu können.

- Aufwandsschätzung [51] Storypoints
- Akzeptanztest:
	- die Abfragemasken können bearbeitet werden
	- die Abfragemasken können gelöscht werden
	- Abfragemasken können hinzugefügt werden

##### Task 1 Abfragemasken erstellen

Abfragemasken können erstellt werden.

- Aufwandsschätzung: 2h

- Tatsächlich benötigte Zeit: 1h20min

##### Task 2 Abfragemasken hinzufügen

Abfragemasken können hinzugefügt werden.

- Aufwandsschätzung: 2h

- Tatsächlich benötigte Zeit: 1h

##### Task 3 Abfragemasken löschen

Abfragemasken können gelölscht werden.

- Aufwandsschätzung: 1h

- Tatsächlich benötigte Zeit: 40min

#### Implementable Story 2 Einzlne OIDs verwalten
> Als Nutzer möchte ich einzelne OIDs verwalten können, um Nutzbarkeit des Programms zu erhöhen.

- Aufwandsschätzung [42] Storypoint
- Akzeptanztest:

##### Task 1 OID bearbeiten

Einzelne OID kann bearbeitet werden.

- Aufwandsschätzung: 2h

- Tatsächlich benötigte Zeit: 1h30min

##### Task 2 OID hinzufügen

Eine einzelne OID kann hinzugefügt werden.

- Aufwandsschätzung: 1h30min

- Tatsächlich benötigte Zeit: 1h

##### Task 3 OID löschen

Einzelne OID kann gelöscht werden.

- Aufwandsschätzung: 30min

- Tatsächlich benötigte Zeit: 20min

## Epic 3: Oberfläche

> Als Admin will ich eine gut strukturierte Oberfläche haben, die auch einfach und intuitiv zu bedienen ist, um die Nutzung der App zu vereinfachen.

Die Oberfläche sollte so einfach und intuitiv wie möglich gestaltet werden.

### Feature 1 *Start der App*

> Als Admin will ich, dass bei Start der App direkt die Kamera für die Scanfunktion an ist, um direkt die Hauptfunktion nutzen zu können.

- Aufwandsschätzung: M

- Akzeptanztest:

	- Es wird direkt die Kamera gestartet bei Start der App.

	- Die App muss beim ersten Start nach der Berechtigung für die Kamera fragen.

	- Oben Rechts gibt es den Button für "Flashlight".

	- In der unteren Hälfte gibt es den Button "QR-Code Scannen".

#### Implementable Story 1 Startanzeige
> Als Nutzer möchte ich, dass sich beim startet der App etwas bestimmtes angezeigt wird, wie z.b. das Logo oder eine Animation welche das Logo der App anschaulich anzeigt.

- Aufwandsschätzung [12] Storypoints
- Akzeptanztest:
	- Beim strten der App wird nicht ein weißer screen angezeigt
	- es wird ein Logo o.ä. angezeigt

##### Task 1 Logo einbettung
Das Logo des Kunden wird als Startbildschrim angezeigt.

- Aufwandsschätzung: 30min

### Feature 2 *Sprachmenü*

> Als Admin möchte ich, dass sich die Srache der Systemsprache anpasst.

- Aufwandschätzung: S

- Akzeptanztest:

	- Die in-App Srpache soll der Systemsprache entsprechen.
	- Ist die Systemsprache nicht Deutsch, so soll Standardgemäß die App in der Sprache Englisch angezeigt werden.

#### Implementable Story 1 Erstellung der String/XML Datei
> Als Admin möchgte ich, dass die Strings in eine XML Datei ausgelagert sind, um eine Übersetzung zu erleichtern.

- Aufwandsschätzung [11] Storypoints
- Akzeptanztest
	- XML Datei ist vorhanden
	- in der XML Datei stehen die übersetzungen der einzelnen Strings
	- Strings sind ausgelagert in der XML Datei

##### Task 1 Übersetzung
Die zu übesetzenden Strings, welche der Nutzer beim bedienen der App zu lesen hat werden übersetzt.

- Aufwandsschätzung: 1h
- Tatsächlich benötigte Zeit: 1h 20min

### Feature 3 *Hintergrundfarbe*

> Als Admin möchte ich, dass die Toolbarfarbe Standardhintergrundfarbe RAL 4007 ist und diese soll veränderbar sein, um den Nutzer die Möglichkeit zu geben die App zu personalisieren.

- Aufwandsschätzung: XS

- Akzeptanztest:

	- In den "Einstellungen" gibt es ein Menü für die Hintergrundfarbe.

	- Standartmäßig ist die Farbe RAL 4007 als Hintergrundfarbe eingestellt weiß als Text eingestellt.

	- Es gibt noch die Option die Hintergrundfarbe auf navy blue oder grau einzustellen.

#### Implementable Story 1 Auswahl von alternativen Farben
> Als Nutzer möchte ich eine andere Farbe als Hintergrund wählen können, um die App etwas mehr personalisieren zu können.

- Aufwandsschätzung [5] Storypoints
- Akzeptanztest:
	- Ausgewählte Farbe wird die neue Hintergrundfarbe
	- Schriftfarbe passt sich an (Kontrast)

##### Task 1 Vorausgewählte Farbparlette
Der Nutzer bekommte eine gewisse Farbparlette zur auswahl von der er sich einen Farbe als Hintergrund legen kann.

- Aufwandsschätzung: 45min

### Feature 4 *Menü*

> Als Admin möchte ich, dass es ein Menü gibt indem ich die Sprache und die Benutzeroberfläche einstellen kann und auch die Abfragen nochmal ansehen kann.

- Aufwandsschätzung: S

- Akzeptanztest:

	- Der Button für das "Menü" ist oben Rechts beim "Start der App".

	- Im "Menü" gibt es den Button Für "Optionen", "verbundene Geräte", "WLAN-Verbindung", "Abfragenmenü" und "Hilfe".

#### Implementable Story 1 *Hilfe*
> Als Nutzer möchte ich, dass mir im Hilfemenü Informationen und eine Bedienungsanleitung zur Nutzung der App angezeigt wird.

- Aufwandsschätzung [9] Storypoints
- Akzeptanztest:
	- Im Hilfsmenü finden sich informationen zur App Nutzung
	- Das Hilfsmenü ist aufrufbar
	- Hilfsmenü passt sich der Systemsrpache an

##### Task 1 Text erstellen
Eine Text Datei soll erstellt werden inder dem Nutzer beschrieben wird wie man die richtig Nutzen kann. Dem Nutzer soll somit die Bedienung erklärt und erleichtert werden, damit er nicht vorher von einer anderen Person dazu belehrt werden muss.

- Aufwandsschätzung: 1h30min
- Tatsächlich benötigte Zeit: TODO