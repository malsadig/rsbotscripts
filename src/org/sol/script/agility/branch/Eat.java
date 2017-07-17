package org.sol.script.agility.branch;

import org.sol.core.node.Node;
import org.sol.core.script.Methods;

public class Eat extends Node {
    public Eat(Methods methods) {
        super(methods);
    }

    @Override
    public boolean activate() {
        return false;
    }

    @Override
    public void run() {
        //if contains food
        //eat
        //else
        //set course.isBankTime to true
    }
}
