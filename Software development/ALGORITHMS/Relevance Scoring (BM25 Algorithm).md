
## The ELI5 (Explain Like I'm 5)

> It is the process of computing a numerical score for each document by evaluating how well the queried terms match it, based on factors such as term frequency, rarity across the corpus, field importance, and document length, in order to rank results from most to least relevant.

## 2. The Problem It Solves

Arbitary return order of the document

## 3. How It Works (The Mechanics)

Elasticsearch doesn’t just return matching docs — it **ranks them by relevance** using _Okapi BM25_.

BM25 considers:

1. **Term frequency** — how often the word appears within a doc
2. **Document frequency** — how common the word is across all docs
3. **Document length** — longer docs dilute term importance
4. [[Field Boosting]] — matches in title > description > body [elastic.co+1](https://www.elastic.co/docs/solutions/search/full-text/how-full-text-works?utm_source=chatgpt.com)

```
IDF = log(total_docs / docs_with_term)
Final Score = (TF × IDF × FieldBoost) ÷ LengthNorm
```

This scoring ensures:

- Documents with terms in **title** score higher than those where terms appear deeper in text.
- Docs with higher term frequency have improved rank.

**This explains why searching “machine learning” returns**:

1. Intro to Machine Learning (title + many matches)
2. Machine Age (title but fewer matches)
3. Irrelevant docs lower down

This ranking is automatic with ES unless you customize it.
