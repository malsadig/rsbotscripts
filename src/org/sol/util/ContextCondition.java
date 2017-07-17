package org.sol.util;

import org.sol.core.script.Methods;

public interface ContextCondition {
    public boolean accept(Methods methods);
}
