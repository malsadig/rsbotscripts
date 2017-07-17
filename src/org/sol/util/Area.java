package org.sol.util;

import org.powerbot.script.Tile;
import org.sol.core.script.Methods;

import java.awt.*;
import java.util.ArrayList;

public class Area {
    protected final Polygon polygon;
    protected int plane = -1;
    private Tile[] tileArrayCache = null;

    public Area(final Tile t1, final Tile t2) {
        this(
                new Tile(Math.min(t1.x(), t2.x()), Math.min(t1.y(), t2.y()), t1.floor()),
                new Tile(Math.max(t1.x(), t2.x()), Math.min(t1.y(), t2.y()), t1.floor()),
                new Tile(Math.max(t1.x(), t2.x()), Math.max(t1.y(), t2.y()), t2.floor()),
                new Tile(Math.min(t1.x(), t2.x()), Math.max(t1.y(), t2.y()), t2.floor())
        );
    }

    public Area(final Tile... bounds) {
        polygon = new Polygon();
        for (final Tile tile : bounds) {
            if (plane != -1 && tile.floor() != plane) {
                throw new RuntimeException("area does not support 3d");
            }
            plane = tile.floor();
            addTile(tile);
        }
    }

    public void translate(final int x, final int y) {
        polygon.translate(x, y);
        tileArrayCache = null;
    }

    public Rectangle getBounds() {
        return polygon.getBounds();
    }

    public int floor() {
        return plane;
    }

    public void addTile(final Tile t) {
        addTile(t.x(), t.y());
    }

    public void addTile(final int x, final int y) {
        polygon.addPoint(x, y);
        tileArrayCache = null;
    }

    public boolean contains(final int x, final int y) {
        return polygon.contains(x, y);
    }

    public boolean contains(final Tile... tiles) {
        for (final Tile tile : tiles) {
            if (tile != null && plane == tile.floor() && contains(tile.x(), tile.y())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAll(final Tile... tiles) {
        for (final Tile tile : tiles) {
            if (tile == null || !contains(tile)) {
                return false;
            }
        }
        return true;
    }

    public Tile getCentralTile() {
        return polygon.npoints > 0 ? new Tile((int) Math.round(avg(polygon.xpoints)), (int) Math.round(avg(polygon.ypoints)), plane) : null;
    }

    public Tile getNearest(final Methods methods) {
        return getNearest(methods, methods.context.players.local().tile());
    }

    public Tile getNearest(final Methods methods, final Tile base) {
        final Tile[] tiles = getTileArray();
        Tile tile = null;
        long dist = Long.MAX_VALUE, temp;
        for (final Tile t : tiles) {
            temp = (long) methods.context.movement.distance(methods.context.players.local(), base);
            if (t == null || temp < dist) {
                dist = temp;
                tile = t;
            }
        }
        return tile;
    }

    public Tile[] getBoundingTiles() {
        final Tile[] bounding = new Tile[polygon.npoints];
        for (int i = 0; i < polygon.npoints; i++) {
            bounding[i] = new Tile(polygon.xpoints[i], polygon.ypoints[i], plane);
        }
        return bounding;
    }

    public Tile[] getTileArray() {
        if (tileArrayCache == null) {
            final Rectangle bounds = getBounds();
            final ArrayList<Tile> tiles = new ArrayList<Tile>(bounds.width * bounds.height);
            final int xMax = bounds.x + bounds.width, yMax = bounds.y + bounds.height;
            for (int x = bounds.x; x < xMax; x++) {
                for (int y = bounds.y; y < yMax; y++) {
                    if (contains(x, y)) {
                        tiles.add(new Tile(x, y, plane));
                    }
                }
            }
            tileArrayCache = tiles.toArray(new Tile[tiles.size()]);
        }
        return tileArrayCache;
    }

    private double avg(final int... numbers) {
        long total = 0;
        for (int i : numbers) {
            total += (long) i;
        }
        return (double) total / (double) numbers.length;
    }
}
