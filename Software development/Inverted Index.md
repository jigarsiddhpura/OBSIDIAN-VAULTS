Tags: #Index 

## 1. The ELI5 (Explain Like I'm 5)

Instead of scanning documents to find terms, you map:
`Term → List of documents where it appears.`

So searching for “machine learning” becomes:

- Look up “machine” → documents containing it
- Look up “learning” → same

This _reverses the search_:

- **Instead of document → terms**, it becomes **term → documents**.
- This _inversion_ dramatically speeds up lookup time. [elastic.co](https://www.elastic.co/docs/solutions/search/full-text/how-full-text-works?utm_source=chatgpt.com)

## 2. The problem it solves

[[Full-text search optimization (ElasticSearch vs RDBMS)]]

## 3. How It Works (The Mechanics)

*Before indexing*:

- Text is _tokenized_ (broken into words/terms).
- Normalized (lowercase, remove stop words, stemming).
- Stored in inverted index structures. [elastic.co](https://www.elastic.co/docs/solutions/search/full-text/how-full-text-works?utm_source=chatgpt.com)

*This creates:*

- **Dictionary**: all unique terms
- **Posting list**: documents & term metadata for each term

⚙️ Efficient search = inverted index + metadata.

## 4. The Trade-offs (The Critical Thinking)

## 5. When to use / Real World Examples

## 6. **Benefits**

- No scanning every record — direct index lookup.
- Supports **fast ranking** based on term statistics (frequency, context).
- Enables search features like [[Relevance Scoring (BM25 Algorithm)]], [[Typo Tolerance]] and [[Field Boosting]].

