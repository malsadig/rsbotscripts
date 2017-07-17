package org.sol.script.grenwall.node;

import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GroundItem;
import org.powerbot.script.rt6.Hud;
import org.powerbot.script.rt6.Item;
import org.sol.api.methods.Summoning;
import org.sol.core.concurrent.LoopTask;
import org.sol.core.node.Node;
import org.sol.core.script.Methods;
import org.sol.script.grenwall.OmniGrenwalls;
import org.sol.script.grenwall.util.Invariants;
import org.sol.util.Area;
import org.sol.util.Condition;

public class Hunt extends Node {
    public static Area huntArea;

    public Hunt(Methods methods) {
        super(methods);

        huntArea = new Area(new Tile(2262, 3237, 0), new Tile(2274, 3253, 0));

        for (final Tile tile : OmniGrenwalls.tiles) {
            OmniGrenwalls.traps.add(new BoxTrap(methods, tile));
        }

        methods.getContainer().submit(new LoopTask(methods) {
            @Override
            public int loop() {
                if (huntArea.contains(methods.context.players.local().tile())) {
                    for (final BoxTrap trap : OmniGrenwalls.traps) {
                        trap.setState();
                    }
                }
                return 50;
            }
        });
    }

    @Override
    public boolean activate() {
        return huntArea.contains(methods.context.players.local().tile()) || methods.banking.activate();
    }

