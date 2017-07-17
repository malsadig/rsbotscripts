package org.sol.core.node;

import java.util.ArrayList;

public class Tree extends ArrayList<Node> {
    public void run() {
        for (Node node : this) {
            if (node.activate()) {
                node.run();
                break;
            }
        }
    }
}