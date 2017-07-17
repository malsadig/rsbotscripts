package org.sol.util;

import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;

public class Loot extends Contextable {
    private int price;
    private String name;
    private int amountCollected;

    public Loot(Methods methods, final int price, final String name) {
        super(methods);

        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAmountCollected() {
        return amountCollected;
    }

    public void add(final int collected) {
        this.amountCollected += collected;
    }

    public int moneyGained() {
        return price * amountCollected;
    }

    public int moneyPerHour() {
        return (int) (3600000.0 / (double) (methods.omniPaint.RUN_TIME.getElapsed()) * moneyGained());
    }

    public int getAmountPerHour() {
        return (int) (3600000.0 / (double) (methods.omniPaint.RUN_TIME.getElapsed()) * amountCollected);
    }
}