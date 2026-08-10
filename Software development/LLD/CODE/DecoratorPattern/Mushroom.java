public class Mushroom extends ToppingsDecorator {
    public Mushroom(BasePizza basePizzaObj) {
        super(basePizzaObj);
    }

    @Override
    public int cost() {
        // basePizza comes from ToppingsDecorator class
        return basePizza.cost() + 30;
    }

}
