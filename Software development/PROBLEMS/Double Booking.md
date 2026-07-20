
Booking system reserves ticket using the below queries

## S1 - To check availability
```sql
Begin transaction;

select * from seats
where event_id='e1' and seat_id='A1' and status='available' 
```

## S2 - Update seat status
```sql
update seats
set status='reserved', user_id='1234', reserved_at=NOW()
where event_id='e1' and seat_id='A1';
commit;
```

![[reservation-system-architecture.png]]

![[double-booking-in-action.png]]

In the end, the service will give a successful response to both Alice and Bob. Both would think that the ticket is assigned to them. However, in the database, the ticket would be assigned to **Bob**.

## Solutions

[[Pessimistic Lock]]
[[Optimistic Lock]]
[[In-Memory Distributed Locking]]
[[Virtual waiting queue]]

## Pros/Cons of each solution

![[pros-cons-double-booking-solutions.png]]

## Sample Schema

```sql

-- seats: source of truth for seat state
CREATE TABLE seats (
  event_id    varchar(64) NOT NULL,
  seat_id     varchar(64) NOT NULL,
  status      varchar(16) NOT NULL, -- 'available' | 'reserved' | 'booked'
  user_id     varchar(64),
  reserved_at timestamptz,
  PRIMARY KEY (event_id, seat_id)
);

-- booking_requests: virtual waiting queue (DB-backed queue)
CREATE TABLE booking_requests (
  id          bigserial PRIMARY KEY,
  event_id    varchar(64) NOT NULL,
  seat_id     varchar(64) NOT NULL,
  user_id     varchar(64) NOT NULL,
  status      varchar(16) NOT NULL DEFAULT 'PENDING', -- 'PENDING'|'PROCESSING'|'DONE'|'FAILED'
  created_at  timestamptz NOT NULL DEFAULT now(),
  picked_at   timestamptz,
  processed_at timestamptz,
  worker_id   varchar(64),
  error       text
);

-- bookings: record of reservations/payments
CREATE TABLE bookings (
  id          bigserial PRIMARY KEY,
  event_id    varchar(64) NOT NULL,
  seat_id     varchar(64) NOT NULL,
  user_id     varchar(64) NOT NULL,
  status      varchar(16) NOT NULL, -- 'reserved'|'booked'|'expired'|'cancelled'
  created_at  timestamptz NOT NULL DEFAULT now(),
  paid_at     timestamptz
);

```
