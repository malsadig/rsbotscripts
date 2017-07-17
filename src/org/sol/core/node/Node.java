package org.sol.core.node;

import org.sol.core.concurrent.Task;
import org.sol.core.script.Methods;

public abstract class Node extends Task {

    public Node(Methods methods) {
        super(methods);
    }

    public abstract boolean activate();

    @Override
    public String toString() {
        return "- " + this.getClass().getSimpleName() + " -";
    }

    @Override
    public boolean equals(Object o) {
        return this == o ||
                o instanceof Node &&
                        this.getClass().getSimpleName().equals(o.getClass().getSimpleName());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}