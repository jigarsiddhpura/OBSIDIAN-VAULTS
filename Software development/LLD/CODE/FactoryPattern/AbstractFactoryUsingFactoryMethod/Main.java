package FactoryPattern.AbstractFactoryUsingFactoryMethod;

enum CarType {
    LUXURY,
    ECONOMY
}

interface CarInterior {
    void assemble();
}

interface CarExterior {
    void metal();
}

class LuxuryCarInterior implements CarInterior {
    public void assemble() {
        System.out.println("Assembling Luxury Car Interior");
    }
}

class EconomyCarInterior implements CarInterior {
    public void assemble() {
        System.out.println("Assembling Economy Car Interior");
    }
}

class LuxuryCarExterior implements CarExterior {
    public void metal() {
        System.out.println("Assembling Luxury Car Exterior");
    }
}

class EconomyCarExterior implements CarExterior {
    public void metal() {
        System.out.println("Assembling Economy Car Exterior");
    }
}

abstract class CarFactory {
    abstract CarInterior createCarInterior();
    abstract CarExterior createCarExterior();
    abstract void produceCompleteVehicle();
}

class LuxuryCarFactory extends CarFactory {
    CarInterior createCarInterior() {
        return new LuxuryCarInterior();
    }
    CarExterior createCarExterior() {
        return new LuxuryCarExterior();
    }
    void produceCompleteVehicle() {
        CarInterior interior = createCarInterior();
        interior.assemble();
        CarExterior exterior = createCarExterior();
        exterior.metal();
    }
}

class EconomyCarFactory extends CarFactory {
    CarInterior createCarInterior() {
        return new EconomyCarInterior();
    }
    CarExterior createCarExterior() {
        return new EconomyCarExterior();
    }
    void produceCompleteVehicle() {
        CarInterior interior = createCarInterior();
        interior.assemble();
        CarExterior exterior = createCarExterior();
        exterior.metal();
    }
}


public class Main {
    public static void main(String[] args) {
        // luxury car interior can only be assembled with luxury car exterior
        CarFactory luxuryCarFactory = new LuxuryCarFactory();
        luxuryCarFactory.produceCompleteVehicle();
        
        
    }
}
