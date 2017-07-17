package org.sol.api.path;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.TilePath;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;
import org.sol.util.Condition;

public abstract class Obstacle extends Contextable implements Condition {
    private String objectName;
    private int id;
    private String action;
    private TilePath path;
    private String name;

    public Obstacle(Methods methods, final String name, final int id, final String action, final Tile... tiles) {
        super(methods);

        this.id = id;
        this.action = action;
        this.name = name;

        this.path = methods.context.movement.newTilePath(tiles);
    }

    public Obstacle(Methods methods, final String name, final int id, final String action) {
        super(methods);

        this.id = id;
        this.action = action;
        this.name = name;
    }

    public boolean containsPath() {
        return path != null;
    }

    public int getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public TilePath getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
