package org.sol.script.barrows;

import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.sol.api.methods.Banking;
import org.sol.api.methods.House;
import org.sol.core.script.OmniScript;
import org.sol.script.barrows.node.Combat;
import org.sol.script.barrows.node.Navigation;
import org.sol.script.barrows.node.Trip;
import org.sol.util.Area;

@Script.Manifest(description = "Completes the Barrows minigame for massive money making!",
        name = "OmniBarrows",
        properties = "hidden=true")
public class OmniBarrows extends OmniScript<ClientContext> {
    private Trip trip;

    @Override
    protected void setup() {
        System.out.println("here3");
        methods.activate(new Banking(methods, new Area(new Tile[]{
                new Tile(3412, 3457, 0), new Tile(3417, 3457, 0),
                new Tile(3421, 3460, 0), new Tile(3426, 3458, 0),
                new Tile(3431, 3457, 0), new Tile(3436, 3457, 0),
                new Tile(3441, 3459, 0), new Tile(3446, 3461, 0),
                new Tile(3449, 3457, 0), new Tile(3454, 3458, 0),
                new Tile(3458, 3461, 0), new Tile(3463, 3463, 0),
                new Tile(3468, 3461, 0), new Tile(3471, 3466, 0),
                new Tile(3474, 3470, 0), new Tile(3479, 3471, 0),
                new Tile(3483, 3468, 0), new Tile(3488, 3466, 0),
                new Tile(3493, 3465, 0), new Tile(3498, 3465, 0),
                new Tile(3503, 3466, 0), new Tile(3508, 3467, 0),
                new Tile(3513, 3470, 0), new Tile(3517, 3473, 0),
                new Tile(3520, 3479, 0), new Tile(3520, 3487, 0),
                new Tile(3519, 3495, 0), new Tile(3519, 3500, 0),
                new Tile(3518, 3505, 0), new Tile(3516, 3510, 0),
                new Tile(3512, 3513, 0), new Tile(3507, 3514, 0),
                new Tile(3502, 3516, 0), new Tile(3497, 3518, 0),
                new Tile(3492, 3518, 0), new Tile(3486, 3519, 0),
                new Tile(3481, 3519, 0), new Tile(3475, 3519, 0),
                new Tile(3470, 3519, 0), new Tile(3465, 3519, 0),
                new Tile(3458, 3519, 0), new Tile(3452, 3519, 0),
                new Tile(3447, 3519, 0), new Tile(3442, 3518, 0),
                new Tile(3437, 3517, 0), new Tile(3433, 3521, 0),
                new Tile(3428, 3520, 0), new Tile(3423, 3519, 0),
                new Tile(3418, 3519, 0), new Tile(3413, 3519, 0)
        })) {
            @Override
            public void addWithdrawables() {
                methods.withdrawables.put(House.HOME_TELETAB_ID, 1);
            }

            @Override
            public boolean needsToBank() {
                return outOfFood() || trip.isComplete();
            }

            private boolean outOfFood() {
                return true;
            }

            @Override
            public void exit() {
                if (methods.house.isInSide()) {
                    if (methods.house.isInBuildingMode()) {
                        methods.house.teleport("Kharyrll");
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

        provide(new Combat(methods), new Navigation(methods));
        ready = true;
    }

    @Override
    protected void addSkillsTrained() {

    }
}