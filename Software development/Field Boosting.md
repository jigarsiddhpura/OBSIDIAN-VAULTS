
## 1. The ELI5 (Explain Like I'm 5)

> **Multiplicative weight applied to the relevance score** based on where the match occurs (eg: title, description, body, etc)

## 2. The Problem It Solves

**No ranking of “important” results first** (e.g., MacBook Pro ahead of laptop bags) while **searching** (eg: laptop). [[Relevance Scoring (BM25 Algorithm)]]

## 3. How It Works (The Mechanics)

```
title        → boost = 3.0
description  → boost = 1.5
content      → boost = 1.0
```

Above scores are used for relevance scoring

```
Relevance Score = (TF × IDF × FieldBoost) ÷ LengthNorm
```

