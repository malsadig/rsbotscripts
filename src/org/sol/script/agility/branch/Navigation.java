package org.sol.script.agility.branch;

import org.powerbot.script.Random;
import org.powerbot.script.rt6.Game;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.TileMatrix;
import org.sol.core.node.Node;
import org.sol.core.script.Methods;
import org.sol.script.agility.course.Course;
import org.sol.script.agility.course.Obstacle;
import org.sol.util.Condition;

import java.awt.*;

public class Navigation extends Node {
    private final Course course;

    public Navigation(Methods methods, Course course) {
        super(methods);
        this.course = course;
    }

    @Override
    public boolean activate() {
        return course.getCurrentObstacle().isInArea(methods) || course.getNextObstacle().isInArea(methods);
    }

    @Override
    public void run() {
        final Obstacle obstacle = course.getCurrentObstacle();
        final Obstacle nextObstacle = course.getNextObstacle();

        if(obstacle.isInArea(methods)) {
            methods.status = "Current obstacle: " + obstacle.getOrdinal() + "/" + (course.getObstacles().size() - 1);
            GameObject object = obstacle.getObject(methods);

            if (object != null) {
                methods.status = "object is not null";
                if (methods.context.players.local().tile().distanceTo(object.tile()) > 7) {
                    methods.status = "Walking to obstacle: " + obstacle.getOrdinal()
                            + "/" + (course.getObstacles().size() - 1);
                    TileMatrix matrix = object.tile().matrix(methods.context);
                    if (matrix != null && matrix.valid()) {
                        matrix.click("Walk here", Game.Crosshair.DEFAULT);
                    }
                    methods.context.movement.step(object);
                } else if (object.inViewport()) {
                    methods.status = "Interacting with obstacle: " + obstacle.getOrdinal()
                            + "/" + (course.getObstacles().size() - 1);

                    if (object.interact(obstacle.getInteraction(), object.name())) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return !nextObstacle.isInArea(methods);
                            }
                        }, 2500);
                    }
                }
            }
        }
        methods.status = "Have we passed obstacle " +
                obstacle.getOrdinal() + "/" + (course.getObstacles().size() - 1) + "?";

        if (nextObstacle.isInArea(methods)) {
            methods.status = "In new obstacle area";
            //todo handle if failed
            if(nextObstacle.getOrdinal() == 0) {
                for (final Obstacle ob:course.getObstacles()){
                    ob.state = Obstacle.State.NOT_STARTED;
                }
            } else {
                obstacle.state = Obstacle.State.COMPLETED;
            }
            course.setToNextObstacle();
        }

        //todo handle banking
    }
}
