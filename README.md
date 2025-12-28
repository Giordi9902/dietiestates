# DietiEstates

DietiEstates è una piattaforma web per la gestione e la ricerca di annunci immobiliari, sviluppata come progetto universitario. Il sistema è composto da un backend Java Spring Boot e un frontend Angular, con un'architettura moderna, sicura e scalabile.

## Struttura del progetto

- **dietiestates-backend/**: Applicazione backend sviluppata in Java con Spring Boot. Gestisce autenticazione, autorizzazione, gestione utenti, annunci, agenzie, email e sicurezza.
- **dietiestates-ui/**: Applicazione frontend sviluppata in Angular. Offre un'interfaccia utente moderna e responsive per la ricerca, la visualizzazione e la gestione degli annunci.
- **docs/**: Documentazione tecnica e presentazioni.

## Funzionalità principali

- Registrazione e autenticazione utenti (anche OAuth2)
- Gestione annunci immobiliari (creazione, ricerca, modifica, eliminazione)
- Gestione agenzie e agenti
- Gestione profilo utente
- Notifiche email (attivazione account, cambio password, ecc.)
- Sicurezza avanzata (JWT, ruoli, permessi)
- Validazione dati lato backend e frontend
- Interfaccia utente responsive e accessibile

## Tecnologie utilizzate

- **Backend**: Java 17, Spring Boot, Spring Security, JPA, Hibernate, PostgreSQL, JWT, Lombok
- **Frontend**: Angular 17+, TypeScript, Bootstrap, RxJS
- **DevOps**: Docker, Docker Compose, Maven

## Avvio rapido

### Prerequisiti

- Java 17+
- Node.js 18+
- Angular CLI
- Docker

### Backend

```sh
cd dietiestates-backend
./mvnw spring-boot:run
```

Oppure con Docker Compose:

```sh
docker-compose up --build
```

### Frontend

```sh
cd dietiestates-ui
npm install
npm start
```

L'app sarà disponibile su `http://localhost:4200`.

## Documentazione

- Documentazione API: `/v3/api-docs` e `/swagger-ui.html` (backend avviato)
- Documentazione tecnica: cartella `docs/`

## Autori

- Giorgio Di Fusco

## Licenza

Progetto a scopo didattico, tutti i diritti riservati.
