package org.sol.script.grenwall;

import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.*;
import org.sol.api.methods.Banking;
import org.sol.api.methods.Summoning;
import org.sol.core.script.Methods;
import org.sol.core.script.OmniScript;
import org.sol.script.grenwall.node.BoxTrap;
import org.sol.script.grenwall.node.Hunt;
import org.sol.script.grenwall.node.Walk;
import org.sol.script.grenwall.util.GrenwallGui;
import org.sol.script.grenwall.util.Invariants;
import org.sol.script.grenwall.util.TeleportMethod;
import org.sol.util.Condition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

//TODO: failure at dense forest 1. maybe add failtile
//TODO: fix shitty navigation
//TODO: refactor BoxTrap
//TODO: camera turn to bank. tele out wait a bit
//TODO: graphics for thread

//v. 2.0
@Script.Manifest(description = "Hunt grenwalls and collect their spikes for some of the quickest money in the game!",
        name = "OmniGrenwalls",
        properties = "hidden=false;topic=1057235")
public class OmniGrenwalls extends OmniScript<ClientContext> implements Script, MessageListener {

    public static final List<Tile> tiles = new ArrayList<Tile>();
    public static final List<BoxTrap> traps = new ArrayList<BoxTrap>();

    //private long lastSend;

    public static TeleportMethod teleportMethod;

    public OmniGrenwalls() {
        tiles.clear();
        traps.clear();
    }

