# RSPL HR Onboarding Backend (Spring Boot + JWT + RBAC)

This backend matches the provided frontend (`main.HTML`) API contract:

- Base URL: `http://localhost:8080/api`
- Endpoints:
  - `POST /auth/login`
  - `GET /health`
  - `GET /hr/team`
  - `GET /hr/candidates?page=0&size=100`
  - `POST /hr/candidates/initiate`
  - `POST /hr/candidates/{id}/approve`
  - `POST /hr/candidates/{id}/reject`
  - `POST /hr/candidates/{id}/send-link`

## Roles (RBAC)

- `HR_ADMIN` → full access
- `HR_EXEC` → view + initiate + send-link
- `HR_MANAGER` → view + approve + reject

In-memory users:

- `hr_admin / Admin@123`
- `hr_exec / Exec@123`
- `hr_manager / Manager@123`

## Run

### Normal (no DEV endpoints)

```bash
mvn spring-boot:run
```

### Dev profile (enables `/api/dev/**`)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Postman quick test

1) Login

`POST http://localhost:8080/api/auth/login`

```json
{"username":"hr_admin","password":"Admin@123"}
```

Copy `data.token`.

2) Call protected endpoints with header:

`Authorization: Bearer <token>`

3) Dev: create digital signature (dev profile only)

`POST http://localhost:8080/api/dev/candidates/{id}/digital-signature`

Body (optional):
```json
{"signedBy":"dev_admin","markSigned":true,"width":600,"height":200}
```

This generates a dummy PNG signature (base64) and sets the candidate status to `SIGNED` so you can test approval.

## Notes

- Data is stored in-memory for demo/testing.
- Replace in-memory store with a database (JPA) when needed.
