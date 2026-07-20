
**Context:** #Lucene 
**Criticality:** High

## 1. The "Why" (The Failure Scenario)
*What happens if we don't have this*

> Without a dedicated search engine, your product discovery relies on RDBMS `LIKE` operators or `tsvector` (Postgres). At scale, this results in **Sequential Scans** that spike CPU utilization to 100%, causing query timeouts and a complete UX breakdown. Furthermore, the lack of **Relevance Scoring** (i.e. users get "MacBook Pro Sleeves" instead of "MacBook Pro" when searching for "MacBook,"); **No typo tolerance**; **No ranking of “important” results first** (e.g., MacBook Pro ahead of laptop bags) leads to high bounce rates and lost revenue.
## 2. High-Level Architecture (The Topology)

[[Inverted Index]]

## Why Traditional Databases Fall Short

### *Librarian Analogy

- A relational database scanner behaves like a librarian who must inspect every book page to find a match – _stationary, slow, and blind to relevance_.

- Even if a book mentions the keyword once, it gets equal weight as a book where the keyword is central — _no built-in relevance ranking_.

### Core Flaws*

- Full table scan for pattern matches (`%term%`) → **O(n)** complexity.
- No concept of _term frequency, title weighting, or semantic relevance_.
## 3. The "Gotchas" (The Trade-offs)

### *When To Choose ES*

✔ High performance search  
✔ Relevance ranking  
✔ Fuzzy matching & typo tolerance  
✔ Scalable distributed architecture

### *When to Stick to Native DB Search*

✔ Small datasets  
✔ Simpler apps with limited search needs  
✔ Avoid schema duplication and operational overhead

## 4. When to use / Real World Examples

- **Log management & analytics** (ELK/Kibana stacks)  - Elastic is widely used for search over high volumes of logs.
- **Metrics and tabular search**
- **Multi-language support**
- Flexible JSON-based search DSL

Note: Elastic isn’t just search — it’s a _document store + analytics engine_.
