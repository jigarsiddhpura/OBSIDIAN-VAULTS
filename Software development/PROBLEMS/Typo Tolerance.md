
## 1. The ELI5 (Explain Like I'm 5)

> Mechanism by which a search engine resolves *misspelled queries* by matching them to the closest valid indexed terms using similarity metrics, without requiring exact text matches.

## 2. The Problem It Solves

[[Inverted Index]]

## 3. How It Works (The Mechanics)

### *Technique 1: Edit distance* ([[Levenshtein distance]])

The engine looks up to the (indexed) word with smallest edit distance and treat it as a match.
Then it looks up:
```
"laptop" → [doc1, doc5, doc9]
```

### *Technique 2: Fuzzy Search*

Allow words within edit distance ≤ **fuzziness**

```
{
  "fuzzy": {
    "review": {
      "value": "laptpo",
      "fuzziness": 1
    }
  }
}
```

## *Technique 3: N-grams*

Words are split into character chunks.

```
laptop → lap, apt, pto, top
laptpo → lap, apt, ptp, tpo
```

*Overlap exists → match confidence increases.*

## 4. When to use / Real World Examples

- E-commerce
- Autocomplete / typeahead
- Search engine
  

