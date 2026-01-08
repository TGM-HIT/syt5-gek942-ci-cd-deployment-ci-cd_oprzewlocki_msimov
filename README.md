# INSY E2E Tests

**Verfasser:** Arbel Itach, Oliwier Przewklocki
**Datum:** 8.1.2026


## 1. Einführung

In dieser Übung werden E2E-Tests für eine  CRUD-Webapplikation  implementiert. Ziel ist es, das vollständige Programm inklusive Benutzeroberfläche in Kombination mit der vorhandenen ReST-Schnittstelle zu testen.

Die Tests überprüfen reale Benutzerinteraktionen im Browser und stellen sicher, dass alle CRUD-Operationen (Create, Read, Update, Delete) wie erwartet funktionieren. Dabei wird das Programm als Gesamtsystem verwendet und nicht isoliert.


## 2. Zielsetzung

Ziel dieser Übung ist das Überprüfen des Frontensd mithilfe automatisierter E2E-Tests.

Tabellen die getestet werden sind zB:

- **Analysis** (CRUD)
- **Sample** (CRUD)
- **BoxPos** (CRUD)

Die Tests sollen reproduzierbar über die Konsole gestartet werden können und eine transparente output zum Test liefern.

## 3. Voraussetzungen

- Docker & Docker Compose
- Node.js (npm oder yarn)
- Funktionstüchtiges ReST-Backend
- Funktionstüchtige Vue-Frontend
- Grundlagen zu E2E-Tests



## 4.  Arbeitsschritte

### 4.1 Implementierte Features

Im Rahmen dieser Übung wurden folgende Komponenten und Features eingesetzt bzw. erweitert:

- Bestehendes ReST-Backend als Datenquelle
- Vue.js Frontend mit bestehenden CRUD-Views
- E2E-Tests zur Überprüfung der Benutzeroberfläche
- Automatisierte Testausführung über npm/yarn


### 4.2 Umgebungsvariablen

Für den Betrieb des Systems werden Umgebungsvariablen verwendet, um Datenbank- und Servicekonfigurationen flexibel zu halten. Diese Variablen werden über eine `.env`-Datei im Projekt-Root definiert.

Beispiel:

POSTGRES_DB=venlab  
POSTGRES_USER=postgres  
POSTGRES_PASSWORD=postgres

Dadurch kann das Programm lokal als auch in einem Container ohne Änderungen laufen ohne Passwörter oder Usernames öffentlich zu machen.



### 4.3 Server & API-Dokumentation

Zusätzlich zu den E2E Tests steht eine Swagger UI zur Verfügung, welche alle Endpoints dokumentiert. Diese dient als Referenz für die Entwicklung der Tests sowie zur manuellen Überprüfung der API-Funktionalität.


### 4.4 Umsetzung der E2E-Tests

Die End-to-End-Tests simulieren reale Benutzeraktionen im Browser. Für Tabellen wie **Analysis**, **Sample**, **Box** und **BoxPos** werden vollständige CRUD-Abläufe getestet:

- Anlegen neuer Datensätze
- Bearbeiten bestehender Einträge
- Löschen von Datensätzen
- Überprüfung der korrekten Daten nach jeder Veränderung



### 4.5 Testausführung & Testbasis

Die Tests laufen natürlich gegen eine echte Datenbank. Dadurch wird garantiert dass das Ergebnis auch bei der richtigen Nutzung gleich ausfallen wird.
Die Testausführung dabei über die Konsole.



### 4.6 Testprotokolle & Reports

Zur Dokumentation des Testfortschritts werden automatisch Feedback generiert. Diese enthalten Informationen wie:

- Erfolgreiche Testfälle
- Fehlgeschlagene Tests
- Laufzeiten der einzelnen Szenarien

## 5. Zusammenfassung

Die implementierten E2E-Tests stellen sicher, dass das Frontend als auch das Backend von einem Ende zum anderen komplett funktioniert ohne einen Teil zu isolieren und alle relevanten Funktionen abgedeckt sind.


## 6. Quellen

[1] Vue.js, “Testing — End-to-End (E2E) Testing,” Vue.js Official Guide. [Online]. Available: https://vuejs.org/guide/scaling-up/testing.html. [Accessed: 20.12.2025].

[2] Cypress.io, “Writing Your First End-to-End Test,” Cypress Documentation. [Online]. Available: https://docs.cypress.io/guides/end-to-end-testing/writing-your-first-end-to-end-test. [Accessed: 27.12.2025].

[3] Cypress.io, “Best Practices,” Cypress Documentation, 2024. [Online]. Available: https://docs.cypress.io/guides/references/best-practices. [Accessed: 8.1.2026].