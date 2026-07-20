

## 1. Where LLD Sits in the Design Pipeline

![[Pasted image 20260719235801.png]]

|Layer|What it defines|Example|
|---|---|---|
|**High-Level Design (HLD)**|Overall system architecture — components and how they talk to each other|Component 1 ↔ Component 2 ↔ Component 3|
|**Low-Level Design (LLD)**|"Double-clicking" into a single component — its classes, objects, and their relationships|Inside Component 1: `Order`, `Payment`, `Inventory` classes|
|**Actual Code**|Implementation|Java/Python/etc. source|

> **Core idea:** LLD is the bridge between architecture and code. It's the step where you decide the classes, their responsibilities, and how they interact — _before_ you write a single line of implementation.

### Why LLD matters

- The goal of LLD is **clean code**. Three properties called out as most important:
    1. **Flexible** — easy to extend
    2. **Maintainable** — easy to change without breaking things
    3. **Testable** — easy to write tests against
- Writing code itself isn't the hard part — even AI can write code now. The **design** (HLD + LLD) is what's genuinely difficult and valuable.


---

## 2. The Three Categories of Design Patterns

LLD patterns fall into three buckets. Knowing the _pattern names_ is optional — recognizing the _problem shape_ is what actually matters.

### 2.1 Creational — _controls object creation_

Decides **how/when objects get created**.

- Singleton, Builder, Factory, Abstract Factory, Object Pool, Prototype
- **Singleton** — no matter how many callers ask for an object, only one instance ever exists and is shared.
- **Builder** — object is constructed step-by-step instead of all at once.

```java
// Singleton — quick sketch
public class ConfigManager {
    private static final ConfigManager INSTANCE = new ConfigManager();
    private ConfigManager() {}
    public static ConfigManager getInstance() { return INSTANCE; }
}
```

> 🔎 **Practical/critic note:** Classic eager Singleton like above is thread-safe by class-loading guarantees, but it's also a well-known interview red flag if used carelessly (global mutable state, hard to unit test/mock). In real Java systems, prefer DI-managed singletons (Spring `@Bean` scoped as singleton) over hand-rolled ones — mention this trade-off if it comes up in an interview, it signals seniority.

### 2.2 Structural — _the skeleton_

Decides **how classes/objects are arranged** so a bigger problem is solved in a flexible way.

- Decorator, Proxy, Composite, Adapter, Bridge, Facade, Flyweight
- Analogy: building a car — wheel, engine, headlights, steering are separate objects; structural patterns decide how they're wired together into "a car."

### 2.3 Behavioral — _how the skeleton behaves_

Decides **how objects communicate/interact** and how responsibility is distributed once the skeleton (structure) exists.

- State, Strategy, Observer, Chain of Responsibility, Template, Iterator, Interpreter, Command, Visitor, Mediator, Memento, Null Object
- Analogy: Class2 needs to talk to Class3 — does it call directly, or go through an orchestrator class? That coordination style is what behavioral patterns govern.


---

## 3. "Has-a" vs "Is-a" Relationships

This is called out as the most common source of confusion.

### 3.1 Is-a → Inheritance

Child class **is a type of** parent class.

```java
class Employee {}
class CEO extends Employee {}         // CEO IS-A Employee
class Manager extends Employee {}     // Manager IS-A Employee
```

### 3.2 Has-a → Association


![[Pasted image 20260719235448.png]]
A **link** between two ***independent*** objects (no parent/child relationship). Example: house _has_ rooms, library _has_ books, school _has_ students.

Has-a splits into two sub-types based on **lifecycle dependency**:

|Type|Relationship strength|Lifecycle rule|UML notation|Example|
|---|---|---|---|---|
|**Aggregation**|Weak|Objects can exist independently of each other|Hollow diamond + line|Library _has_ Books — if the library is destroyed, books still exist|
|**Composition**|Strong|Child object's existence depends on the parent|Filled diamond + line|House _has_ Rooms — if the house is destroyed, rooms cease to exist|

#### Aggregation example (weak — library doesn't manage book lifecycle)

```java
class Library {
    private List<Book> books; // knows about books, doesn't own their lifecycle
    // Library does NOT create/delete Book objects — they can exist independently
}
```

#### Composition example (strong — house owns room lifecycle)

```java
class House {
    private List<Room> rooms = new ArrayList<>();

    // House IS responsible for creating rooms — rooms can't exist without a House
    public Room addRoom(String type) {
        Room room = new Room(type);
        rooms.add(room);
        return room;
    }
}
```

> 🔎 **Practical/critic note:** The distinguishing test given in the video is genuinely useful and interview-safe: _"Can object B exist independently if object A is destroyed?"_ Yes → aggregation. No → composition. In Java terms, composition usually means the child object is instantiated **inside** the parent's constructor/methods and has no meaningful existence outside it (e.g., `Engine` created inside `Car`'s constructor); aggregation usually means the child is **passed in** or **fetched externally** (e.g., a `Book` list passed into `Library`). *This maps directly to constructor injection patterns — worth mentioning if you want to sound senior in an interview*.

---

## 4. Interview Time Management (Practical Advice from the Video)

- **Machine coding rounds:** ~1–2 hours (fully working code expected).
- **Standard LLD rounds:** ~40–45 minutes. Code is usually still expected, at least partially — don't assume a UML diagram alone is enough.
- **Recommended time split:** Spend only ~10–15 minutes on UML/class design; save the rest for actual coding, because **not delivering code significantly hurts your chances**, per the speaker's experience.
- **Shortcut UML notation:** Instead of memorizing hollow vs. filled diamonds, just draw an arrow between classes and label it `is-a` or `has-a` directly. Faster, and equally clear to an interviewer.

> 🔎 **Practical/critic note:** This time-boxing advice is the single most actionable point in the video for anyone actively interviewing. A common failure mode is over-polishing the UML/whiteboard diagram and running out of time to write compilable code — interviewers weight working code heavily. Treat the diamond/arrow notation debate as irrelevant; it's not what gets you hired.

---

## 5. Key Takeaways

- [ ] LLD = classes + objects + their relationships for **one component**, sitting between HLD and code.
- [ ] Goal of LLD: clean code that is **flexible, maintainable, testable**.
- [ ] Three pattern categories: **Creational** (object creation), **Structural** (skeleton/arrangement), **Behavioral** (interaction/communication).
- [ ] Knowing pattern _names_ is optional; recognizing the _problem_ a pattern solves is what matters.
- [ ] **Is-a** = inheritance (parent/child). **Has-a** = association, further split into **aggregation** (weak, independent lifecycles) and **composition** (strong, dependent lifecycles).
- [ ] Test for aggregation vs. composition: _does the child object survive if the parent is destroyed?_
- [ ] In interviews (~40–45 min), cap UML/design discussion at 10–15 minutes and prioritize writing actual code.

