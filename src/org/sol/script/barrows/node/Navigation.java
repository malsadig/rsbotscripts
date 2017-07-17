package org.sol.script.barrows.node;

import org.powerbot.script.Tile;
import org.sol.api.path.Obstacle;
import org.sol.api.path.Path;
import org.sol.core.node.Node;
import org.sol.core.script.Methods;

public class Navigation extends Node {
    private final Path pathToBarrows;

    public Navigation(Methods methods) {
        super(methods);

        pathToBarrows = getBarrowsPath();
        pathToBarrows.run();

        ////to solve puzzle
        ////widget 25
        ////traverse components. get to first of type 0
        ////select the one after that
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void run() {
        pathToBarrows.run();
    }

    public Path getBarrowsPath() {
        return new Path(methods,
                new Obstacle(methods, "Gate", -1, "Open",
                        new Tile(3511, 3477, 0), new Tile(3506, 3478, 0),
                        new Tile(3503, 3482, 0), new Tile(3498, 3484, 0),
                        new Tile(3493, 3486, 0), new Tile(3489, 3483, 0),
                        new Tile(3486, 3479, 0), new Tile(3481, 3477, 0),
                        new Tile(3476, 3476, 0), new Tile(3471, 3475, 0),
                        new Tile(3466, 3474, 0), new Tile(3461, 3473, 0),
                        new Tile(3457, 3470, 0), new Tile(3453, 3467, 0),
                        new Tile(3449, 3464, 0), new Tile(3444, 3462, 0),
                        new Tile(3443, 3457, 0), new Tile(-1, -1, -1)) {
                    @Override
                    public boolean accept() {
                        return methods.banking.getArea().contains(methods.context.players.local().tile());
                    }
                },
                new Obstacle(methods, "Barrows", -1, null,
                        new Tile(3443, 3456, 0), new Tile(3440, 3452, 0),
                        new Tile(3437, 3448, 0), new Tile(3436, 3443, 0),
                        new Tile(3433, 3439, 0), new Tile(3432, 3434, 0),
                        new Tile(3431, 3429, 0), new Tile(3431, 3424, 0),
                        new Tile(3432, 3419, 0), new Tile(3433, 3414, 0),
                        new Tile(3435, 3409, 0), new Tile(3437, 3404, 0),
                        new Tile(3437, 3399, 0), new Tile(3437, 3394, 0),
                        new Tile(3437, 3389, 0), new Tile(3437, 3384, 0),
                        new Tile(3436, 3379, 0), new Tile(3435, 3374, 0),
                        new Tile(3434, 3369, 0), new Tile(3434, 3364, 0),
                        new Tile(3434, 3359, 0), new Tile(3433, 3354, 0),
                        new Tile(3430, 3350, 0), new Tile(3429, 3345, 0),
                        new Tile(3427, 3340, 0), new Tile(3427, 3335, 0),
                        new Tile(3428, 3330, 0), new Tile(3429, 3325, 0),
                        new Tile(3429, 3320, 0), new Tile(3427, 3315, 0),
                        new Tile(3425, 3310, 0), new Tile(3423, 3305, 0),
                        new Tile(3422, 3300, 0), new Tile(3422, 3295, 0),
                        new Tile(3427, 3296, 0), new Tile(3430, 3292, 0),
                        new Tile(3430, 3297, 0), new Tile(3430, 3302, 0),
                        new Tile(3430, 3307, 0), new Tile(3429, 3312, 0),
                        new Tile(3431, 3317, 0), new Tile(3435, 3320, 0),
                        new Tile(3440, 3321, 0), new Tile(3445, 3322, 0),
                        new Tile(3447, 3317, 0), new Tile(3444, 3313, 0),
                        new Tile(3442, 3308, 0), new Tile(3439, 3304, 0),
                        new Tile(3438, 3299, 0), new Tile(3439, 3294, 0),
                        new Tile(3440, 3289, 0), new Tile(3444, 3286, 0),
                        new Tile(3449, 3285, 0), new Tile(3451, 3290, 0),
                        new Tile(3451, 3295, 0), new Tile(3448, 3299, 0),
                        new Tile(3446, 3304, 0), new Tile(3446, 3309, 0),
                        new Tile(3451, 3309, 0), new Tile(3456, 3309, 0),
                        new Tile(3461, 3309, 0), new Tile(3466, 3309, 0),
                        new Tile(3471, 3310, 0), new Tile(3476, 3309, 0),
                        new Tile(3479, 3305, 0), new Tile(3484, 3302, 0),
                        new Tile(3489, 3301, 0), new Tile(3494, 3299, 0),
                        new Tile(3498, 3296, 0), new Tile(3501, 3292, 0),
                        new Tile(3505, 3289, 0), new Tile(3510, 3287, 0),
                        new Tile(3514, 3284, 0), new Tile(3518, 3281, 0),
                        new Tile(3523, 3280, 0), new Tile(3528, 3279, 0),
                        new Tile(3533, 3279, 0), new Tile(3538, 3280, 0),
                        new Tile(3539, 3285, 0), new Tile(3539, 3290, 0),
                        new Tile(3540, 3295, 0), new Tile(3541, 3300, 0),
                        new Tile(3543, 3305, 0), new Tile(3546, 3309, 0),
                        new Tile(3550, 3312, 0), new Tile(3555, 3313, 0),
                        new Tile(3560, 3314, 0), new Tile(3564, 3311, 0),
                        new Tile(3565, 3306, 0), new Tile(3566, 3304, 0)) {
                    @Override
                    public boolean accept() {
                        return !methods.banking.getArea().contains(methods.context.players.local().tile());
                    }
                }
        );
    }

    public static interface CollisionFlags {
        final int REQUIRED_FLAG = 0x1280100,
                WALL_NORTHWEST = 0x1,
                WALL_NORTH = 0x2,
                WALL_NORTHEAST = 0x4,
                WALL_EAST = 0x8,
                WALL_SOUTHEAST = 0x10,
                WALL_SOUTH = 0x20,
                WALL_SOUTHWEST = 0x40,
                WALL_WEST = 0x80,
                OBJECT_TILE = 0x100,
                UNKNOWN = 0x80000,
                BLOCKED_TILE = 0x200000,
                UNLOADED_TILE = 0x1000000;
    }
}