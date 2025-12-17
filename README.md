# DietiEstates

DietiEstates è un portale immobiliare full‑stack (Angular frontend + Spring Boot backend) sviluppato come monolite modulare con pattern DDD. Supporta autenticazione (JWT / OIDC), gestione annunci, ricerche geospaziali (PostGIS), gestione immagini su Amazon S3 e ruoli/permessi per agenti e amministratori.

## Tecnologie principali

- Backend: Java, Spring Boot, Spring Data JPA, Hibernate, Postgres/PostGIS
- Frontend: Angular (TypeScript), Bootstrap, Leaflet (mappe)
- Storage oggetti: Amazon S3 (S3ImageStorageService)
- Build: Maven (backend), npm / Angular CLI (frontend)

## Struttura del repository (sintetica)

- `dietiestates-backend/` — applicazione Spring Boot
  - `src/main/java/it/unina/ingsw/dietiestates_backend/` — codice sorgente (package: auth, listings, agency, file, security, email, errors)
  - `sql/schema_iniziale.sql` — dump iniziale, estensioni PostGIS
  - `docker-compose.yml` — compose helper per sviluppo/DB (verificare file)
- `dietiestates-ui/` — applicazione Angular (UI)
- `docs/` — documentazione e capitoli (LaTeX)

## Requisiti di sviluppo

- Java 17+
- Maven
- Node.js 16+ / npm
- PostgreSQL con estensione PostGIS
- (opzionale) Docker / Docker Compose

## Configurazione database

1. Creare un database PostgreSQL e abilitare le estensioni richieste (`postgis`).
2. Eseguire lo script `dietiestates-backend/sql/schema_iniziale.sql` per creare tabelle, funzioni e dati iniziali.
3. Configurare le proprietà di connessione in `dietiestates-backend/src/main/resources/application-dev.yml` o utilizzare variabili d'ambiente (`spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`).

## Configurazione S3 (gestione immagini)

Le credenziali e il bucket S3 vengono lette tramite le proprietà nel backend. Variabili/env da configurare:

- `aws.access-key`
- `aws.secret-key`
- `aws.region`
- `aws.s3.bucket-name`

La classe `S3Config.java` crea il client AmazonS3 e `S3ImageStorageService` espone upload/delete/presign. In produzione preferire IAM role/temporary credentials invece di chiavi statiche.

## Avvio locale (suggerito)

Backend (da `dietiestates-backend`):

```bash
# build
./mvnw clean package

# lanciare direttamente con Spring Boot
./mvnw spring-boot:run

# oppure eseguire jar
java -jar target/dietiestates-backend-0.0.1-SNAPSHOT.jar
```

Frontend (da `dietiestates-ui`):

```bash
npm install
# avvia dev server
npm start # oppure `ng serve` se disponibile
```

Con Docker Compose (se il file è predisposto):

```bash
cd dietiestates-backend
docker-compose up --build
```

## Note operative e best practices

- Le immagini vengono salvate su S3; il DB memorizza solo metadati (`listing_image.url`, `sort_order`, `created_at`).
- Per coerenza tra S3 e DB si applica il pattern "upload-first + DB-insert + compensating delete" in caso di errore.
- Usare indici GIST su campi spaziali (`CREATE INDEX idx_listing_coordinate ON listing USING GIST (coordinate);`) per performance di ricerca.
- Le operazioni che assegnano ruoli e agency devono essere eseguite in una singola transazione per rispettare eventuali trigger DB.

## Documentazione

- Capitoli e diagrammi: `docs/chapters/` (LaTeX).
- Schema DB: `dietiestates-backend/sql/schema_iniziale.sql`.
- Implementazione storage: `dietiestates-backend/src/main/java/.../file/S3ImageStorageService.java` e `ImageStorageService.java`.

## Test e CI

- Eseguire i test con Maven: `./mvnw test`.
- Per test di integrazione con S3 usare un mock (LocalStack) o un bucket di staging.

## Contatti e contributi

Apri una issue o una pull request nel repository; includi i log e i passi per riprodurre eventuali problemi. Per domande rapide vedi i commenti nelle classi principali (`service`, `controller`, `repository`).
