public class ExtraCheese extends ToppingsDecorator {
    public ExtraCheese(BasePizza basePizzaObj) {
        super(basePizzaObj);
    }

    @Override
    public int cost() {
        // basePizza comes from ToppingsDecorator class
        return basePizza.cost() + 20;
    }
}