
# [Concept Name, e.g., Optimistic Locking]

**Tags:** #SystemDesign #Database #Concurrency #ProblemPattern

## 1. The ELI5 (Explain Like I'm 5)
* *One sentence summary of what this actually is.*

## 2. The Problem It Solves
* *What breaks if we don't use this?* (e.g., "Prevents two users from booking the same seat without locking the whole table.")

## 3. How It Works (The Mechanics)
* *Step-by-step pseudo-code or mechanism.*

## 4. The Trade-offs (The Critical Thinking)
| Pros (Why use it?) | Cons (Why avoid it?) |
| :--- | :--- |
| High concurrency (users don't wait) | High failure rate if traffic is huge |
| No DB locks held | Wasted resources on retry |

## 5. When to use / Real World Examples
* *Where have I seen this?* (e.g., Ticketmaster Seat Selection, Wiki Edits)

## 6. Code Snippet / Implementations
```javascipt
//code
```

