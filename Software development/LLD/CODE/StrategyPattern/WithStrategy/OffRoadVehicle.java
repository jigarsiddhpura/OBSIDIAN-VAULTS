package StrategyPattern.WithStrategy;

import StrategyPattern.WithStrategy.Strategy.SportsDriveStrategy;

public class OffRoadVehicle extends Vehicle {
    public OffRoadVehicle() {
        super(new SportsDriveStrategy());
    }
}
