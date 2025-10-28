# INSY REST API (Spring Boot + PostgreSQL)

REST-Backend für das VENLAB-System.
CRUD für `Sample`, `Analysis`, `BoxPos` inklusive **Composite IDs**.  
Swagger UI verfügbar. CI Pipeline auf GitHub Actions vorhanden.

---

## Features

- Spring Boot 3 + JPA (Hibernate)
- PostgreSQL mit Schema `venlab`
- `.env` Variablen für DB-Verbindung
- **Composite Keys** (`SampleId`, `BoxPosId`)
- REST Controller über `AbstractCrudController`
- Swagger UI Dokumentation
- E2E Tests mit realer PostgreSQL-Datenbank
- GitHub Actions CI Pipeline (build + test)

---

## Umgebungsvariablen (`.env`)

Das Projekt liest DB Credentials aus `.env`.

Erstelle eine `.env` im Projekt-Root mit:
```env
POSTGRES_DB=venlab  
POSTGRES_USER=postgres  
POSTGRES_PASSWORD=postgres
```

---

## Lokale Entwicklung

PostgreSQL starten (Beispiel mit Docker):
```bash
docker run -d --name venlab-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=venlab -p 5432:5432 postgres:13-alpine
```
Schema anlegen: `psql -h localhost -U postgres -d venlab -c "CREATE SCHEMA IF NOT EXISTS venlab;`

Back-End starten:
mvn spring-boot:run

---

## Server & Swagger UI

API läuft nach dem Start unter:
http://localhost:8040/api

Swagger UI erreichbar unter:
http://localhost:8040/swagger-ui/index.html

---

## Modellierung: Composite IDs

### SampleId
Sample hat eine zusammengesetzte ID bestehend aus:
- `s_id` (String)
- `s_stamp` (LocalDateTime)

Bei GET `/api/samples/{id}` wird die ID nicht als JSON übergeben, sondern als einzelner String im Format:
`s_id,s_stamp`

Beispiel:
`2122900311111,2023-07-27T12:37:06`

Dies gilt für alle Endpoints mit SampleId als Path-Parameter.

---

### BoxPosId
BoxPos hat eine zusammengesetzte ID bestehend aus:
- `bPosId` (Integer)
- `bId` (String)

Swagger Format:
`bPosId,bId`

Beispiel:
`3,BOX-44`

---

## E2E Tests

Die End-to-End Tests laufen gegen eine echte PostgreSQL Instanz.
Dies wird über die CI Pipeline & lokale Docker-Datenbank ermöglicht.

Testbasis:
- `BaseE2ETest` stellt:
    - Random Port
    - `TestRestTemplate`
    - `uniqueId()`, `timestamp()`, `baseUrl()` Helper bereit

Jeder E2E Test ruft echte Endpoints auf, z.B. `/api/analysis`.

---

## Projektstruktur

src/main/java/com/.../controller     → REST Controller  
src/main/java/com/.../service       → Geschäftslogik  
src/main/java/com/.../repository    → JPA Repositories  
src/main/java/com/.../entity        → Entities & Composite IDs  
src/test/java/.../controller        → E2E Tests

---

## CI Pipeline (GitHub Actions)

Die CI Pipeline führt Folgendes aus:
1) Startet PostgreSQL (Docker Service)
2) Legt Schema `venlab` an
3) Führt `mvn clean verify` aus
4) Lädt Testreports als Artifact hoch

Damit wird sichergestellt, dass:
- E2E Tests zuverlässig laufen
- keine lokal-spezifischen DB Abhängigkeiten bestehen
- Build fehlschlägt, wenn CRUD- oder API-Verhalten bricht

---
