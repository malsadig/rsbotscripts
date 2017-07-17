package org.sol.script.fungus;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Hud;
import org.powerbot.script.rt6.Item;
import org.sol.api.path.Obstacle;
import org.sol.api.path.Path;
import org.sol.core.node.Node;
import org.sol.core.script.Methods;
import org.sol.util.Area;
import org.sol.util.Condition;

import java.util.ArrayList;

public class Farm extends Node {
    private final Path path;
    private final int foodId;

    private ArrayList<Tile> tiles;
    private Tile tile;

    private Area farmArea;

    public Farm(Methods methods, final int foodId) {
        super(methods);

        farmArea = new Area(
                new Tile(3414, 3455, 0), new Tile(3419, 3455, 0),
                new Tile(3423, 3458, 0), new Tile(3428, 3456, 0),
                new Tile(3434, 3455, 0), new Tile(3439, 3455, 0),
                new Tile(3444, 3457, 0), new Tile(3448, 3454, 0),
                new Tile(3453, 3455, 0), new Tile(3458, 3455, 0),
                new Tile(3461, 3451, 0), new Tile(3462, 3446, 0),
                new Tile(3463, 3441, 0), new Tile(3465, 3436, 0),
                new Tile(3467, 3431, 0), new Tile(3467, 3426, 0),
                new Tile(3467, 3421, 0), new Tile(3467, 3416, 0),
                new Tile(3467, 3411, 0), new Tile(3466, 3406, 0),
                new Tile(3461, 3403, 0), new Tile(3455, 3402, 0),
                new Tile(3450, 3401, 0), new Tile(3443, 3400, 0),
                new Tile(3438, 3400, 0), new Tile(3433, 3400, 0),
                new Tile(3428, 3400, 0), new Tile(3422, 3405, 0),
                new Tile(3418, 3408, 0), new Tile(3415, 3412, 0),
                new Tile(3414, 3418, 0), new Tile(3412, 3423, 0),
                new Tile(3409, 3428, 0), new Tile(3405, 3431, 0),
                new Tile(3403, 3436, 0), new Tile(3403, 3441, 0),
                new Tile(3406, 3445, 0), new Tile(3408, 3450, 0),
                new Tile(3408, 3455, 0), new Tile(3413, 3456, 0)
        );

        this.path = new Path(methods, new Obstacle(methods, "Gate", -1, "Open",
                new Tile(3511, 3478, 0), new Tile(3507, 3481, 0),
                new Tile(3503, 3484, 0), new Tile(3499, 3487, 0),
                new Tile(3494, 3489, 0), new Tile(3489, 3487, 0),
                new Tile(3486, 3483, 0), new Tile(3483, 3479, 0),
                new Tile(3478, 3477, 0), new Tile(3473, 3475, 0),
                new Tile(3468, 3474, 0), new Tile(3463, 3471, 0),
                new Tile(3458, 3469, 0), new Tile(3453, 3467, 0),
                new Tile(3448, 3466, 0), new Tile(3443, 3465, 0),
                new Tile(3440, 3461, 0)) {
            @Override
            public boolean accept() {
                if (!methods.context.objects.select(10).name(getName()).isEmpty()) {
                    GameObject fence = methods.context.objects.poll();
                    if (fence.tile().y() > methods.context.players.local().tile().y() &&
                            !farmArea.contains(methods.context.players.local().tile())) {
                        return false;
                    }
                }
                return !farmArea.contains(methods.context.players.local().tile());
            }
        });

        this.foodId = foodId;

        tiles = new ArrayList<Tile>() {
            {
                add(new Tile(3436, 3453, 0));
                add(new Tile(3435, 3447, 0));
                add(new Tile(3421, 3438, 0));
            }
        };
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void run() {
        methods.context.camera.pitch(true);
        if (!methods.context.hud.floating(Hud.Window.BACKPACK)) {
            methods.context.hud.open(Hud.Window.BACKPACK);
            return;
        }

        if (methods.banking.activate()) {
            tile = null;
            if (methods.context.players.local().healthPercent() > 70) {
                if (methods.withdrawables.containsKey(foodId)) {
                    methods.withdrawables.remove(foodId);
                }
            } else {
                if (!methods.withdrawables.containsKey(foodId)) {
                    methods.withdrawables.put(foodId, 4);
                }
            }
            methods.banking.run();
            return;
        }

        if (path.isComplete()) {
            if (tile == null) {
                tile = getTile();
            }

            if (methods.context.players.local().tile().equals(tile)) {
                if (!methods.context.objects.select(2).name("Fungi on log").isEmpty()) {
                    final GameObject fungus = methods.context.objects.poll();
                    if (fungus.interact("Pick")) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return fungus != null && fungus.valid() && fungus.inViewport();
                            }
                        }, 1000);
                    }
                } else {
                    if (!methods.context.backpack.select().id(Invariants.SICKLE_ID).isEmpty()) {
                        final Item sickle = methods.context.backpack.poll();
                        if (sickle.interact("Bloom")) {
                            methods.sleep.sleep(new Condition() {
                                @Override
                                public boolean accept() {
                                    return methods.context.objects.select(2).name("Fungi on log").isEmpty()
                                            || methods.context.players.local().animation() == 1100;
                                }
                            }, 1000);
                        }
                    }
                }
            } else {
                if (tile.matrix(methods.context).inViewport()) {
                    methods.context.camera.turnTo(tile);
                    final Tile oldLocation = methods.context.players.local().tile();
                    if (tile.matrix(methods.context).interact("Walk here")) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return methods.context.players.local().tile().equals(oldLocation) || methods.context.players.local().idle();
                            }
                        }, 3500);
                    }
                } else {
                    methods.context.camera.turnTo(tile);
                    if (methods.context.movement.step(tile)) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return !tile.matrix(methods.context).inViewport();
                            }
                        }, 2500);
                    }
                }
            }
        } else if (!methods.context.backpack.select().id(foodId).isEmpty() && methods.context.players.local().healthPercent() < 50) {
            final Item food = methods.context.backpack.poll();
            if (food.interact("Eat")) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return food.valid();
                    }
                }, 1500);
            }
        } else {
            path.run();
        }
    }

    public Tile getTile() {
        for (final Tile tile : tiles) {
            if (methods.context.players.select().at(tile).isEmpty()) {
                return tile;
            }
        }
        return tiles.get(0);
    }
}