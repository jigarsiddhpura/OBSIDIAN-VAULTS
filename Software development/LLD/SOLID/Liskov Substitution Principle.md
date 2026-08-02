
> [!tldr] 
> If Class B is subtype of Class A, then we should be able to replace object of A with B without breaking the behaviour of the program.

***Subclass should extend the capability of parent class NOT narrow it down***

> [!caution]

```
interface Bike {
    void turnOnEngine();
    void accelerate();
}

class MotorCycle implements Bike {
    boolean isEngineOn;
    int speed;

    public void turnOnEngine() {
        //turn on the engine!
        isEngineOn = true;
    }

    public void accelerate() {
        //increase the speed
        speed = speed + 10;
    }
}

class Bicycle implements Bike {

    public void turnOnEngine() {
        throw new AssertionError("there is no engine");
    }

    public void accelerate() {
        //do something
    }
}
```


Here, class `Bicycle` narrows down the functionality of `Bike` by throwing error in `turnOnEngine` 

> [!success]

```mermaid
flowchart TD
    Vehicle["public class Vehicle {
    public Integer getNumberOfWheels(){
        return 2;
    }
}"]

    EngineVehicle["public class EngineVehicle extends Vehicle{
    
    public boolean hasEngine(){
        return true;
    }
}"]

    Bicycle["public class Bicycle extends Vehicle {
}"]

    Car["class Car extends EngineVehicle{
}"]

    MotorCycle["public class MotorCycle extends EngineVehicle{
}"]

    %% Relationships
    Vehicle --> EngineVehicle
    Vehicle --> Bicycle
    EngineVehicle --> Car
    EngineVehicle --> MotorCycle
```
