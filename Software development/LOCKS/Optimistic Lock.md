

#Concurrency #Database 

## 1. ELI5
* Here we use an extra column ==(version)== to check if seat is available and update *only if* ==version== is unchanged

## 2. Problem it solves
* [[Double Booking]]

## 3. How it works
* ```sql
  begin transaction;
  
  #S1 
  select seat_id, status, version from seats
  where event_id='e1' and seat_id='A1';
  
  #S2
  update seats
  set status='reserved', user_id='1234', reserved_at=NOW(),      version = version + 1
  where event_id='e1' and seat_id='A1' and status='available'    and version=42;
  
  commit; 
  ```
* ![[OL-select-seats-and-read-version.png]]
* ![[OL-second-update.png]]

## 4. Trade-offs

#### Pros
* Better read performance than pessimistic lock.
* Better throughput than [[Pessimistic Lock]], as more queries can execute concurrently.
* Can handle *moderate traffic* for less popular event.

#### Cons
* Poor user experience due to *last minute failure -> repeated process for selecting seats*

## 5. When to use

* Retries are accepted by the user (profile update/shopping cart update)
* Incrementing a “likes” count using a version field (classic example)


> [!info] Instead of checking the version, can the query rely solely on the `status` while updating the record’s state? (Leave your thoughts in the comments below)
> You _can_, but you absolutely _shouldn’t_ in any production-grade, concurrency-heavy environment.
> 
> **1. Status is not monotonic — version is.**  
> `status` can cycle, repeat, or be derived from business workflows. `version` is guaranteed to move forward with every modification. That monotonic progression is what allows the system to reliably detect race conditions.
> 
> **2. Status is business logic; version is concurrency logic. Mixing the two creates fragility.**  
   If tomorrow the business introduces a new transitional state or merges two states, your “status-only locking” silently collapses. `version` remains stable and decoupled.

