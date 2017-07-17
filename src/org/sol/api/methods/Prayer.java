package org.sol.api.methods;

import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Constants;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;

public class Prayer extends Contextable {
    private final static int PRAYER_WIDGET = 1505,
            PRAYER_COMPONENT_1 = 3,
            PRAYER_COMPONENT_2 = 7;

    public Prayer(Methods methods) {
        super(methods);
    }

    public boolean isFull() {
        return getPrayerPointsLeft() == getPrayerLevel() * 10;
    }

    public boolean isEmpty() {
        return getPrayerPointsLeft() == 0;
    }

    public int getPrayerPointsLeft() {
        String pointsLeftString = getPrayerString();
        if (pointsLeftString.equals("")) return -1;

        int pointsLeft = Integer.parseInt(pointsLeftString);

        return pointsLeft;
    }

    public int getPrayerLevel() {
        return methods.context.skills.realLevel(Constants.SKILLS_PRAYER);
    }

    public String getPrayerString() {
        Component big = methods.context.widgets.widget(PRAYER_WIDGET).component(PRAYER_COMPONENT_1);
        if (big != null && big.valid() && big.visible()) {
            Component little = big.component(PRAYER_COMPONENT_2);
            if (little != null && little.valid() && little.visible()) {
                return little.text();
            }
        }
        return "";
    }
}
