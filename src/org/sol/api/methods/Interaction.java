package org.sol.api.methods;

import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Widget;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;

public class Interaction extends Contextable {
    public Interaction(Methods methods) {
        super(methods);
    }

    public boolean containsText(final Widget widget, String text) {
        return getChildContaining(widget, text) != null;
    }

    public boolean interactWidget(final Widget widget, String text) {
        final Component child = getChildContaining(widget, text);
        return child != null && child.click(true);
    }

    public Widget getWidgetContaining(String text) {
        text = text.toLowerCase();
        for (final Widget widget : methods.context.widgets.sparseCache) {
            if (widget.valid()) {
                if (getChildContaining(widget, text) != null) {
                    return widget;
                }
            }
        }
        return null;
    }

    public Component getChildContaining(final Widget widget, String text) {
        if (widget.valid()) {
            for (final Component c : widget.components()) {
                if (c.text().toLowerCase().contains(text)) {
                    return c;
                }
                final Component child = getChildHelper(c, text);
                if (child != null) {
                    return child;
                }
            }
        }
        return null;
    }

    private Component getChildHelper(final Component widget, String text) {
        if (widget.valid()) {
            for (final Component child : widget.components()) {
                if (child != null) {
                    if (getChildHelper(child, text) != null) {
                        return child;
                    }
                    String string = child.text();
                    if (string != null && string.toLowerCase().contains(text)) {
                        return child;
                    }
                }
            }
        }
        return null;
    }

    Component getContinue() {
        Component button = methods.context.widgets.widget(752).component(5);
        if (button != null && button.valid() && button.textColor() == 128 && button.relativePoint().getX() == 0) {
            return button;
        }
        button = methods.context.widgets.widget(1184).component(18);
        if (button != null && button.valid()) {
            return button;
        }
        button = methods.context.widgets.widget(1191).component(18);
        if (button != null && button.valid()) {
            return button;
        }
        return null;
    }

    public boolean canContinue() {
        return getContinue() != null;
    }

    public boolean clickContinue() {
        final Component widgetChild = getContinue();
        return widgetChild != null && widgetChild.click(true);
    }
}