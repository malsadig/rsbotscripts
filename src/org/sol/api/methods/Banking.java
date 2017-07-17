package org.sol.api.methods;

import org.powerbot.script.Filter;
import org.powerbot.script.rt6.Bank;
import org.powerbot.script.rt6.Item;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;
import org.sol.util.Area;

public abstract class Banking extends Contextable {
    private final Area area;

    private int[] toWithDraw;
    private Item toDeposit;

    public Banking(Methods methods, final Area area) {
        super(methods);

        this.area = area;
        addWithdrawables();
    }

    public boolean activate() {
        return (needsToBank() || area.contains(methods.context.players.local().tile()))
                && ((toDeposit = depositItem()) != null
                || (toWithDraw = withdrawItem()) != null
                || methods.context.bank.opened());
    }

    public abstract void addWithdrawables();

    public abstract boolean needsToBank();

    public abstract void exit();

    public abstract void walkToBank();

    public void run() {
        if (needsToBank() && !area.contains(methods.context.players.local().tile())) {
            exit();
            return;
        }
        if (methods.context.bank.opened()) {
            if (toDeposit != null) {
                methods.status = "Depositing " + toDeposit.name();
                methods.context.bank.deposit(toDeposit.id(), Bank.Amount.ALL);
            } else if (toWithDraw != null) {
                withdraw();
            } else {
                methods.context.bank.close();
            }
        } else {
            if (methods.context.bank.inViewport()) {
                methods.context.camera.turnTo(methods.context.bank.nearest());
                methods.context.bank.open();
            } else {
                walkToBank();
            }
        }
    }

    private void withdraw() {
        final int id = toWithDraw[0], amount = toWithDraw[1],
                inventoryCount = methods.context.backpack.select().id(id).count(true);
        if (inventoryCount > amount) {
            methods.status = "Depositing " + id;
            methods.context.bank.deposit(id, inventoryCount - amount);
        } else {
            if (methods.context.bank.select().id(id).count(true) >= amount - inventoryCount) {
                methods.status = "Withdrawing " + id;
                methods.context.bank.withdraw(id, amount - inventoryCount);
            } else {
                System.out.println("Out of: " + id + ".");
            }
        }
    }

    private int[] withdrawItem() {
        for (final int withdrawal : methods.withdrawables.keySet()) {
            final int amount = methods.withdrawables.get(withdrawal);
            if (amount != -1 && methods.context.backpack.select().id(withdrawal).count(true) != amount) {
                return new int[]{withdrawal, amount};
            }
        }
        return null;
    }

    private Item depositItem() {
        if (!methods.context.backpack.select().select(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item != null && item.valid() && !methods.withdrawables.keySet().contains(item.id());
            }
        }).isEmpty()) {
            return methods.context.backpack.poll();
        }
        return null;
    }

    public Area getArea() {
        return this.area;
    }
}