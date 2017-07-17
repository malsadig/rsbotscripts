package org.sol.core.script;

import org.powerbot.script.rt6.Item;
import org.sol.core.concurrent.LoopTask;

import java.util.HashMap;
import java.util.Map;

public class InventoryMonitor extends LoopTask {
    private final Map<Integer, Integer> cache;

    public InventoryMonitor(Methods methods) {
        super(methods);

        this.cache = new HashMap<Integer, Integer>();
        update();
    }

    @Override
    public int loop() {
        if (!methods.loot.isEmpty()) {
            final Map<Integer, Integer> changes = new HashMap<Integer, Integer>();

            for (final Item item : methods.context.backpack.items()) {
                final int curCount = methods.context.backpack.select().id(item.id()).count(true);
                final int oldCount = getCacheCount(item.id());
                if (curCount > oldCount) {
                    changes.put(item.id(), curCount - oldCount);
                }
            }

            if (changes.size() > 0) {
                for (final int id : methods.loot.keySet()) {
                    if (changes.containsKey(id)) {
                        final int deltaCount = changes.get(id);
                        methods.loot.get(id).add(deltaCount);
                    }
                }
                update();
            }
        }
        return 45;
    }

    void update() {
        cache.clear();

        for (final Item item : methods.context.backpack.items()) {
            if (cache.containsKey(item.id())) {
                final int oldValue = cache.remove(item.id());
                cache.put(item.id(), oldValue + item.stackSize());
            } else {
                cache.put(item.id(), item.stackSize());
            }
        }
    }

    private int getCacheCount(final int id) {
        return cache.containsKey(id) ? cache.get(id) : 0;
    }
}