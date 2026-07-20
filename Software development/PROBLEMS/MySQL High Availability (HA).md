
**Context:** #Architecture #Database #HighAvailability #Scalability
**Criticality:** Medium

## 1. The "Why" (The Failure Scenario)
*What happens if we don't have this*

> *The high availability of the primary node is essential for the cluster to continue accepting ‘Writes’. As hardware failures can happen in data centers, the primary node can fail. Upon failure of the primary node, the monitoring system should trigger the recovery workflow to promote one of the replicas as the new primary. This is called the _fail-over_ _process_. ==Post the fail-over, clients should be able to identify the new primary node in the cluster and redirect the ‘Writes’ to the new primary.==*

## 2. High-Level Architecture (The Topology)

![[high-availability-architecture.png]]

> [!note] The primary note is detected via the metadata flag 

The fail-over process involves 5 steps:

#### A. Failure Detection

![[failure-detection.png]]

- **Agent** runs as a daemon along with MySQL process. Health metrics sent to ELB are ==state of the instance, disk usage, replication status and lag, and state of MySQL process==
- **Monitor** compares previous health with the latest health received from the agent and notifies the orchestrator incase of any any failure / breach ( disk usage, replication lag, etc.)
- **Orchestrator** checks false positives and triggers the recovery workflow in case the failure is guaranteed.

#### B. Detecting False positives

> *Why is this step necessary?*
> Scenarios like temporary network failures or MySQL restarts are automatically resolved and do not require a failover process

![[detect-false-positive.png]]

![[terminated-vs-stopped.png]]

#### C. The Failover Process

![[failover-process.png]]

> [!faq] If the primary node is unreachable, then how will replicas catchup?
> 
> MySQL replication works with two threads, _IO_thread_ and _SQL_thread_. The _IO_thread_ connects to the primary and copies the binary log events into a local file called as **relay log.** The SQL thread then reads from the relay log and applies it to the replica.
> 
> If the _IO_thread_ in MySQL has not synchronized with the primary node before the primary node failure, there is a risk of data loss. However, if the _IO_thread_ is synchronized and the replication delay is caused by the _SQL_thread_, we will wait for a period to allow the _SQL_thread_ to read and apply all the log entries from the relay log file.


#### D. Service Discovery

Altair uses DNS for service discovery. On successful fail-over, Altair updates the DNS with the IP of the new primary node, so no restart is required for the applications to connect to the new primary.

> **What is Split Brain?**
> 
> Split brain is a state in the MySQL cluster when two nodes in the cluster start accepting ‘Writes’ i.e., there are two primary nodes.
> 
> **When does it occur?**
> 
> When the fail-over process is unable to cordon off the primary node before promoting one of the replicas as the new primary.
> 
> **How does it occur?**
> 
> When different nodes in a MySQL cluster spread across different fault domains get network partitioned.
> When a network partition causes a split brain, it’s impossible to ensure both consistency and availability as per the [CAP](https://en.wikipedia.org/wiki/CAP_theorem) theorem
> ![[split-brain-mysql.png]]
> 
> **Why to prevent split-brain?**
> 
> As nodes will be unable to communicate, 
> - Different nodes will have in-consistent data (if availability is prioritized, eg: social media)
> - One of the nodes will be down -> 503 service unavailable to users (if consistency is prioritized, eg: banking systems)


#### E. How ALTAIR (Flipkart's HA solution) prevents split-brain / How to fence-off old primary

![[altair-prevent-split-brain.png]]

## 3. The "Gotchas" (The Trade-offs)

blah

## 4. Failure Modes & Recovery (The Most Important Part)

### Different Failure Scenarios

> **Node failure**

The roles of each component in node failure detection are as follows:

1. Node dies.
2. Monitor misses three consecutive agent health updates.
3. The monitor marks the node as unhealthy after 30s.
4. The monitor notifies the orchestrator about the failure.
5. The orchestrator validates the failure and then triggers the recovery process.

> **MySQL failure**

The following steps describe the role of different components involved in MySQL failure detection:

1. MySQL process fails.
2. Agent sends the state of the MySQL in its health update to the monitor.
3. The monitor identifies that the node is up but the MySQL process does not run.
4. The monitor notifies the orchestrator about the failure.
5. Orchestrator validates that the node is up but the MySQL process has failed.
6. The orchestrator triggers the recovery process.

### Failure Scenarios based on different partitions

> Network partition between the primary and the replicas

This failure scenario is not a candidate for triggering the fail-over.

> **Network partition between the control plane and the primary**

Either the orchestrator or the monitor can be partitioned with the primary or both can be partitioned with the primary.

- **Orchestrator and primary**

1. The monitor receives the health updates from the agent running in the primary node.
2. For node or MySQL failures, the monitor will notify the orchestrator about the failure. Since the orchestrator is partitioned with the primary node, the ping checks to the node and MySQL will fail.
3. The orchestrator can promote one of the replicas as the new primary, provided the replica is not network-partitioned with the orchestrator. 

> [!note] Here, human intervention is required to fence off the primary before promoting the replica 

- **Monitor and primary**

1. The monitor doesn’t receive the health updates from the agent and notifies the Orchestrator about the failure.
2. Orchestrator detects that the node is healthy and the MySQL process is running.
3. The orchestrator treats this as a *false positive*.

- **Orchestrator and Monitor are partitioned with the primary**

1. The monitor doesn’t receive the health updates from the agent and considers this as a failure.
2. The monitor notifies the orchestrator about the failure.
3. Now, it’s similar to the case where orchestrator and primary are partitioned.

## 5. Decision Matrix (When to use what?)

nil

## Verification Commands / Monitoring

nil



