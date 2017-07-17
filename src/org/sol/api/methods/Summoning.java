package org.sol.api.methods;

import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Hud;
import org.powerbot.script.rt6.Item;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;
import org.sol.script.grenwall.util.Invariants;
import org.sol.util.Condition;

public class Summoning extends Contextable {
    private static final int SUMMONING_WIDGET = 1506;
    private static final int SUMMONING_COMPONENT = 5;//or 148

    private static final int SETTING_TIME_LEFT = 1786;
    private static final int SETTING_SPECIAL_POINTS = 1787;
    private static final int SETTING_SUMMONED_FAMILIAR = 1831;

    public static final int[] SUMMONING_POTIONS = {12140, 12142, 12144, 12146};

    public Summoning(Methods methods) {
        super(methods);
    }

    public int getSpecialPoints() {
        return methods.context.varpbits.varpbit(SETTING_SPECIAL_POINTS) & 0x3f;
    }

    public int getPoints() {
        return methods.context.skills.level(org.powerbot.script.rt6.Constants.SKILLS_SUMMONING);
    }

    public boolean isSummoned() {
        return methods.context.game.loggedIn() && getSummonedPouchId() > 0 && getSecondsLeft() > 0;
    }

    public boolean interact(final String act) {
        final Component c = methods.context.widgets.component(SUMMONING_WIDGET, 4);
        return c != null && isSummoned() && c.visible() && c.interact(act);
    }

    public boolean cast() {
        return isSummoned() && interact("Cast");
    }

    public boolean summon(final Monster monster) {
        if (isSummoned()) return false;
        if (getPoints() < 10) return false;
        if ((methods.context.hud.floating(Hud.Window.BACKPACK) || methods.context.hud.open(Hud.Window.BACKPACK)) &&
                methods.context.backpack.select().id(monster.getPouchId()).count() > 0) {
            methods.status = "Summoning familiar";
            if (!methods.context.backpack.select().id(Invariants.FRUIT_BAT_POUCH).first().isEmpty()) {
                final Item pouch = methods.context.backpack.poll();
                final Component widget;
                if ((widget = pouch.component()) != null) {
                    if (widget.interact("Summon")) {
                        return methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return !methods.summoning.isSummoned();
                            }
                        }, 2500);
                    }
                }
            }
        }
        return false;
    }

    public boolean drink() {
        if (methods.summoning.getPoints() < 10 &&
                ((methods.context.hud.floating(Hud.Window.BACKPACK) || methods.context.hud.open(Hud.Window.BACKPACK)) &&
                        methods.context.backpack.select().id(SUMMONING_POTIONS).count() > 0)) {
            methods.status = "Drinking potion";
            if (!methods.context.backpack.select().id(SUMMONING_POTIONS).first().isEmpty()) {
                final Item potion = methods.context.backpack.poll();
                final Component widget;
                if ((widget = potion.component()) != null) {
                    final int beforeId = widget.itemId();
                    if (widget.interact("Drink")) {
                        methods.sleep.sleep(new Condition() {
                            @Override
                            public boolean accept() {
                                return widget.itemId() == beforeId;
                            }
                        }, 2500);
                    }
                }
            }
        }
        return false;
    }

    public boolean renew() {
        return isSummoned() && interact("Renew Familiar");
    }

    public void dismiss() {
        methods.status = "Dismissing familiar.";
        final Component widget = methods.context.widgets.widget(1188).component(11);
        if (widget.valid() && widget.visible()) {
            if (methods.context.input.sendln("1")) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return isSummoned();
                    }
                }, 3000);
            }
        } else {
            if (interact("Dismiss")) {
                methods.sleep.sleep(new Condition() {
                    @Override
                    public boolean accept() {
                        return !widget.valid() || !widget.visible();
                    }
                }, 3000);
            }
        }
    }

    public int getSummonedPouchId() {
        return methods.context.varpbits.varpbit(SETTING_SUMMONED_FAMILIAR);
    }

    public int getSecondsLeft() {
        return (methods.context.varpbits.varpbit(SETTING_TIME_LEFT) >>> 6) * 30;
    }

    public boolean canRenew() {
        return getSecondsLeft() <= 150;
    }

    public enum Monster {
        FRUIT_BAT(12033);

        private final int pouchId;

        Monster(final int pouchId) {
            this.pouchId = pouchId;
        }

        public int getPouchId() {
            return pouchId;
        }
    }
}