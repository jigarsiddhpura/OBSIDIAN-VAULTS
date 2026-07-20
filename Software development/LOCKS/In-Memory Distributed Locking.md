

#Concurrency #Database 

## 1. ELI5
* Request -> Redis lock (fast, in-memory) -> DB update (single writer) -> (redis unlock) 

## 2. Problem it solves
* [[Double Booking]]

## 3. How it works
* ```sql
  SET lock:event:e1:seat:A1 user-1234 NX PX 5000
  -- `NX` → acquire lock only if it doesn’t exist
  -- `PX 5000` → auto-expire lock after 5 seconds

  begin transaction;
  
  #S1 
  select status from seats
  where event_id='e1' and seat_id='A1'
  
  #S2
  update seats
  set status='reserved', user_id='1234', reserved_at=NOW()
  where event_id='e1' and seat_id='A1' and status = 'available';
  
  commit; 
  ```

* ![[double-booking-prevented-by-in-memory-distributed-locking.png]]

## 4. Trade-offs

#### Pros
* High concurrency - db is no longer the bottleneck 
* High performance - redis + reduced db load

#### Cons
* Cache unavailability
	* may lead to db load spike
	* **lock will disappear** -> another request acquires lock on the same row -> DOUBLE BOOKING 
	* *SOLUTION: DB `update ... where status='available'` protects correctness*
* App crash before releasing lock
	* *SOLUTION: define TTL for each lock -> seat becomes bookable again* 


## 5. Real world Examples

* Airbnb (via Redis / ElastiCache) at scale for caching and coordination tasks; teams often leverage Redis locks for controlling things like inventory or workflow concurrency in microservices
* Google’s internal distributed lock service, _Chubby_, is used to coordinate master election and mutual exclusion across core systems like Bigtable and the Google File System — a classic _distributed lock manager_ use case

## 6. When to use

* Need to handle *1K-10K requests/sec* traffic seamlessly


> [!info] What if the cache crashes just after a seat lock is acquired? Would another request override the seat booking leading to double booking?
> If the cache (the distributed lock store) crashes immediately after the lock is acquired, **the lock is lost** because the lock state is held _ephemerally_ inside the cache.
> 
> In that situation:
> 	- The application **assumes the lock is still held**.
> 	- But the lock system (Redis/Memcached/etc.) **forgets all locks on restart**.
> 	- A second request comes in, attempts to acquire the same lock key, and sees **no key present**.
> 	- It acquires the lock again.
> 	- Now you have **two threads both believing they hold the same lock**, leading to a potential **double booking**.
> 
>