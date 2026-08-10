package FactoryPattern.AbstractFactoryUsingSimpleFactory;

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

class CarInteriorFactory {
    CarInterior createCarInterior(CarType carType) {
        if (carType == CarType.LUXURY) {
            return new LuxuryCarInterior();
        } else if (carType == CarType.ECONOMY) {
            return new EconomyCarInterior();
        }
        return null;
    }
}

class CarExteriorFactory {
    CarExterior createCarExterior(CarType carType) {
        if (carType == CarType.LUXURY) {
            return new LuxuryCarExterior();
        } else if (carType == CarType.ECONOMY) {
            return new EconomyCarExterior();
        }
        return null;
    }
}

class CarFactoryProducer{
    public static Object getFactory(String choice){
        if(choice.equalsIgnoreCase("INTERIOR")){
            return new CarInteriorFactory();
        } else if(choice.equalsIgnoreCase("EXTERIOR")){
            return new CarExteriorFactory();
        }
        return null;
    }
}

public class Main {
    public static void main(String[] args) {
        // assembling luxury car interior with economy car exterior
        
        CarInteriorFactory interiorFactory = (CarInteriorFactory) CarFactoryProducer.getFactory("INTERIOR");
        CarInterior luxuryInterior = interiorFactory.createCarInterior(CarType.LUXURY);
        luxuryInterior.assemble();

        CarExteriorFactory exteriorFactory = (CarExteriorFactory) CarFactoryProducer.getFactory("EXTERIOR");
        CarExterior economyExterior = exteriorFactory.createCarExterior(CarType.ECONOMY);
        economyExterior.metal();
    }
}