
### 1. Encapsulation

**Bundling data with the methods that operate on it, and hiding internal state from outside interference.**

python

```python
class Portfolio:
    def __init__(self, cash: float):
        self._cash = cash          # "private" - not to be touched directly
        self._positions = {}

    def buy(self, symbol: str, qty: int, price: float):
        cost = qty * price
        if cost > self._cash:
            raise ValueError("Insufficient funds")
        self._cash -= cost
        self._positions[symbol] = self._positions.get(symbol, 0) + qty

    def get_cash(self) -> float:
        return self._cash
```

**Why it matters practically:** without encapsulation, some other part of your codebase could do `portfolio._cash -= 100000` directly and silently corrupt state with no validation. You'd spend hours later trying to figure out why cash went negative. Encapsulation forces all mutations through `buy()`, so your invariants (cash never negative, positions always consistent) are enforced in one place. This is the principle people violate most in real codebases — public mutable fields everywhere, and then "helper" scripts reach in and mutate them.

### 2. Abstraction

**Exposing only what's necessary, hiding implementation complexity behind a simple interface.**

python

```python
class MarketDataFeed(ABC):
    @abstractmethod
    def get_price(self, symbol: str) -> float:
        pass

class BloombergFeed(MarketDataFeed):
    def get_price(self, symbol: str) -> float:
        # actual socket connection, auth handshake, protocol parsing...
        return self._parse_bloomberg_response(symbol)

class IEXFeed(MarketDataFeed):
    def get_price(self, symbol: str) -> float:
        # REST call, JSON parsing, rate limit handling...
        return self._call_iex_api(symbol)
```

Your strategy code just calls `feed.get_price("AAPL")`. It doesn't know or care whether that's a raw TCP socket to Bloomberg or a REST call to IEX.

**Why it matters practically:** this is what lets you swap data providers, or run backtests against a `SimulatedFeed` that replays historical data, without touching a single line of your strategy logic. If you skip abstraction and hardcode Bloomberg calls throughout your strategy code, switching providers later means rewriting everything.

### 3. Inheritance

**A subclass reuses and extends behavior from a parent class.**

python

```python
class Order:
    def __init__(self, symbol: str, qty: int):
        self.symbol = symbol
        self.qty = qty

    def validate(self):
        if self.qty <= 0:
            raise ValueError("Quantity must be positive")

class LimitOrder(Order):
    def __init__(self, symbol: str, qty: int, limit_price: float):
        super().__init__(symbol, qty)
        self.limit_price = limit_price

    def validate(self):
        super().validate()
        if self.limit_price <= 0:
            raise ValueError("Limit price must be positive")

class StopOrder(Order):
    def __init__(self, symbol: str, qty: int, stop_price: float):
        super().__init__(symbol, qty)
        self.stop_price = stop_price
```

**Practical criticism, not just praise:** inheritance is the most overused and misused of the four principles. Deep inheritance hierarchies (`Order → TradableOrder → ExchangeOrder → NASDAQLimitOrder`) get brittle fast — you change the base class and break five subclasses you forgot existed. The rule of thumb real engineers use: prefer composition over inheritance unless there's a genuine "is-a" relationship AND you actually need polymorphic dispatch (see below). Here, `LimitOrder` and `StopOrder` really are `Order`s, and it's shallow (one level deep) — that's the sweet spot.

> [!faq] The problem with inheritance

Say you extend the`Order` hierarchy to handle _execution venues_ and _notification methods_:

python

```python
class Order:
    ...

class EmailNotifyingOrder(Order):
    def notify(self):
        send_email(...)

class SlackNotifyingOrder(Order):
    def notify(self):
        send_slack(...)

class NASDAQOrder(Order):
    def route(self):
        connect_to_nasdaq(...)

class NYSEOrder(Order):
    def route(self):
        connect_to_nyse(...)
```

