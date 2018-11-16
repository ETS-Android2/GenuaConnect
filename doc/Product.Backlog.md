# Product Backlog

## Epic 1: QR-Code scannen

> Als Admin möchte ich das Produkt nutzen um einen QR-Code scannen zu können.

Beschreibung:

Der Admin muss sich im Rechenzentrum mit dem bereitgestellten WLAN verbinden. Dies erledigt er, indem er ein QR-Code scannt. Außerdem kann der Admin mittels QR-Codes an den Geräten im Rechenzentrum Informationen dieser abfragen.

### Feature 1 *WLAN verbindung herstellen*

> Als Admin möchte ich, dass die App in der Lage ist einen QR Code auf einem Wlan Gerät zu scannen und sich mit diesem zu verbinden.

- Aufwandsschätzung: L

- Akzeptanztests:
    - Zugriff auf die Handykamera

    - QR-Codes können gescannt werden.

    - Smartphone ist mit dem WLAN verbunden.

    - Bei Verbindung werden die WLAN Parameter direkt im Bildschirm angezeigt und diese können immer im Abfragenmenü angezeigt werden lassen.

    - Bei sich ändernden WLAN-Zugangsdaten (Änderung Passwort, aber Access-Point-Name und MAC gleich) sind keine weiteren Benutzer-Interaktionen (außer QR-Code scannen) nötig.

    - Beim Wechsel in das gesicherte WLAN wird der Betrieb der Anwendung wieder im ursprünglichen Zustand aufgenommen.

    - falls WlAN-Netz nicht verschlüsselt ist, dann wird keine Verbindung hergestellt und es wird dem Nutzer eine Fehlermeldung mittig im Screen angezeigt.

### Feature 2 *Geräteverbindung herstellen*

> Als Admin möchte ich, dass die App in der Lage ist, mir die vordefinierten Informationen  anzuzeigen, indem ich einen QR-Scode scanne.

- Aufwandsschätzung: L

- Akzeptanztests:
    - Zugriff auf die Handykamera

    - QR-Codes können gescannt werden.

    - Informationen werden abgerufen und direkt danach für paar sekunden angezeigt im Bildschirm (Mit der Option direkt die Infos wegzuklicken) und dann im Abfragenmenü gespeichert.

    - Standard-Abfragen (1.3.6.1.2.1.1) werden vom überwachenden Gerät abgefragt und vom bedienenden Smartphone angezeigt.

    - Gestartete Abfragen werden periodisch erneut abgefragt.

### Feature 3 *Blitzlicht*

> Als Admin möchte ich, dass ich bei Bedarf das Handyblitzlicht aktivieren kann, um einen QR-Code auch bei nicht optimalen Lichtverhältnissen scannen zu können.

- Aufwandsschätzung: S

- Akzeptanztests:

	- Zugriff auf Handy-Blitzlicht.

	- Blitzlicht ist nach Betätigung des Buttons aktiv.

### Feature 4 *Multiscan*

> Als Admin möchte ich, dass ich den QR-Code für das Wlan und die Geräteverbindung nacheinander einscannen kann.

- Aufwandsschätzung: L

- Akzeptanztest:

	- Man kann in einem Screen den QR-Code für das WIFI scannen, dessen Infos dann in dem Screen gezeigt werden und im selben Screen ohne Unterbrechung den QR-Code für mehrere Geräte des Rechenzentrum nacheinander scannen.

## Epic 2: Verwaltung von Abfragen

> Als Admin möchte ich bestimmte Abfragen kategorisiert und übersichtlich verwalten und benutzen können.

Beschreibung:

Die Abfragen, die der Admin tätigen will, kann mit der App in ein Abfragemaske gepackt werden, damit Abfragen gleichen Typs effizienter getätigt werden können. Eine Abfragemaske kann vorher defeniert werden. Dies sind eine bestimmte Menge von Abfragen, die immer gleichzeitig getätigt werden. Eine einzelne Abfrage ist die versuchte Ermittlung von Informationen.

