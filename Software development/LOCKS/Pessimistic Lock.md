
#Concurrency #Database 

## 1. ELI5
* Lock the entire row and then make updates so no other person can make update at the same time

## 2. Problem it solves
* [[Double Booking]]

## 3. How it works
* ```sql
  begin transaction;
  
  #S1 
  select * from seats
  where event_id='e1' and seat_id='A1' and status='available'
  FOR UPDATE
  [[FOR-UPDATE]]
  
  #S2
  update seats
  set status='reserved', user_id='1234', reserved_at=NOW()
  where event_id='e1' and seat_id='A1';
  
  commit; 
  ```
* ![[locking-of-seat.png]]
* ![[successful-booking-of-alice-seat.png]]

## 4. Trade-offs

#### Pros
* High Consistency
* Simple to implement
* Suitable for high [[Contention]] scenarios

#### Cons
* High throughput due to slow execution
* Deadlock risk
* Can only scale to **~200 users**

## 5. Real world Examples

* Core banking ledger
* Stock exchange matching engine
* Medical Device Control (life-critical)
* Industrial PLC Control
* Air Traffic Control Coordination

> [!info] What would happen if S1 executes, but the database connection disconnects? Would the lock remain or get released?
> 
> The lock is **released automatically**.  
> 
> When the database connection drops, the database cleans up all locks held by that session.
> 
> **Detailed Reasoning (The “Why”)**
> When you perform `SELECT ... FOR UPDATE`, you are creating a **row-level lock within the transaction and session**. This lock is tightly coupled to the lifecycle of the DB session.
