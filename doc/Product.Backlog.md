# Product Backlog



## Epic 1: QR-Code scannen



> Als Admin möchte ich das Produkt nutzen um einen QR-Code scannen zu können.



Beschreibung:

Der Admin muss sich im Rechenzentrum mit dem bereitgestellten WLAN verbinden. Dies erledigt er, indem er ein QR-Code scannt. Außerdem kann der Admin mittels QR-Codes an den Geräten im Rechenzentrum Informationen dieser abfragen.



### Feature 1 *WLAN verbindung herstellen*

> Als Admin möchte ich, dass die App in der Lage ist einen QR Code auf einem Wlan Gerät zu scannen und sich mit diesem zu verbinden.

- Aufwandsschätzung: S

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

    - Informationen werden abgerufen und angezeigt

    - Standard-Abfragen (1.3.6.1.2.1.1) werden vom Gerät abgefragt und angezeigt.

    - Gestartete Abfragen werden periodisch erneut abgefragt.



### Feature 3 *Blitzlicht*



> Als Admin möchte ich, dass ich bei Bedarf das Handyblitzlicht aktivieren kann, um einen QR-Code auch bei nicht optimalen Lichtverhältnissen scannen zu können.



- Aufwandsschätzung: S



- Akzeptanztests:

	- Zugriff auf Handy-Blitzlicht.

	- Button für die Aktivierung ist gut sichtbar.



### Feature 4 *Multiscan*



> Als Admin möchte ich, dass ich den QR-Code für das Wlan und die Geräteverbindung einscannen kann



- Aufwandsschätzung: L



- Akzeptanztest:

	- Man kann einen QR-Code Scannen und während der anmeldung dann im 2. Screen den anderen QR-		Code scannen.



## Epic 2: Verwaltung von Abfragen



> Als Admin möchte ich bestimmte Abfragen kategorisiert und übersichtlich verwalten und benutzen können.



Beschreibung:

Die Abfragen, die der Admin tätigen will, kann mit der App in ein Abfragenschema gepackt werden, damit Abfragen gleichen Typs effizienter getätigt werden können.



###Feature 1 *Abfragenschema erstellen*

>Als Admin möchte ich Abfragenschemata erstellen können, um gleiche Abfragen schneller tätigen zu können.



- Aufwandsschätzung: S



- Akzeptanztest:

	- Abfragen können verwaltet (neu angelegt, bearbeitet, angesehen und gelöscht) werden.

	- Abfragen enthalten mindestens eine Bezeichnung, eine OID (min. Dot-Notation) den 					erwarteten Rückgabewert-Typ.

	- Abfragen können keiner, einer oder mehreren Kategorien (Art der Hardware) zugeordnet 				werden.

	- Abfragen können gestartet werden und das Result angezeigt werden.



### Feature 2 *Abfragenmenü*



> Als Admin möchte ich, dass ich die Art der Abfrage (Welche Hardware Kategorie) auswählen kann.



- Aufwandsschätzung: M



- Akzeptanztest:

	- Es gibt einzelne Menüpunkte für die einzelnen Kategorien der Abfragen (Art der Hardware)

	- Die einzelnen Hardwareinfos werden in den zugehörigen Menüpunkten angezeigt.

	- Es gibt ein Menüpunkt zur Anzeige von Wlan Informationen.



## Epic 3: Oberfläche



> Als Admin will ich eine gut strukturierte und ansehbare Oberfläche haben, die auch einfach und intuitiv zu bedienen ist.



Beschreibung:

Die Oberfläche sollte so einfach und intuitiv wie möglich gestaltet werden.



### Feature 1 *Start der App*



> Als Admin will ich, dass bei Start der App direkt die Kamera für die Scanfunktion an ist.



- Aufwandsschätzung: L



- Akzeptanztest:

	- Es wird direkt die Kamera gestartet bei Start der App.

	- Die App muss beim ersten Start nach der Berechtigung für die Kamera fragen.

	- Unten Links gibt es den Button "Multiscan".

		- Im Multiscan Modus ändert sich der Button zu "Multiscan beenden".

		- Den Button "QR-Code Scannen" gibt es für beide Screens jeweils.

	- Unten Rechts gibt es den Button für "Flashlight".

		- Im Multiscan-Modus ist der Button für "Flashlight" nur einmal verfügbar unten Links.

	- In der unteren Hälfte gibt es den Button "QR Code Scannen".



### Feature 2 *Sprachmenü*



> Als Admin möchte ich, dass ich in einem seperaten Menü die Sprache ändern kann.



- Aufwandschätzung: S



- Akzeptanztest:

	- Man kann in den Einstellungen ein Menü Sprachauswahl auswählen.

	- Der Button Sprachauswahl ist in den "Einstellungen".

	- Es sollten die Sprachen Deutsch und Englisch auswählbar sein.

	- Bei Auswahl der Sprache sollte die Richtige Sprache angezeigt werden.

	- Es sollten zur erleichterung nur die Beiden jeweiligen Flaggen angezeigt werden im Button 		(DE/EN).



### Feature 3 *Hintergrundfarbe*



> Als Admin möchte ich, dass die Hintergrundfarbe Standardhintergrundfarbe RAL 7004 ist und der Text strandardmäßig weiß ist. Die textfarbe ist änderbar in einem seperaten Menü.



- Aufwandsschätzung: XS



- Akzeptanztest:

	- In den "Einstellungen gibt es ein Menü für die Hintergrundfarbe.

	- Standartmäßig ist die Farbe RAL 7004 als Hintergrundfarbe eingestellt und Schwarz als Text 		eingestellet.

	- Es gibt noch die Option den Text auf schwarz einzustellen.



### Feture 4 *Einstellungen*



> Als Admin möchte ich, dass es ein Einstellungsmenü gibt.



- Aufwandsschätzung: S



- Akzeptanztest:

	- Der Button für "Einstellungen" ist oben Links beim "Start der App".

	- In den "Einstellungen" gibt es den Button Für "Sprachmenü" und