### Feature 1 *Abfragenschema erstellen*

>Als Admin möchte ich Abfragenschemata erstellen können, um gleiche Abfragen schneller tätigen zu können.

- Aufwandsschätzung: S

- Akzeptanztest:

	- Abfragen können verwaltet (neu angelegt, bearbeitet, angesehen und gelöscht) werden.

	- Abfragen enthalten mindestens eine Bezeichnung, eine OID (min. Dot-Notation) den 					erwarteten Rückgabewert-Typ.

	- Abfragen können keiner, einer oder mehreren Kategorien (Art der Hardware) zugeordnet werden.

	- Abfragen können gestartet werden und das Result angezeigt werden.

### Feature 2 *Abfragenmenü*

> Als Admin möchte ich, dass ich die Art der Abfrage (Welche Hardware Kategorie) auswählen kann.

- Aufwandsschätzung: M

- Akzeptanztest:

	- Die Informationen, die man durch die Abfragen erhalten hat, werden in Menüs abgespeichert, bei denen man einzelne Kategorien als Untermenüs auswöhlen kann (Art der Hardware)

	- Die einzelnen Hardwareinfos werden in den zugehörigen Menüpunkten angezeigt.

	- Es gibt ein Menüpunkt zur Anzeige von Wlan Informationen.

	- Es gibt ein Menüpunkt zur Anzeige von Systemzuständen.

## Epic 3: Oberfläche

> Als Admin will ich eine gut strukturierte Oberfläche haben, die auch einfach und intuitiv zu bedienen ist.

Beschreibung:

Die Oberfläche sollte so einfach und intuitiv wie möglich gestaltet werden.

### Feature 1 *Start der App*

> Als Admin will ich, dass bei Start der App direkt die Kamera für die Scanfunktion an ist.

- Aufwandsschätzung: L

- Akzeptanztest:

	- Es wird direkt die Kamera gestartet bei Start der App.

	- Die App muss beim ersten Start nach der Berechtigung für die Kamera fragen.

	- Oben Rechts gibt es den Button für "Flashlight".

	- In der unteren Hälfte gibt es den Button "QR Code Scannen".

### Feature 2 *Sprachmenü*

> Als Admin möchte ich, dass ich in einem seperaten Menü die Sprache ändern kann.

- Aufwandschätzung: S

- Akzeptanztest:

	- Man kann im Menü ein Menüpunkt Optionen auswählen.

	- Der Button Sprachauswahl ist in den "Optionen".

	- Es sollten die Sprachen Deutsch und Englisch auswählbar sein.

	- Bei Auswahl der Sprache sollte die Richtige Sprache angezeigt werden.

	- Es sollten zur Erleichterung nur die Beiden jeweiligen Flaggen angezeigt werden im Button 		(DE/EN).

### Feature 3 *Hintergrundfarbe*

> Als Admin möchte ich, dass die Hintergrundfarbe Standardhintergrundfarbe RAL 7004 ist und der Text strandardmäßig weiß ist. Die textfarbe ist änderbar in einem seperaten Menü.

- Aufwandsschätzung: XS

- Akzeptanztest:

	- In den "Einstellungen gibt es ein Menü für die Hintergrundfarbe.

	- Standartmäßig ist die Farbe RAL 4007 als Hintergrundfarbe eingestellt und Schwarz als Text eingestellet.

	- Es gibt noch die Option den Text auf schwarz einzustellen.

### Feture 4 *Menü*

> Als Admin möchte ich, dass es ein Menü gibt indem ich die Sprache und die Benutzeroberfläche einstellen kann und auch die Abfragen nochmal ansehen kann.

- Aufwandsschätzung: S

- Akzeptanztest:

	- Der Button für "Menü" ist oben Links beim "Start der App".

	- Im "Menü" gibt es den Button Für "Optionen", "verbundene Geräte", "WLAN-Verbindung", "Abfragenmenü" und "Hilfe".

