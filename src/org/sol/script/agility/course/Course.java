package org.sol.script.agility.course;

import org.sol.core.script.Methods;

import java.util.LinkedList;
import java.util.List;

public class Course {
    private final Methods methods;
    private final Courses internal;
    private final LinkedList<Obstacle> obstacles;
    private final boolean canBank;
    private int obstacleNumber;

    private boolean bankTime;

    private boolean failable = false;

    public Course(final Courses internal, final Methods methods) {
        this.methods = methods;
        this.internal = internal;
        this.obstacles = new LinkedList<Obstacle>();
        this.canBank = internal.getBankArea() != null;

        for (final Obstacles obstacle : Obstacles.values()) {
            Obstacle ob = obstacle.getObstacle();
            if (ob.getCourse() == internal) {
                obstacles.add(ob);
                if (ob.isInArea(methods)) {
                    ob.state = Obstacle.State.IN_PROGRESS;
                    this.obstacleNumber = ob.getOrdinal();
                }
            }
        }
    }

    public Obstacle getPreviousObstacle() {
        return obstacleNumber > 0 ? obstacles.get(obstacleNumber - 1) : obstacles.getLast();
    }

    public Obstacle getCurrentObstacle() {
        return obstacles.get(obstacleNumber);
    }

    public Obstacle getNextObstacle() {
        return obstacleNumber == obstacles.size() - 1 ? obstacles.getFirst() : obstacles.get(obstacleNumber + 1);
    }

    public void setToPrevObstacle() {
        this.obstacleNumber = obstacleNumber > 0 ? obstacleNumber - 1 : obstacles.size() - 1;
    }

    public void setToNextObstacle() {
        this.obstacleNumber = (obstacleNumber + 1) % obstacles.size();
    }

    public int getObstacleNumber() {
        return obstacleNumber;
    }

    public void setObstacleNumber(final int obstacleNumber) {
        this.obstacleNumber = obstacleNumber;
    }

    public boolean isBankTime() {
        return bankTime;
    }

    public void setBankTime(final boolean bankTime) {
        this.bankTime = bankTime;
    }

    public Courses getInternal() {
        return this.internal;
    }

    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }
}