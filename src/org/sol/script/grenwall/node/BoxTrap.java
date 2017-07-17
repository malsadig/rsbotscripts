package org.sol.script.grenwall.node;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.GroundItem;
import org.powerbot.script.rt6.Item;
import org.sol.core.script.Methods;
import org.sol.script.grenwall.util.Invariants;
import org.sol.util.Condition;
import org.sol.util.Timer;

import java.awt.*;

public class BoxTrap {
    public State state;
    private final Tile location;

    public Timer timer;
    private final Methods methods;

    public BoxTrap(final Methods methods, final Tile location) {
        this.location = location;
        this.timer = new Timer(0);
        this.state = State.MISSING;

        this.methods = methods;
    }

    public void setState() {
        for (final State state : State.values()) {
            if (state.activate(this, methods) && (this.state == null || !this.state.equals(state))) {
                this.state = state;
                if (!this.state.name().toLowerCase().contains("baited")) {
                    this.timer = new Timer(0);
                }
            }
        }
    }

    public long getPriority() {
        return timer.getElapsed();
    }

    public State getState() {
        return this.state;
    }

    public Tile getLocation() {
        return this.location;
    }

    public enum State implements Comparable<State> {
        DISMANTLED(Invariants.BOX_TRAP, Color.ORANGE, 1) {
            @Override
            public boolean activate(final BoxTrap boxTrap, final Methods methods) {
                return !methods.context.groundItems.select(10).at(boxTrap.getLocation()).id(this.getId()).first().isEmpty()
                        && methods.context.players.local().animation() != 5208;
            }

            @Override
            public void execute(final BoxTrap boxTrap, final Methods methods) {
                final GroundItem groundBait;
                if (!methods.context.groundItems.select(10).at(boxTrap.getLocation()).id(Invariants.PAPAYA_FRUIT, Invariants.RAW_PAWYA_MEAT).first().isEmpty()
                        && (groundBait = methods.context.groundItems.poll()) != methods.context.groundItems.nil()) {
                    methods.status = "Picking up " + groundBait.name();
                    methods.context.camera.turnTo(groundBait);
                    if (groundBait.interact("Take", groundBait.name())) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return groundBait.valid() && groundBait.inViewport();
                            }
                        }, 2500);
                    }
                } else {
                    final GroundItem box;
                    if (!methods.context.groundItems.select(10).at(boxTrap.getLocation()).id(this.getId()).first().isEmpty()
                            && (box = methods.context.groundItems.poll()) != methods.context.groundItems.nil()) {
                        if (box.interact("Lay", box.name())) {
                            methods.sleep.sleep(new Condition() {
                                @Override
                                public boolean accept() {
                                    return box.valid() && box.inViewport();
                                }
                            }, 3000);
                            boxTrap.state = LAID;
                        }
                    }
                }
            }
        },

        SHAKING(28906, Color.GREEN, 2) {
            @Override
            public boolean activate(final BoxTrap boxTrap, final Methods methods) {
                return !methods.context.objects.select(10).at(boxTrap.getLocation()).id(this.getId(), 28913).first().isEmpty()
                        && methods.context.players.local().animation() != 5212;
            }

            @Override
            public void execute(final BoxTrap boxTrap, final Methods methods) {
                final GameObject box;
                if (!methods.context.objects.select(10).at(boxTrap.getLocation()).id(this.getId(), 28913).first().isEmpty()
                        && (box = methods.context.objects.poll()) != methods.context.objects.nil()) {
                    if (box.interact("Check", box.name())) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return box.valid() && box.inViewport();
                            }
                        }, 3000);
                    }
                }
            }
        },

        FAILED(19192, Color.RED, 3) {
            @Override
            public boolean activate(final BoxTrap boxTrap, final Methods methods) {
                return !methods.context.objects.select(10).at(boxTrap.getLocation()).id(this.getId()).first().isEmpty()
                        && methods.context.players.local().animation() != 5212;
            }

            @Override
            public void execute(final BoxTrap boxTrap, final Methods methods) {
                final GameObject box;
                if (!methods.context.objects.select(10).at(boxTrap.getLocation()).id(this.getId()).first().isEmpty()
                        && (box = methods.context.objects.poll()) != methods.context.objects.nil()) {
                    if (box.interact("Dismantle", box.name())) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return box.valid() && box.inViewport();
                            }
                        }, 3000);
                    }
                }
            }
        },

        LAID(19187, Color.YELLOW, 4) {
            @Override
            public boolean activate(final BoxTrap boxTrap, final Methods methods) {
                return !(boxTrap.getState() != null && boxTrap.getState().name().toLowerCase().contains("baited"))
                        && !methods.context.objects.select(10).at(boxTrap.getLocation()).id(this.getId()).isEmpty()
                        && ((methods.context.backpack.select().id(Invariants.RAW_PAWYA_MEAT).count() > 0
                        && Hunt.getNumber(BAITED_GRENWALL) < 3)
                        || methods.context.backpack.select().id(Invariants.PAPAYA_FRUIT).count() > 0);

            }

            @Override
            public void execute(final BoxTrap boxTrap, final Methods methods) {
                final int baitId;

                if (Hunt.getNumber(BAITED_GRENWALL) < 3) {
                    if (methods.context.backpack.select().id(Invariants.RAW_PAWYA_MEAT).count() > 0) {
                        baitId = Invariants.RAW_PAWYA_MEAT;
                    } else {
                        baitId = Invariants.PAPAYA_FRUIT;
                    }
                } else {
                    baitId = Invariants.PAPAYA_FRUIT;
                }

                final Item bait;
                final Component widget;

                if (!methods.context.backpack.select().id(baitId).first().isEmpty()
                        && (bait = methods.context.backpack.poll()) != methods.context.backpack.nil()
                        && (widget = bait.component()) != null) {
                    if (methods.context.players.local().idle()) {
                        if (methods.context.backpack.itemSelected()) {
                            if (!methods.context.objects.select(10).at(boxTrap.getLocation()).id(this.getId()).first().isEmpty()) {
                                final GameObject box = methods.context.objects.poll();
                                if (box.interact("Use", box.name())) {
                                    methods.sleep.sleep(new Condition() {
                                        @Override
                                        public boolean accept() {
                                            return methods.context.backpack.itemSelected()
                                                    || widget.itemId() != -1;
                                        }
                                    }, 2500);
                                }
                            }
                        } else {
                            if (widget.interact("Use")) {
                                methods.sleep.sleep(new Condition() {
                                    @Override
                                    public boolean accept() {
                                        return !methods.context.backpack.itemSelected();
                                    }
                                }, 2000);
                            }
                        }
                    }
                } else {
                    Hunt.cast(methods);
                }
            }
        },

        MISSING(Invariants.BOX_TRAP, Color.WHITE, 5) {
            @Override
            public boolean activate(final BoxTrap boxTrap, final Methods methods) {
                return methods.context.objects.select(10).at(boxTrap.getLocation()).select(new Filter<GameObject>() {
                    @Override
                    public boolean accept(GameObject gameObject) {
                        return gameObject.name().toLowerCase().contains("box");
                    }
                }).isEmpty() && methods.context.groundItems.select(10).at(boxTrap.getLocation()).select(new Filter<GroundItem>() {
                    @Override
                    public boolean accept(GroundItem groundItem) {
                        return groundItem.name().toLowerCase().contains("box");
                    }
                }).isEmpty();
            }

            @Override
            public void execute(final BoxTrap boxTrap, final Methods methods) {
                if (!methods.context.players.local().tile().equals(boxTrap.getLocation())) {
                    if (boxTrap.getLocation().matrix(methods.context).inViewport()) {
                        methods.context.camera.turnTo(boxTrap.getLocation());
                        final Tile oldLocation = methods.context.players.local().tile();
                        if (boxTrap.getLocation().matrix(methods.context).interact("Walk here")) {
                            methods.sleep.sleep(new Condition() {
                                @Override
                                public boolean accept() {
                                    return methods.context.players.local().tile().equals(oldLocation) || methods.context.players.local().inMotion();
                                }
                            }, 3500);
                        }
                    } else {
                        methods.context.camera.turnTo(boxTrap.getLocation());
                        if (methods.context.movement.step(boxTrap.getLocation())) {
                            methods.sleep.sleep(new Condition() {
                                @Override
                                public boolean accept() {
                                    return !boxTrap.getLocation().matrix(methods.context).inViewport();
                                }
                            }, 2500);
                        }
                    }
                } else {
                    if (!methods.context.players.local().inMotion() && methods.context.players.local().animation() == -1) {
                        final Item box;
                        final Component boxWidget;
                        if (!methods.context.backpack.select().id(this.getId()).first().isEmpty()
                                && (box = methods.context.backpack.poll()) != methods.context.backpack.nil()) {
                            boxWidget = box.component();
                            if (box.component().interact("Lay")) {
                                methods.sleep.sleep(new Condition() {
                                    @Override
                                    public boolean accept() {
                                        return methods.context.players.local().animation() != 5208;
                                    }
                                }, 2500);
                            }

                            final int baitId;
                            if (methods.context.backpack.select().id(Invariants.RAW_PAWYA_MEAT).count() > 0 && Hunt.getNumber(BAITED_GRENWALL) < 3) {
                                baitId = Invariants.RAW_PAWYA_MEAT;
                            } else {
                                baitId = Invariants.PAPAYA_FRUIT;
                            }

                            final Item bait;
                            final Component widget;

                            if (!methods.context.backpack.select().id(baitId).first().isEmpty()
                                    && (bait = methods.context.backpack.poll()) != methods.context.backpack.nil()) {
                                if ((widget = bait.component()) != null && widget.interact("Use")) {
                                    methods.sleep.sleep(new Condition() {
                                        @Override
                                        public boolean accept() {
                                            return methods.context.players.local().animation() == 5208 || boxWidget.itemId() != -1;
                                        }
                                    }, 2000);
                                }
                            }
                        }
                    }
                }
            }
        },

        BAITED_GRENWALL(19187, Color.CYAN, 6) {
            @Override
            public boolean activate(final BoxTrap boxTrap, final Methods methods) {
                return false;
            }

            @Override
            public void execute(final BoxTrap boxTrap, final Methods methods) {
            }
        },

        BAITED_PAWYA(19187, Color.MAGENTA, 6) {
            @Override
            public boolean activate(final BoxTrap boxTrap, final Methods methods) {
                return false;
            }

            @Override
            public void execute(final BoxTrap boxTrap, final Methods methods) {
            }
        };

        private final int id, priority;
        private final Color color;

        public abstract boolean activate(final BoxTrap boxTrap, final Methods methods);

        public abstract void execute(final BoxTrap boxTrap, final Methods methods);

        State(final int id, final Color color, final int priority) {
            this.id = id;
            this.color = color;
            this.priority = priority;
        }

        public int getId() {
            return this.id;
        }

        public Color getColor() {
            return this.color;
        }

        public int getPriority() {
            return this.priority;
        }
    }
}