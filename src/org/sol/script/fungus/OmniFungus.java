package org.sol.script.fungus;

import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.sol.api.methods.Banking;
import org.sol.api.methods.House;
import org.sol.core.script.OmniScript;
import org.sol.util.Area;
import org.sol.util.Condition;

import javax.swing.*;

@Script.Manifest(description = "Collect fungus for massive money making!",
        name = "OmniFungus",
        properties = "hidden=false;topic=1159046")
public class OmniFungus extends OmniScript<ClientContext> {
    @Override
    protected void setup() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String foodIdString = JOptionPane.showInputDialog("ID of Food to Eat:");
                if (foodIdString != null && !foodIdString.equals("")) {
                    final int foodId = Integer.parseInt(foodIdString);

                    methods.addLoot(Invariants.FUNGUS_ID, "Fungi");

                    methods.activate(new Banking(methods, new Area(new Tile[]{
                            new Tile(3410, 3519, 0), new Tile(3415, 3520, 0),
                            new Tile(3421, 3520, 0), new Tile(3426, 3520, 0),
                            new Tile(3431, 3520, 0), new Tile(3438, 3520, 0),
                            new Tile(3444, 3520, 0), new Tile(3449, 3518, 0),
                            new Tile(3454, 3517, 0), new Tile(3460, 3517, 0),
                            new Tile(3466, 3517, 0), new Tile(3472, 3514, 0),
                            new Tile(3477, 3514, 0), new Tile(3482, 3518, 0),
                            new Tile(3488, 3521, 0), new Tile(3493, 3522, 0),
                            new Tile(3498, 3521, 0), new Tile(3503, 3521, 0),
                            new Tile(3508, 3519, 0), new Tile(3511, 3514, 0),
                            new Tile(3514, 3510, 0), new Tile(3516, 3505, 0),
                            new Tile(3519, 3501, 0), new Tile(3519, 3496, 0),
                            new Tile(3520, 3491, 0), new Tile(3521, 3486, 0),
                            new Tile(3521, 3481, 0), new Tile(3521, 3476, 0),
                            new Tile(3518, 3472, 0), new Tile(3514, 3469, 0),
                            new Tile(3510, 3466, 0), new Tile(3505, 3463, 0),
                            new Tile(3500, 3465, 0), new Tile(3495, 3463, 0),
                            new Tile(3489, 3461, 0), new Tile(3484, 3460, 0),
                            new Tile(3479, 3460, 0), new Tile(3474, 3460, 0),
                            new Tile(3469, 3462, 0), new Tile(3464, 3462, 0),
                            new Tile(3459, 3461, 0), new Tile(3455, 3458, 0),
                            new Tile(3450, 3457, 0), new Tile(3445, 3458, 0),
                            new Tile(3440, 3460, 0), new Tile(3435, 3458, 0),
                            new Tile(3430, 3458, 0), new Tile(3425, 3458, 0),
                            new Tile(3420, 3460, 0)
                    })) {
                        public Area area = new Area(new Tile(3386, 3547, 0), new Tile(3557, 3385, 0));

                        @Override
                        public void addWithdrawables() {
                            methods.withdrawables.put(House.HOME_TELETAB_ID, 1);
                            methods.withdrawables.put(Invariants.SICKLE_ID, 1);
                            methods.withdrawables.put(foodId, 4);
                        }

                        @Override
                        public boolean needsToBank() {
                            return methods.prayer.isEmpty() || methods.context.backpack.select().count() >= 28 ||
                                    methods.house.isRightOutside() || methods.house.isInSide() ||
                                    methods.prayer.isEmpty();
                        }

                        @Override
                        public void exit() {
                            if (methods.house.isInSide()) {
                                if (methods.house.isInBuildingMode()) {
                                    if (!methods.prayer.isFull()) {
                                        methods.house.pray();
                                        methods.sleep.sleep(new Condition() {
                                            @Override
                                            public boolean accept() {
                                                return false;
                                            }
                                        }, 5000);
                                    } else {
                                        methods.house.teleport("Kharyrll");
                                    }
                                } else {
                                    methods.house.leave();
                                }
                            } else if (methods.house.isRightOutside()) {
                                methods.house.enter(true);
                            } else {
                                methods.house.homeTele();
                            }
                        }

                        @Override
                        public void walkToBank() {
                            methods.context.movement.newTilePath(new Tile(3496, 3488, 0), new Tile(3501, 3487, 0),
                                    new Tile(3504, 3483, 0), new Tile(3506, 3478, 0),
                                    new Tile(3511, 3478, 0), new Tile(3512, 3478, 0)).traverse();
                        }
                    });

                    provide(new Farm(methods, foodId));
                    ready = true;
                }
            }
        });
    }

    @Override
    protected void addSkillsTrained() {
    }
}