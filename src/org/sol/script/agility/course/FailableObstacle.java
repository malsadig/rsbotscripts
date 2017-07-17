package org.sol.script.agility.course;

import org.sol.core.script.Methods;

public class FailableObstacle extends Obstacle {

    public FailableObstacle(Obstacle other) {
        super(other);
    }

    public boolean isInFailArea() {
        return false;
    }

    public int leaveFailArea() {
        return -1;
    }
}