
> [!tldr] Interfaces should be such, that client should NOT implement unnecessary functions they do not need

> [!caution]

```
interface RestaurantEmployee {
    void washDishes();
    void serveCustomers();
    void cookFood();
}

class waiter implements RestaurantEmployee {

    public void washDishes(){
        //not my job
    }

    public void serveCustomers() {
        //yes and here is my implemenation
        System.out.println("serving the customer");
    }

    public void cookFood(){
        // not my job
    }
}
```

Here, waiter has to unnecessarily implement functions - washDishes, cookFood, that it doesn't need. 

> [!success]

```
interface WaiterInterface {
    void serveCustomers();
    void takeOrder();
}

interface ChefInterface {
    void cookFood();
    void decideMenu();
}

class waiter implements WaiterInterface {

    public void serveCustomers() {
        System.out.println("serving the customer");
    }

    public void takeOrder(){
        System.out.println("taking orders");
    }
}
```

Now we only need to implement necessary functions.