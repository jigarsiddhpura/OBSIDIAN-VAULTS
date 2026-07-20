
# Topic: [e.g., MySQL High Availability]

**Context:** #Architecture #Database #HighAvailability #Scalability
**Criticality:** [High/Medium/Low - How vital is this for uptime?]

## 1. The "Why" (The Failure Scenario)
*What happens if we DON'T have this?*
> *Example: If the primary DB goes down without HA, the app goes offline, and we lose $$$ until a human manually promotes a new server.*

## 2. High-Level Architecture (The Topology)
*Sketch the flow or describe the nodes.*
* **Node A (Primary):** Handles Reads/Writes.
* **Node B (Replica):** Syncs data from Primary.
* **The "Switcher" (Proxy/Orchestrator):** How does the app know which one is alive? (e.g., ProxySQL, Orchestrator).

## 3. The "Gotchas" (The Trade-offs)
Architecting is the art of picking which "poison" you can live with. Use the **CAP Theorem** lens here.
* **Latency vs. Consistency:** Does the Primary wait for the Replica to confirm (Synchronous) or just send and forget (Asynchronous)?
* **Complexity:** Is this harder to debug? (Yes, distributed systems always are).

## 4. Failure Modes & Recovery (The Most Important Part)
| Scenario | What Happens? | Recovery Time (RTO) |
| :--- | :--- | :--- |
| Primary DB crashes | Orchestrator detects heart-beat failure. | < 30 seconds (Auto-failover) |
| Network Split (Brain) | Both nodes think they are Primary. | Risk: Data Corruption. |
| Replica Lag | Replica is 5 mins behind Primary. | Risk: Users see "old" data. |

## 5. Decision Matrix (When to use what?)
* **Standard Master-Slave:** Good for read-heavy apps (e.g., Blogs).
* **Multi-Master (Group Replication):** Good for high-uptime requirements where any node can die.
* **Galera Cluster:** Use when you need strict consistency across all nodes.

## 6. Verification Commands / Monitoring
*How do I prove this is working right now?*
```sql
-- Check slave status
SHOW SLAVE STATUS\G;
-- Check replication lag
SELECT NOW() - info_date FROM heartbeat_table;