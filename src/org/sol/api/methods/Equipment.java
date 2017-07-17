package org.sol.api.methods;

import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Item;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;
import org.sol.util.Condition;

public class Equipment extends Contextable {
    private final static int WIDGET = 387;

    public Equipment(Methods methods) {
        super(methods);
    }

    int getSlot(final int id) {
        final int[] appearance = methods.context.players.local().appearance();
        for (int i = 0; i < appearance.length; i++) {
            if (appearance[i] == id) {
                return i;
            }
        }

        return -1;
    }

    public boolean isCompletelyEmpty() {
        for (final Slot equipment : Slot.values()) {
            if (equipment.appearanceIndex != -1 ? equipment.getItemId(methods) != -1 : equipment.getItem(methods).id() != -1) {
                return false;
            }
        }
        return true;
    }

    public boolean equip(final int id) {
        if (!methods.context.backpack.select().id(id).isEmpty()) {
            for (final Item item : methods.context.backpack) {
                final Component component = item.component();
                if (component != null && component.click(true)) {
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return component.id() != -1;
                        }
                    }, 750);
                    return contains(id);
                }
            }
        }
        return false;
    }

    boolean contains(final int id) {
        return getSlot(id) != -1;
    }

    public enum Slot {
        HELMET(6, 0),
        CAPE(9, 1),
        NECK(12, 2),
        WEAPON(15, 3),
        TORSO(18, 4),
        SHIELD(21, 5),
        LEGS(24, 7),
        GLOVES(27, 9),
        BOOTS(30, 10),
        RING(33, -1),
        QUIVER(36, -1),
        AURA(45, 14);

        private final int componentIndex;
        private final int appearanceIndex;

        Slot(int componentIndex, int appearanceIndex) {
            this.componentIndex = componentIndex;
            this.appearanceIndex = appearanceIndex;
        }

        public int getComponentIndex() {
            return componentIndex;
        }

        public int getAppearanceIndex() {
            return appearanceIndex;
        }

        public Item getItem(final Methods methods) {
            final Component component = methods.context.widgets.widget(WIDGET).component(getComponentIndex());
            return component != null ? new Item(methods.context, component) : null;
        }

        public int getItemId(final Methods methods) {
            return appearanceIndex != -1 ? methods.context.players.local().appearance()[appearanceIndex] : getItem(methods).id();
        }

        public boolean isEmpty(final Methods methods) {
            return getItemId(methods) == -1;
        }
    }
}