package org.sol.core.concurrent;

import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;

public abstract class Task extends Contextable implements Runnable {
    public Task(Methods methods) {
        super(methods);
    }
}