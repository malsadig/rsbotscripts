package org.sol.script.barrows.node;

import org.powerbot.script.Tile;
import org.sol.core.concurrent.LoopTask;
import org.sol.core.node.Node;
import org.sol.core.script.Methods;
import org.sol.util.Area;

import java.util.LinkedList;
import java.util.List;

public class Combat extends Node {
    public static Area barrowsArea;
    public static List<Tile> tiles;

    public Combat(final Methods methods) {
        super(methods);

        barrowsArea = new Area(new Tile(3562, 3307, 0), new Tile(3557, 3307, 0),
                new Tile(3553, 3304, 0), new Tile(3549, 3300, 0),
                new Tile(3548, 3295, 0), new Tile(3547, 3290, 0),
                new Tile(3547, 3285, 0), new Tile(3547, 3280, 0),
                new Tile(3546, 3275, 0), new Tile(3551, 3274, 0),
                new Tile(3556, 3274, 0), new Tile(3560, 3271, 0),
                new Tile(3565, 3269, 0), new Tile(3570, 3268, 0),
                new Tile(3574, 3271, 0), new Tile(3579, 3273, 0),
                new Tile(3582, 3277, 0), new Tile(3583, 3282, 0),
                new Tile(3583, 3287, 0), new Tile(3584, 3292, 0),
                new Tile(3584, 3297, 0), new Tile(3584, 3302, 0),
                new Tile(3582, 3307, 0));
        tiles = new LinkedList<Tile>();
        methods.getContainer().submit(new LoopTask(methods) {
            @Override
            public int loop() {
                Tile base = methods.context.game.mapOffset();
                for (int x = 0; x < base.x() + 104; x++) {
                    for (int y = 0; y < base.y() + 104; y++) {
                        Tile t = new Tile(x, y, base.floor());
                        if (!tiles.contains(t) && methods.context.movement.reachable(methods.context.players.local().tile(), t)) {
                            tiles.add(t);
                        }
                    }
                }
                return 100;
            }
        });
    }

    @Override
    public boolean activate() {
        return methods.banking.activate();
    }

    @Override
    public void run() {
        if (methods.banking.activate()) {
            methods.banking.run();
        }
    }
}