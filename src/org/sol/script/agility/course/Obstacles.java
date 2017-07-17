package org.sol.script.agility.course;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Player;
import org.sol.core.script.Methods;
import org.sol.script.agility.Invariants;
import org.sol.util.ContextCondition;

public enum Obstacles {
    BURTHORPE_LOG_BEAM_WALK(new Obstacle.Builder().setOrdinal(0).setCourse(Courses.BURTHORPE)
            .setIds(Invariants.BURTHORPE_LOG_BEAM_ID).setInteraction("Walk")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    return player.tile().floor() == 0 && player.idle()
                            && !BURTHORPE_WALL_CLIMB.getObstacle().isInArea(methods);
                }
            }).build()),
    BURTHORPE_WALL_CLIMB(new Obstacle.Builder().setOrdinal(1).setCourse(Courses.BURTHORPE)
            .setIds(Invariants.BURTHORPE_WALL_CLIMB_ID).setInteraction("Climb-up")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 0 && x >= 2918 && x <= 2921 && y >= 3554 && y <= 3565 && player.idle();
                }
            }).build()),
    BURTHORPE_LEDGE_BALANCE(new Obstacle.Builder().setOrdinal(2).setCourse(Courses.BURTHORPE)
            .setObjectName(Invariants.BURTHORPE_BALANCING_LEDGE_NAME).setInteraction("Walk-across")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 1 && !BURTHORPE_MONKEY_BARS.getObstacle().isInArea(methods) && x >= 2916 && x <= 2920
                            && y >= 3562 && y <= 3564 && player.idle();
                }
            }).build()),
    BURTHORPE_LOW_WALL(new Obstacle.Builder().setOrdinal(3).setCourse(Courses.BURTHORPE)
            .setIds(Invariants.BURTHORPE_LOW_WALL_ID).setInteraction("Climb-over")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 1 && !BURTHORPE_ROPE_SWING.getObstacle().isInArea(methods) && x >= 2910 && x <= 2912
                            && y >= 3561 && y <= 3564 && player.idle();
                }
            }).build()),
    BURTHORPE_ROPE_SWING(new Obstacle.Builder().setOrdinal(4).setCourse(Courses.BURTHORPE)
            .setIds(Invariants.BURTHORPE_ROPE_ID).setInteraction("Swing-on")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 1 && x == 2912 && y >= 3561 && y <= 3563 && player.idle();
                }
            }).build()),
    BURTHORPE_MONKEY_BARS(new Obstacle.Builder().setOrdinal(5).setCourse(Courses.BURTHORPE)
            .setIds(Invariants.BURTHORPE_MONKEY_BARS_ID).setInteraction("Swing-across")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 1 && x >= 2916 && x <= 2917 && y >= 3561 && y <= 3563 && player.idle();
                }
            }).build()),
    BURTHORPE_CLIMB_DOWN(new Obstacle.Builder().setOrdinal(6).setCourse(Courses.BURTHORPE)
            .setIds(Invariants.BURTHORPE_JUMP_DOWN_ID).setInteraction("Jump-down")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 1 && x >= 2915 && x <= 2917 && y >= 3553 && y <= 3554 && player.idle();
                }
            }).build()),

    GNOME_BASIC_LOG_BALANCE(new Obstacle.Builder().setOrdinal(0).setCourse(Courses.GNOME_BASIC)
            .setIds(Invariants.GNOME_BASIC_LOG_ID).setInteraction("Walk-across")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 0 && x >= 2468 && x <= 2490 && y >= 3436 && y <= 3440 && player.idle();
                }
            }).build()),
    GNOME_BASIC_CLIMB_OBSTACLE_1(new Obstacle.Builder().setOrdinal(1).setCourse(Courses.GNOME_BASIC)
            .setIds(Invariants.GNOME_BASIC_OBSTACLE_1_ID).setInteraction("Climb-over")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 0 && x <= 2479 && x >= 2469 && y >= 3426 && y <= 3429 && player.idle();
                }
            }).build()),
    GNOME_BASIC_TREE_BRANCH(new Obstacle.Builder().setOrdinal(2).setCourse(Courses.GNOME_BASIC)
            .setIds(Invariants.GNOME_BASIC_TREE_BRANCH_ID).setInteraction("Climb")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 1 && x <= 2476 && x >= 2471 && y >= 3422 && y <= 3424 && player.idle();
                }
            }).build()),
    GNOME_BASIC_ROPE_BALANCE(new Obstacle.Builder().setOrdinal(3).setCourse(Courses.GNOME_BASIC)
            .setIds(Invariants.GNOME_BASIC_ROPE_ID).setInteraction("Walk-on")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 2 && x <= 2477 && x >= 2472 && y >= 3418 && y <= 3421 && player.idle();
                }
            }).build()),
    GNOME_BASIC_CLIMB_DOWN_TREE(new Obstacle.Builder().setOrdinal(4).setCourse(Courses.GNOME_BASIC)
            .setIds(Invariants.GNOME_BASIC_TREE_CLIMB_DOWN_ID).setInteraction("Climb-down")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 2 && x <= 2489 && x >= 2483 && y >= 3418 && y <= 3421 && player.idle();
                }
            }).build()),
    GNOME_BASIC_CLIMB_OBSTACLE_2(new Obstacle.Builder().setOrdinal(5).setCourse(Courses.GNOME_BASIC)
            .setIds(Invariants.GNOME_BASIC_OBSTACLE_2_ID).setInteraction("Climb-over")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 0 && x >= 2479 && x <= 2490 && y >= 3414 && y <= 3426 && player.idle();
                }
            }).build()),
    GNOME_BASIC_PIPE_SQUEEZE(new Obstacle.Builder().setOrdinal(6).setCourse(Courses.GNOME_BASIC)
            .setIds(Invariants.GNOME_BASIC_PIPE_IDS).setInteraction("Squeeze-through")
            .setArea(new ContextCondition() {
                @Override
                public boolean accept(Methods methods) {
                    Player player = methods.context.players.local();
                    Tile t = player.tile();
                    int x = t.x(), y = t.y(), floor = t.floor();
                    return floor == 0 && x >= 2479 && x <= 2490 && y >= 3427 && y <= 3431 && player.idle();
                }
            }).build()),;

    private final Obstacle obstacle;

    Obstacles(final Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }
}