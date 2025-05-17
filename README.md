Почему это воспроизводит «глючный» прод-сценарий
	•	HikariCP по умолчанию сбрасывает флаг readOnly при возвращении в пул, но этот сброс не всегда охватывает уровень транзакции на сервере.
	•	conn.setReadOnly(true) отправляет на сервер SET SESSION CHARACTERISTICS AS TRANSACTION READ ONLY.
	•	При повторном getConnection() этот флаг может сохраниться на стороне PostgreSQL до DISCARD ALL.
	•	Поэтому следующий UPDATE на том же соединении падает.

Таким образом, вы эмулируете баг клиента в «чистой» среде без PgBouncer.

```bash
curl -X PUT http://localhost:8080/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Bob","email":"bob@example.com"}'
```

```bash
curl http://localhost:8080/api/v1/probe
```