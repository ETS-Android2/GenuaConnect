# Genua Connect

Screenshot of Genua Connect (in use) / in app view by Arton Kastrati
![Screenshot of Genua Connect / in app view by Samuel Gigliotti]
(doc/images/Appscreenshot4.jpg)

Screenshot of Genua Connect (in use) / in app view by Arton Kastrati
![Screenshot of Genua Connect / in app view by Samuel Gigliotti]
(doc/images/Appscreenshot3.jpg)

Screenshot of Genua Connect (in use) / in app view by Arton Kastrati
![Screenshot of Genua Connect / in app view by Samuel Gigliotti]
(doc/images/Appscreenshot2.jpg)

Screenshot of Genua Connect (in use) / in app view by Arton Kastrati
![Screenshot of Genua Connect / in app view by Samuel Gigliotti]
(doc/images/Appscreenshot1.jpg)

Screenshot of Genua Connect (in use) / in app view by Samuel Gigliotti


Administratoren (Admins) müssen operative Tätigkeiten an Rechnern/Firewalls in den Rechenzentren tätigen (Updates, Abstürze untersuchen, Hardware-Defekte).
Bei solchen Arbeiten ist nebem dem Finden der richtigen Hardware der Zustand der Appliance wichtig: 
Wurden für die Wartungsarbeiten bespielsweise die produktiven IP-Adressen an das High Availability (HA)-Pendant im anderen Rechenzentrum-Abschnitt abgegeben?
Oftmals sind Wartungsfenster zeitlich sehr kurz bemessen oder zu Zeiten, bei denen eine zügige Bearbeitung erforderlich ist.
Ein App, die den Status der Maschine ermittelt und anzeigt ist eine erhebliche Erleichterung für die Durchführung und Kontrolle von Wartungsarbeiten.

Genua diese Problematiken löst die Application *Genua Connect*. Sie ermöglicht es dem Nutzer, mittels einem QR-Code, sich mit dem Intranet zu verbinden. Ändert sich das Passwort
so erhält der Nutzer eine Benachrichtigung und wird geben den QR-Code erneut zu scannen.
Ist die Verbindung sicher und es verbleibt genügend Zeit bis zur nächsten Passwort aktualisierung des Intranets, so kann der Nutzer sich zu einem Gerät begeben und mittels dem inbegriffenen
QR-Code scanner, über vordefinierte Abfragemasken, sich die gewünschten Informationen anzeigen zu lassen.

## Features

### Critical Features

#### Verbindung und Abfragenanzeige

- Einlesen der WLAN-Zugangsdaten ist via QR-Code möglich.

- Nach dem Einscannen ist das Smartphone mit dem vorgegebenen WLAN verbunden.

- Bei sich ändernden WLAN-Zugangsdaten (Änderung Passwort, aber Access-Point-Name und MAC gleich) sind keine weiteren Benutzer-Interaktionen (außer QR-Code scannen) nötig.

- Die Netzwerkparameter des WLANs (IPv4-, IPv6-Adresse, Netzmaske, DNS-Server-IP, Gateway-IP) inkl. DHCP-Zuweisung werden dargestellt.

- In unverschlüsselte WLANs bucht sich die App nicht ein, auch nicht im Fehlerfall.

- Beim Wechsel in ein unverschlüsseltes WLAN wird eine Fehlermeldung angezeigt und es werden keine Daten übertragen.

- Beim Wechsel in das gesicherte WLAN wird der Betrieb der Anwendung wieder im ursprünglichen Zustand aufgenommen.

- Einlesen der Geräte-Daten und -Zugangsdaten ist via QR-Code möglich.

- Standard-Abfragen (1.3.6.1.2.1.1) werden vom Gerät abgefragt und angezeigt.

- Gestartete Abfragen werden periodisch erneut abgefragt.

#### Verwalten von Abfragen

- Abfragen können verwaltet (neu angelegt, bearbeitet, angesehen und gelöscht) werden.

- Abfragen enthalten mindestens eine Bezeichnung, eine OID (min. Dot-Notation) den erwarteten Rückgabewert-Typ.

- Abfragen können keiner, einer order mehreren Kategorien (Art der Hardware) zugeordnet werden.

- Abfragen können gestartet werden und das Result angezeigt werden.

### Additional Features

- App kann auf das Handylicht zugreifen und dieses aktivieren.
- Ein Sprachmenü indem man die Sprache einstellen kann.

## Installation

1. Repository klonen: `git clone`
2. Android Studio Projekt öffnen
3. Android Studio Projekt bauen
4. Android Studio Projekt im Emulator ausführen oder APK erstellen lassen

## Verwendung der App

**TODO:** Beschreibung der wichtigsten Anwendungsfälle

### Wichtiger Anwendungsfall 1

### Wichtiger Anwendungsfall 2

## Changelog

Die Entwicklungsgeschichte befindet sich in [CHANGELOG.md](CHANGELOG.md).

## Verwendete Bibliotheken

implementation 'com.journeyapps:zxing-android-embedded:3.6.0'

api 'org.snmp4j:snmp4j:2.5.0'

## Lizenz

**TODO Lizenz nennen**. Genaue Bedingungen der Lizenz können in [LICENSE](LICENSE) nachgelesen werden.