
- **Check `rows_affected`** on `UPDATE` to detect races and take appropriate action (rollback / mark failed).

- Use **`FOR UPDATE SKIP LOCKED`** in queue consumers to allow multiple workers to consume without collision.

- Prefer **conditional UPDATE** (`WHERE status='available'`) for the lowest DB contention if you have a queue layer already serializing requests.

- Keep critical transactions **short** (no external HTTP calls inside the DB transaction). If you need external calls (payment), do them outside and then finalize atomically.

- Consider **dead-letter / retry** logic for requests that repeatedly fail.

- Expiry job must be idempotent and safe to run in parallel (use `FOR UPDATE SKIP LOCKED`)