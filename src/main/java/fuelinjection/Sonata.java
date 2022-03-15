package fuelinjection;

public class Sonata extends Car {

    private static final double DISTANCE_PER_LITTER = 10;
    private static final String NAME = "Sonata";

    public Sonata(double tripDistance) {
        super(tripDistance);
    }

    @Override
    double getDistancePerLiter() {
        return DISTANCE_PER_LITTER;
    }

    @Override
    String getName() {
        return NAME;
    }
}
