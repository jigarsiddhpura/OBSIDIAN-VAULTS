

#Concurrency #Database 

## 1. ELI5
* 
## 2. Problem it solves
* [[Double Booking]]

## 3. How it works
* ```sql
  
  #S1 
  -- API: Check seat status (fast read)
  select status, user_id, reserved_at from seats
  where event_id='e1' and seat_id='A1'
  
  #U1 
  -- API: Add request to the virtual waiting queue
  Uptdate into booking_requests (event_id, seat_id, user_id)
  values ('e1', 'a1', '1234')
  Returning id;
  -- return queue id to the caller so they can poll status or be notified
  
  #S2 
  -- Worker: claim a request from the queue
  -- MULTIPLE WORKERS poll the queue
  -- `FOR UPDATE SKIP LOCKED` prevent multiple workers to take the same row
  -- Worker transaction to claim 1 pending request
  BEGIN;
  -- Find one pending request and lock it so other workers skip it
  SELECT id, event_id, seat_id, user_id
  FROM booking_requests
  WHERE status = 'PENDING'
  ORDER BY created_at
  LIMIT 1
  FOR UPDATE SKIP LOCKED;
  
  -- If no row returned:
  commit; -- and worker sleeps/asks again.
    
  -- If row returned (`id = 42`), then mark it as processing:
  UPDATE booking_requests
  SET status = 'PROCESSING', worker_id = 'worker-1', picked_at = now()
  WHERE id = 42;
  COMMIT;
  
  #U2 
  -- Worker: try to reserve seat in DB transactions
  BEGIN;

  -- Try atomic claim
  UPDATE seats
  SET status = 'reserved', user_id = 'user-123', reserved_at = now()
  WHERE event_id = 'E1' AND seat_id = 'A1' AND status = 'available';
	
  -- check rows_affected
  -- If = 1 -> insert booking record
  INSERT INTO bookings (event_id, seat_id, user_id, status)
  VALUES ('E1', 'A1', 'user-123', 'reserved') 
  RETURNING id;
	
  COMMIT;
  
  #U3
  -- Worker: Acknowledge queue (on success / failure)
  -- After worker commits the seat change, mark queue row accordingly.
  
  -- On success:
  UPDATE booking_requests
  SET status = 'DONE', processed_at = now(), worker_id = 'worker-1'
  WHERE id = 42;
  
  -- If seat was already taken (rows_affected = 0):
  UPDATE booking_requests
  SET status = 'FAILED', processed_at = now(), error = 'seat not available'
  WHERE id = 42;
  -- If transient error, you may set status to `'PENDING'` or `'RETRY'` with           retry_count.
  
  #U4
  -- Finalize booking after payment (idempotent)
  -- When payment completes, mark booking and seat as `booked`. Use conditional updates to avoid accidental overwrites.
  
  BEGIN;

  UPDATE bookings
  SET status = 'booked', paid_at = now()
  WHERE id = 100 AND status = 'reserved';
	
  UPDATE seats
  SET status = 'booked'
  WHERE event_id = 'E123' AND seat_id = 'A1' AND user_id = 'user-123' AND status =     'reserved';
	
  COMMIT;
  -- If rows_affected = 0 on either update, treat as idempotent/no-op or fail depending on business logic.
  
  #CLEAN
  -- Expire stale reservations (background job)
  -- A scheduled job frees seats that stayed `reserved` beyond TTL (e.g., 10 minutes). Use `FOR UPDATE SKIP LOCKED` to parallelize safely across multiple workers.
  -- Worker picks a batch of reserved seats that expired
	BEGIN;
	
	SELECT event_id, seat_id, user_id
	FROM seats
	WHERE status = 'reserved' AND reserved_at < now() - INTERVAL '10 minutes'
	FOR UPDATE SKIP LOCKED
	LIMIT 100;
	
	-- For each selected row:
	UPDATE seats
	SET status = 'available', user_id = NULL, reserved_at = NULL
	WHERE event_id = 'E123' AND seat_id = 'A1';
	
	-- Update bookings referencing those seats:
	UPDATE bookings
	SET status = 'expired'
	WHERE event_id = 'E123' AND seat_id = 'A1' AND status = 'reserved';
	
	COMMIT;

  ```

* [[Safety & Idempotency checklist]]

* ![[seat-reservation-using-virtual-waiting-queue.png]]

## 4. Trade-offs

#### Pros
* Scalable
* Fairness due to FIFO

#### Cons
* Complexity due to
	* queueing layer
	* Server-sent Events (SSE)


## 5. Real world Examples

* Coldplay concerts

## 6. When to use

* Need to handle *10K+ requests/sec* traffic seamlessly



> [!info] At what RPS, should one pivot to a Virtual Waiting Queue based system? (10K RPS, 50 KRPS or 100K RPS?)
> 
> There is **no fixed RPS threshold**, but in practice **systems pivot to a Virtual Waiting Room when they reach ~20–30K RPS _on a single hot resource_** (like a popular seat category, a flash-sale item, or a concert event page).


