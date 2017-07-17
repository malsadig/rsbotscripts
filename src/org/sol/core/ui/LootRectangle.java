package org.sol.core.ui;

import org.sol.core.script.Methods;
import org.sol.util.Loot;

import java.awt.*;

public class LootRectangle extends MovableRectangle {
    public LootRectangle(Methods methods, int x, int y, Color color) {
        super(methods, x, y, color);
    }

    public void update(boolean firstTime) {
        int index = 0;
        if (firstTime) {
            texts.add("Loot:");
        } else {
            texts.set(index++, "Loot:");
        }

        int totalMoney = 0;
        int totalPerHour = 0;

        for (final int id : methods.loot.keySet()) {
            Loot loot = methods.loot.get(id);
            String name = loot.getName();
            int amount = loot.getAmountCollected(),
                    amountPerHour = loot.getAmountPerHour(),
                    moneyGained = loot.moneyGained(),
                    moneyPerHour = loot.moneyPerHour();
            String text = name + " Gotten: " + amount + " (" + amountPerHour + " /hr), ($"
                    + moneyGained + "),  ($" + moneyPerHour + " /hr)";

            totalMoney += moneyGained;
            totalPerHour += moneyPerHour;

            if (firstTime) {
                texts.add(text);
            } else {
                texts.set(index++, text);
            }
        }
        if (firstTime) {
            texts.add("Total Money: $" + totalMoney + " ($" + totalPerHour + ")");
        } else {
            texts.set(index, "Total Money: $" + totalMoney + " ($" + totalPerHour + ")");
        }
    }
}