    @Override
    public void run() {
        methods.context.camera.pitch(true);
        if (!methods.context.hud.opened(Hud.Window.BACKPACK)) {
            methods.context.hud.open(Hud.Window.BACKPACK);
            return;
        }

        if (methods.banking.activate()) {
            methods.banking.run();
            return;
        }

        if (methods.summoning.getPoints() < 15) {
            methods.summoning.drink();
        }

        if (!methods.summoning.isSummoned()) {
            methods.summoning.summon(Summoning.Monster.FRUIT_BAT);
            return;
        }

        if (methods.summoning.canRenew()) {
            if (methods.summoning.renew()) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return methods.summoning.canRenew();
                    }
                }, 1500);
            }
            return;
        }

        if (Random.nextInt(0, 100) < Random.nextInt(0, 5) && Random.nextInt(0, 3) == 2) {
            methods.context.camera.angle((methods.context.camera.yaw() + Random.nextInt(0, 360)) % 360);
        }

        final BoxTrap trap = getCurrentBox();

        if (methods.context.backpack.select().count() < 26) {
            if (trap != null && trap.getState() != null) {
                int numTotal = OmniGrenwalls.tiles.size(),
                        numGrenwalls = getNumber(BoxTrap.State.BAITED_GRENWALL),
                        meatCount = methods.context.backpack.select().id(Invariants.RAW_PAWYA_MEAT).count(),
                        fruitCount = methods.context.backpack.select().id(Invariants.PAPAYA_FRUIT).count();
                if (getNumber(BoxTrap.State.MISSING) +
                        numGrenwalls +
                        getNumber(BoxTrap.State.BAITED_PAWYA) == numTotal) {
                    if ((numGrenwalls == 3 && fruitCount > 0) || (meatCount + fruitCount > 0)) {
                        build(trap);
                    } else {
                        cast(methods);
                    }
                } else {
                    build(trap);
                }
            } else {
                if (!methods.context.players.local().inMotion() && methods.context.players.local().animation() == -1) {
                    /*if (methods.context.backpack.isItemSelected()) {
                        Component child = methods.context.backpack.getSelectedItem().getComponent();
                        if (child != null && child.click(true)) {
                            methods.sleep.sleep(new Condition() {
                                @Override
                                public boolean accept() {
                                    return methods.context.backpack.isItemSelected();
                                }
                            }, 2000);
                        }
                    } else {*/
                    cast(methods);
                    //}
                }
            }
        } else {
            drop();
        }
    }

    private void build(final BoxTrap trap) {
        methods.context.camera.turnTo(trap.getLocation());
        if (!methods.context.game.inViewport(trap.getLocation().matrix(methods.context).nextPoint())) {
            methods.status = "Walking to trap";
            methods.context.movement.step(trap.getLocation());
        }
        methods.status = "Hunting'...";
        trap.getState().execute(trap, methods);
    }

    public static void cast(final Methods methods) {
        if (!methods.context.groundItems.select(5).id(Invariants.PAPAYA_FRUIT).first().isEmpty()) {
            final GroundItem fruit;
            if ((fruit = methods.context.groundItems.poll()) != methods.context.groundItems.nil()) {
                methods.context.camera.turnTo(fruit.tile());
                if (fruit.interact("Take", fruit.name())) {
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return fruit.inViewport() && fruit.valid();
                        }
                    }, 2500);
                }
            }
        } else {
            methods.status = "Casting fruit fall";

            if (methods.summoning.cast()) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return methods.context.groundItems.select(5).id(Invariants.PAPAYA_FRUIT).first().isEmpty();
                    }
                }, 3000);
            }
        }
    }

    private void drop() {
        if (!methods.context.backpack.select().select(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return !methods.withdrawables.keySet().contains(item.id()) && !item.name().equals("Bauble");
            }
        }).isEmpty()) {
            final Item droppable = methods.context.backpack.poll();
            final Component droppableWidget = droppable.component();
            methods.status = "Dropping " + droppable.name();
            if (droppableWidget.interact("Drop")) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return droppableWidget.itemId() != -1;
                    }
                }, 2000);
            }
        } else if (!methods.context.backpack.select().id(Invariants.PAPAYA_FRUIT).isEmpty()) {
            final Item fruit = methods.context.backpack.poll();
            final Component fruitWidget = fruit.component();
            methods.status = "Eating " + fruit.name();
            if (fruitWidget.interact("Eat")) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return fruitWidget.itemId() != -1;
                    }
                }, 2000);
            }
        } else if (!methods.context.backpack.select().id(Invariants.RAW_PAWYA_MEAT).isEmpty()) {
            final Item meat = methods.context.backpack.poll();
            final Component meatWidget = meat.component();
            methods.status = "Dropping " + meat.name();
            if (meatWidget.interact("Drop")) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return meatWidget.itemId() != -1;
                    }
                }, 2000);
            }
        }
    }

    BoxTrap getCurrentBox() {
        final BoxTrap finished = getFinished(), unFinished = getUnfinished();

        if (finished != null && unFinished != null) {
            if (unFinished.getState().equals(BoxTrap.State.LAID)) {
                return unFinished;
            } else {
                if (!methods.context.backpack.itemSelected()) {
                    return finished;
                }
            }
        } else if (finished == null && unFinished != null) {
            if (unFinished.getState().equals(BoxTrap.State.LAID) || !methods.context.backpack.itemSelected()) {
                boolean contained = methods.context.backpack.select().id(Invariants.PAPAYA_FRUIT, Invariants.RAW_PAWYA_MEAT).count() >= getNumber(BoxTrap.State.MISSING) + getNumber(BoxTrap.State.FAILED);
                if (getNumber(BoxTrap.State.BAITED_GRENWALL) > 2) {
                    if (methods.context.backpack.select().id(Invariants.PAPAYA_FRUIT).count() >= getNumber(BoxTrap.State.MISSING) + getNumber(BoxTrap.State.FAILED)) {
                        return unFinished;
                    }
                } else if (methods.context.backpack.select().id(Invariants.RAW_PAWYA_MEAT).count() > 0 || contained) {
                    return unFinished;
                }
            }
        } else if (finished != null) {
            if (!methods.context.backpack.itemSelected()) {
                return finished;
            }
        }

        return null;
    }

    private BoxTrap getUnfinished() {
        BoxTrap tempTrap = null;
        int priority = Integer.MAX_VALUE;

        for (final BoxTrap trap : OmniGrenwalls.traps) {
            if (trap.getState().getPriority() < priority || tempTrap == null) {
                if (trap.getState().equals(BoxTrap.State.MISSING) ||
                        trap.getState().equals(BoxTrap.State.LAID)) {
                    priority = trap.getState().getPriority();
                    tempTrap = trap;
                }
            }
        }
        return tempTrap;
    }

    private BoxTrap getFinished() {
        BoxTrap tempTrap = null;
        long priority = Long.MIN_VALUE;

        for (final BoxTrap trap : OmniGrenwalls.traps) {
            if (trap.getPriority() > priority || tempTrap == null) {
                if (trap.getState().equals(BoxTrap.State.DISMANTLED) ||
                        trap.getState().equals(BoxTrap.State.SHAKING) ||
                        trap.getState().equals(BoxTrap.State.FAILED)) {
                    priority = trap.getPriority();
                    tempTrap = trap;
                }
            }
        }
        return tempTrap;
    }

    public static int getNumber(final BoxTrap.State state) {
        int count = 0;
        for (final BoxTrap trap : OmniGrenwalls.traps) {
            if (trap.getState() != null && trap.getState().equals(state)) {
                count++;
            }
        }
        return count;
    }
}