Now you need an order that's **NASDAQ-routed AND Slack-notified**. With single inheritance, you're stuck. You'd have to create `NASDAQSlackOrder`, then `NASDAQEmailOrder`, then `NYSESlackOrder`... This is the classic "class explosion" problem — N notification types × M venue types = N×M classes, and it gets worse every time you add a new dimension (say, a new risk-check policy). This is exactly the trap people fall into with deep inheritance trees.

#### ✅Composition fixes it

Instead of baking "how do I notify" and "how do I route" into the class hierarchy, you inject them as interchangeable _behaviors_ — separate objects the `Order` holds a reference to, rather than inherits from:

python

```python
from abc import ABC, abstractmethod

# --- Independent, swappable behaviors ---

class Notifier(ABC):
    @abstractmethod
    def notify(self, message: str): ...

class EmailNotifier(Notifier):
    def notify(self, message: str):
        send_email(message)

class SlackNotifier(Notifier):
    def notify(self, message: str):
        send_slack(message)

class Router(ABC):
    @abstractmethod
    def route(self, order: "Order"): ...

class NASDAQRouter(Router):
    def route(self, order: "Order"):
        connect_to_nasdaq(order)

class NYSERouter(Router):
    def route(self, order: "Order"):
        connect_to_nyse(order)

# --- Order composes these behaviors instead of inheriting them ---

class Order:
    def __init__(self, symbol: str, qty: int, router: Router, notifier: Notifier):
        self.symbol = symbol
        self.qty = qty
        self.router = router
        self.notifier = notifier

    def execute(self):
        self.router.route(self)
        self.notifier.notify(f"Order executed: {self.symbol} x{self.qty}")
```

Now any combination is just a constructor call — no new classes needed:

python

```python
order1 = Order("AAPL", 100, router=NASDAQRouter(), notifier=SlackNotifier())
order2 = Order("BRK.A", 5, router=NYSERouter(), notifier=EmailNotifier())
```

Add a new venue (`LSERouter`) or a new notification channel (`SMSNotifier`) tomorrow, and you write **one** new class — you never touch `Order`, and you never get combinatorial blowup.

> Each of these varies _independently_ of the others — that's the signal that composition is the right call. The rule of thumb: use inheritance when you have a genuine taxonomy that varies along **one axis** (LimitOrder vs StopOrder — different _kinds_ of the same thing). Use composition when behavior varies along **multiple independent axes** that need to be mixed and matched — that's where inheritance either forces class explosion or forces you to pick one axis as the "base class" arbitrarily and hack the rest in with mixins or flags, both of which get ugly to maintain fast.

### 4. Polymorphism

**Different classes respond to the same method call in their own way — the caller doesn't need to know which concrete type it's dealing with.**

python

```python
def execute_orders(orders: list[Order], market_price: float):
    for order in orders:
        order.validate()
        if isinstance(order, LimitOrder) and market_price <= order.limit_price:
            print(f"Executing limit order for {order.symbol}")
        elif isinstance(order, StopOrder) and market_price >= order.stop_price:
            print(f"Executing stop order for {order.symbol}")
```

A cleaner polymorphic version avoids `isinstance` checks entirely by pushing the logic into each class:

python

```python
class LimitOrder(Order):
    def should_execute(self, market_price: float) -> bool:
        return market_price <= self.limit_price

class StopOrder(Order):
    def should_execute(self, market_price: float) -> bool:
        return market_price >= self.stop_price

def execute_orders(orders: list[Order], market_price: float):
    for order in orders:
        if order.should_execute(market_price):
            print(f"Executing {order.symbol}")
```

**Why it matters practically:** this is the real payoff of the whole OOP exercise. Add a new order type (`TrailingStopOrder`) tomorrow, and `execute_orders` doesn't change at all — no new `elif` branch, no risk of breaking existing order types. If you ever see a function full of `isinstance` or `type ==` checks branching on subclass, that's a sign polymorphism isn't being used properly — push the behavior down into the subclasses instead.