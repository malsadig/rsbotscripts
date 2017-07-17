package org.sol.script.agility.branch;

import org.sol.core.node.Node;
import org.sol.core.script.Methods;
import org.sol.script.agility.OmniAgility;

public class Bank extends Node {
    public Bank(Methods methods) {
        super(methods);
    }

    @Override
    public boolean activate() {
        return OmniAgility.course.getInternal().getBankArea().contains(methods.context.players.local().tile())
                && OmniAgility.course.isBankTime();
    }

    @Override
    public void run() {

    }
}