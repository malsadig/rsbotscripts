package org.sol.core.concurrent;

import org.sol.core.script.Methods;
import org.sol.util.Condition;

public abstract class LoopTask extends Task {
    private boolean running;

    public LoopTask(Methods methods) {
        super(methods);
        this.running = true;
    }

    public abstract int loop();

    @Override
    public void run() {
        while (running && methods.isRunning()) {
            try {
                final int wait = loop();
                if (wait > 0) {
                    methods.sleep.sleep(new Condition() {
                        @Override
                        public boolean accept() {
                            return true;
                        }
                    }, wait);
                } else {
                    running = false;
                }
            } catch (final Throwable t) {
                t.printStackTrace();
            }
        }
    }
}