public abstract class ToppingsDecorator extends BasePizza {
    protected BasePizza basePizza;

    public ToppingsDecorator(BasePizza basePizzaObj) {
        this.basePizza = basePizzaObj;
    }

    public int cost() {
        return basePizza.cost();
    }
}