    @Override
    protected void setup() {
        //Bot.setSpeed(Mouse.Speed.FAST); // TODO

        methods.addLoot(Invariants.GRENWALL_SPIKES, "Spikes");

        teleportMethod = new TeleportMethod(methods);

        /*context.getContainer().submit(new LoopTask(context) {
            @Override
            public int loop() {
                if (System.currentTimeMillis() - lastSend > 5000 && context.omniPaint != null) {
                    final String name = Environment.getDisplayName();
                    context.omniOnlineCounter.updateStats("grenwalls",
                            name,
                            2 * System.currentTimeMillis() - context.omniPaint.RUN_TIME.getElapsed(),
                            org.org.sol.core.ui.Paint.format(context.omniPaint.RUN_TIME.getElapsed()),
                            moneyGained,
                            moneyPerHour,
                            context.omniPaint.SKILL_LIST[21].expPerHour);

                    lastSend = System.currentTimeMillis();
                }
                return 50;
            }
        });*/

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new GrenwallGui();
                gui.setVisible(true);
                ((GrenwallGui) gui).startButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        methods.getContainer().submit(new Runnable() {
                            @Override
                            public void run() {
                                teleportMethod.init((TeleportMethod.Method) (((GrenwallGui) gui).teleportMethodBox.getSelectedItem()));

                                Setup setup = (Setup) (((GrenwallGui) gui).setupBox.getSelectedItem());
                                int randomIndex = (int) (Math.random() * ((setup.getConfigs().length - 1) + 1));
                                setup.addTiles(methods, randomIndex);
                                methods.activate(new Banking(methods, teleportMethod.getBankArea()) {
                                    @Override
                                    public void addWithdrawables() {
                                        methods.withdrawables.put(Invariants.BOX_TRAP, 7);
                                        methods.withdrawables.put(Summoning.SUMMONING_POTIONS[0], 2);
                                        methods.withdrawables.put(Invariants.FRUIT_FALL_SCROLL, 200);
                                        methods.withdrawables.put(Invariants.FRUIT_BAT_POUCH, 7);

                                        methods.withdrawables.put(Invariants.HERBICIDE, -1);
                                        methods.withdrawables.put(Invariants.BONE_CRUSHER, -1);
                                        methods.withdrawables.put(Invariants.PAPAYA_FRUIT, -1);
                                        methods.withdrawables.put(Invariants.RAW_PAWYA_MEAT, -1);
                                        methods.withdrawables.put(Invariants.GRENWALL_SPIKES, -1);
                                        methods.withdrawables.put(Summoning.SUMMONING_POTIONS[1], -1);
                                        methods.withdrawables.put(Summoning.SUMMONING_POTIONS[2], -1);
                                        methods.withdrawables.put(Summoning.SUMMONING_POTIONS[3], -1);

                                        if (teleportMethod.getMethod() == TeleportMethod.Method.VARROCK) {
                                            methods.withdrawables.put(Invariants.VARROCK_TELETAB, 2);
                                        }
                                    }

                                    @Override
                                    public boolean needsToBank() {
                                        return outOfBoxes() || outOfPouches() || outOfScrolls() || outOfPotion();
                                    }

                                    @Override
                                    public void exit() {
                                        if (methods.summoning.isSummoned()) {
                                            methods.summoning.dismiss();
                                            return;
                                        }

                                        methods.status = "Teleporting out";
                                        if (!methods.context.groundItems.select(10).select(new Filter<GroundItem>() {
                                            @Override
                                            public boolean accept(GroundItem groundItem) {
                                                return groundItem.name().toLowerCase().contains("box")
                                                        && OmniGrenwalls.tiles.contains(groundItem.tile());
                                            }
                                        }).first().isEmpty()) {
                                            final GroundItem item = methods.context.groundItems.poll();
                                            methods.context.camera.turnTo(item);
                                            if (item.interact("Take", item.name()) || item.interact("Check", item.name())) {
                                                methods.sleep.sleep(new Condition() {
                                                    @Override
                                                    public boolean accept() {
                                                        return item.valid() && item.inViewport();
                                                    }
                                                }, 2000);
                                            }
                                        } else if (!methods.context.objects.select(10).select(new Filter<GameObject>() {
                                            @Override
                                            public boolean accept(GameObject sceneObject) {
                                                return sceneObject.name().toLowerCase().contains("box")
                                                        && OmniGrenwalls.tiles.contains(sceneObject.tile());
                                            }
                                        }).first().isEmpty()) {
                                            final GameObject object = methods.context.objects.poll();
                                            if (object.interact("Dismantle", object.name()) || object.interact("Check", object.name())) {
                                                methods.sleep.sleep(new Condition() {
                                                    @Override
                                                    public boolean accept() {
                                                        return object.valid() && object.inViewport();
                                                    }
                                                }, 2000);
                                            }
                                        } else {
                                            if (!methods.summoning.isSummoned()) {
                                                methods.summoning.summon(Summoning.Monster.FRUIT_BAT);
                                            } else {
                                                teleportMethod.getMethod().teleport(methods);
                                            }
                                        }
                                    }

                                    @Override
                                    public void walkToBank() {
                                        methods.status = "Walking to bank.";
                                        final TilePath pathToBank = teleportMethod.getPathToBank();
                                        if (pathToBank.traverse()) {
                                            methods.sleep.sleep(new Condition() {
                                                @Override
                                                public boolean accept() {
                                                    return !(pathToBank.next().matrix(methods.context).onMap());
                                                }
                                            }, 1000);
                                        }
                                    }
                                });

                                provide(new Hunt(methods), new Walk(methods));

                                ready = true;
                            }
                        });
                        gui.dispose();
                    }
                });
            }
        });
    }

    @Override
    public void repaint(Graphics g) {
        super.repaint(g);

        final Graphics2D graphics = (Graphics2D) g;
        if (Hunt.huntArea != null && Hunt.huntArea.contains(methods.context.players.local().tile())) {
            for (final BoxTrap trap : traps) {
                if (trap != null) {
                    final BoxTrap.State state = trap.getState();

                    graphics.setStroke(new BasicStroke(3));

                    if (state != null) {
                        graphics.setColor(state.getColor());
                        TileMatrix matrix = trap.getLocation().matrix(methods.context);
                        graphics.draw(matrix.bounds());
                        /*Point p = matrix.getCenterPoint();
                        String string = org.org.sol.core.ui.Paint.format(trap.timer.getElapsed());
                        string = string.substring(string.lastIndexOf(":") + 1);
                        g.drawString(trap.getState() + " " + string, p.x, p.y);*/
                    }
                }
            }
        }
    }

    @Override
    protected void addSkillsTrained() {
        this.skillsTrained.add("Hunter");
    }

    @Override
    public void messaged(MessageEvent event) {
        final String string = event.text().toLowerCase();
        if (event.eventId == 0 || event.text() == null || event.source().equals("")) {
            if (string.startsWith("you place") || string.contains("already baited this trap")) {
                BoxTrap.State state;

                if (string.contains("papaya fruit in the box trap as bait")) {
                    state = BoxTrap.State.BAITED_PAWYA;
                } else if (string.contains("raw pawya meat in the box trap as bait")) {
                    state = BoxTrap.State.BAITED_GRENWALL;
                } else {
                    state = BoxTrap.State.BAITED_PAWYA;
                }
                for (final BoxTrap trap : traps) {
                    if (trap.state.equals(BoxTrap.State.LAID)) {
                        trap.state = state;
                        trap.timer = new org.sol.util.Timer(0);
                    }
                }
            }
        }
    }

    public enum Setup {
        X_SETUP(new int[][]{
                {2265, 3242},
                {2268, 3242},
                {2265, 3239},
                {2268, 3239}
        }) {
            @Override
            public void addTiles(final Methods methods, final int index) {
                int[] xy = this.configs[index];
                int x = xy[0], y = xy[1];
                tiles.add(new Tile(x, y, 0));
                tiles.add(new Tile(x + 2, y, 0));
                tiles.add(new Tile(x, y + 2, 0));
                tiles.add(new Tile(x + 2, y + 2, 0));
                if (methods.context.skills.level(21) >= 80) {
                    tiles.add(new Tile(x + 1, y + 1, 0));
                }
            }
        },
        THREE_TWO_SETUP(new int[][]{
                {2265, 3244},
                {2265, 3241},
        }) {
            @Override
            public void addTiles(final Methods methods, final int index) {
                int[] xy = this.configs[index];
                int x = xy[0], y = xy[1];
                tiles.add(new Tile(x, y, 0));
                tiles.add(new Tile(x + 2, y, 0));
                tiles.add(new Tile(x + 4, y, 0));
                tiles.add(new Tile(x + 1, y - 1, 0));
                if (methods.context.skills.level(21) >= 80) {
                    tiles.add(new Tile(x + 3, y - 1, 0));
                }
            }
        };

        final int[][] configs;

        Setup(final int[][] configs) {
            this.configs = configs;
        }

        public abstract void addTiles(final Methods methods, final int index);

        public int[][] getConfigs() {
            return configs;
        }
    }

    private boolean outOfPouches() {
        return methods.context.backpack.select().id(Invariants.FRUIT_BAT_POUCH).count() == 0 && methods.summoning.getSecondsLeft() == 0;
    }

    private boolean outOfBoxes() {
        return methods.context.backpack.select().id(Invariants.BOX_TRAP).count() + methods.context.groundItems.select(10).id(Invariants.BOX_TRAP).count() +
                methods.context.objects.select(10).select(new Filter<GameObject>() {
                    @Override
                    public boolean accept(final GameObject gameObject) {
                        return gameObject.name().toLowerCase().contains("box");
                    }
                }).size() < OmniGrenwalls.tiles.size();
    }

    private boolean outOfPotion() {
        return methods.context.backpack.select().id(Summoning.SUMMONING_POTIONS).count() == 0
                && methods.summoning.getPoints() == 0;
    }

    private boolean outOfScrolls() {
        return methods.context.backpack.select().id(Invariants.FRUIT_FALL_SCROLL).count() == 0;
    }
}