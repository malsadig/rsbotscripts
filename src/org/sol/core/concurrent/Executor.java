package org.sol.core.concurrent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Executor {
    private final List<Thread> threads;

    public Executor() {
        this.threads = Collections.synchronizedList(new LinkedList<Thread>());
    }

    public void submit(final Runnable runnable) {
        new Thread(runnable).start();
    }

    public void shutdown() {
        while (threads.size() > 0) {
            final Thread thread = threads.remove(0);
            thread.interrupt();
        }
    }
}