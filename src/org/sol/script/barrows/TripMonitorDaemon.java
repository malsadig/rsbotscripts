package org.sol.script.barrows;

import org.sol.core.concurrent.LoopTask;
import org.sol.core.script.Methods;
import org.sol.script.barrows.node.Trip;
import org.sol.util.Condition;

public class TripMonitorDaemon extends LoopTask {
    private Trip trip;
    private int tripCount;

    private final Condition cryptingCondition;

    public TripMonitorDaemon(final Methods methods) {
        super(methods);

        cryptingCondition = new Condition() {
            @Override
            public boolean accept() {
                return methods.context.players.local().tile().floor() == 3;// or is in the barrowsArea
            }
        };
    }

    @Override
    public int loop() {
        if (trip == null || trip.isComplete()) {
            trip = new Trip();
            tripCount++;
        }
        if (cryptingCondition.accept()) {

        }
        return 0;
    }

    public Trip getTrip() {
        return trip;
    }

    public int getTripCount() {
        return tripCount;
    }
}