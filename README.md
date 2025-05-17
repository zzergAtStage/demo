
# Приложение для воспроизведения "org.postgresql.util.PSQLException: ERROR: cannot execute UPDATE in a read-only transacation"

## Installation and Tests
clone, clean, package
`mvn clean package`

Build with docker compose:
in project's root folder: `docker-compose -f Docker-compose.yaml up -d --build`

## After testing don't forget to delete unused images
`docker image rm demo_app`

### Swagger UI
`http://localhost:8080/swagger-ui/index.html#/user-controller/update`

Set Connection read-only: 
`http://localhost:8080/swagger-ui/index.html#/user-controller/probeReadOnly`


### Curl
```bash
curl -X PUT http://localhost:8080/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Bob","email":"bob@example.com"}'
```

```bash
curl http://localhost:8080/api/v1/probe
```

# Ideas
Цитата из неизвестного источника:
Почему это воспроизводит «глючный» прод-сценарий
	•	HikariCP по умолчанию сбрасывает флаг readOnly при возвращении в пул, но этот сброс не всегда охватывает уровень транзакции на сервере.
	•	conn.setReadOnly(true) отправляет на сервер SET SESSION CHARACTERISTICS AS TRANSACTION READ ONLY.
	•	При повторном getConnection() этот флаг может сохраниться на стороне PostgreSQL до DISCARD ALL.
	•	Поэтому следующий UPDATE на том же соединении падает.

Таким образом, вы эмулируете баг клиента в «чистой» среде без PgBouncer.

