package org.sol.core.script;

import org.powerbot.script.rt6.ClientContext;
import org.sol.api.methods.*;
import org.sol.core.concurrent.Executor;
import org.sol.core.ui.Paint;
import org.sol.util.Loot;
import org.sol.util.OnlineCounter;
import org.sol.util.Sleep;

import java.util.HashMap;
import java.util.Map;

public class Methods {
    public ClientContext context;

    private final Executor container;

    public GrandExchange grandExchange;

    public ActionBar actionBar;
    public Equipment equipment;
    public Interaction interaction;
    public Lodestone lodestone;
    public Sleep sleep;
    public Summoning summoning;
    public OnlineCounter onlineCounter;
    public Banking banking;
    public Fighting fighting;
    public House house;
    public Prayer prayer;

    public Paint omniPaint;

    public final Map<Integer, Integer> withdrawables;
    public final Map<Integer, Loot> loot;

    public final OmniScript omniScript;

    private boolean running;

    public String status;

    public Methods(final ClientContext ctx, final OmniScript omniScript) {
        running = true;

        this.context = ctx;

        this.omniScript = omniScript;

        this.container = new Executor();
        this.withdrawables = new HashMap<Integer, Integer>();
        this.loot = new HashMap<Integer, Loot>();

        this.actionBar = new ActionBar(this);
        this.equipment = new Equipment(this);
        this.grandExchange = new GrandExchange(this);
        this.interaction = new Interaction(this);
        this.lodestone = new Lodestone(this);
        this.sleep = new Sleep(this);
        this.summoning = new Summoning(this);
        this.house = new House(this);
        this.prayer = new Prayer(this);
        this.onlineCounter = new OnlineCounter(this);

        this.omniPaint = new Paint(this, this.omniScript.TITLE);
    }

    public void shutdownContainer() {
        this.running = false;
        container.shutdown();
    }

    public void activate(final Banking banking) {
        this.banking = banking;
    }

    public void activate(final Fighting fighting) {
        this.fighting = fighting;
    }

    public Executor getContainer() {
        return container;
    }

    public boolean isRunning() {
        return running;
    }

    public void addLoot(final int id, final String name) {
        int price = grandExchange.getPrice(id);
        loot.put(id, new Loot(this, price, name));
    }
}