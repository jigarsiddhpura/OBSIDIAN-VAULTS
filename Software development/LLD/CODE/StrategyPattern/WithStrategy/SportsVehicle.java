package StrategyPattern.WithStrategy;

import StrategyPattern.WithStrategy.Strategy.SportsDriveStrategy;

public class SportsVehicle extends Vehicle {
    public SportsVehicle() {
        super(new SportsDriveStrategy());
    }
}
