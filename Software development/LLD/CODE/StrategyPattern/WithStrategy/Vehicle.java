package StrategyPattern.WithStrategy;

import StrategyPattern.WithStrategy.Strategy.DriveStrategy;

public class Vehicle {
    public DriveStrategy driveStrategy;

    // constructor injection
    public Vehicle (DriveStrategy driveObj) {
        this.driveStrategy = driveObj;
    }

    public void drive() {
        driveStrategy.drive();
    }
}
