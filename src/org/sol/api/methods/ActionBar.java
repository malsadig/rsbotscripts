package org.sol.api.methods;

import org.powerbot.script.rt6.Component;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;

public class ActionBar extends Contextable {
    private static final int ACTION_BAR_WIDGET = 1430;
    private static final int LOCK_COMPONENT = 19;

    private static final int COLLAPSE_WIDGET = 1477;
    private static final int COLLAPSE_CHILD = 56;
    private static final int COLLAPSE_BUTTON = 2;

    private static final int BAR_LOCKED = 682, BAR_LOCKED_MASK = 0x10;

    public ActionBar(Methods methods) {
        super(methods);
    }

    public boolean isExpanded() {
        Component child = methods.context.widgets.widget(ACTION_BAR_WIDGET).component(0);
        return child != null && child.visible();
    }

    public boolean expand(final boolean expanded) {
        if (expanded == isExpanded()) {
            return true;
        }

        final Component container = methods.context.widgets.widget(COLLAPSE_WIDGET).component(COLLAPSE_CHILD);

        if (container != null && container.valid() && container.visible()) {
            final Component button = container.component(COLLAPSE_BUTTON);
            return button != null && button.valid() && button.visible() && button.click(true);
        }
        return false;
    }

    public boolean isLocked() {
        return (methods.context.varpbits.varpbit(BAR_LOCKED) & BAR_LOCKED_MASK) == BAR_LOCKED_MASK;
    }

    public boolean lock(final boolean lock) {
        if (lock == isLocked()) {
            return true;
        }
        final Component button = methods.context.widgets.widget(ACTION_BAR_WIDGET).component(LOCK_COMPONENT);
        return button != null && button.click(true);
    }

    public Component getNode(final int index) {
        return methods.context.widgets.widget(ACTION_BAR_WIDGET).component(18 - index);
    }
}