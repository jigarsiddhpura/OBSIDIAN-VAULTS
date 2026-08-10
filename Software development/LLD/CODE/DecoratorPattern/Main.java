public class Main {
    public static void main(String[] args) {
        BasePizza pizza = new MargheritaPizza();
        System.out.println("Cost of Margherita Pizza: " + pizza.cost());

        // this way you can add toppings to the base pizza without modifying the existing code
        BasePizza pizzaWithCheeseAndMushroom = new Mushroom(new ExtraCheese(new MargheritaPizza()));
        System.out.println("Cost of Margherita Pizza with Extra Cheese and Mushroom: " + pizzaWithCheeseAndMushroom.cost());
    }
}
