package org.sol.script.barrows.node;

import org.powerbot.script.Tile;
import org.sol.script.barrows.combat.Brother;
import org.sol.script.barrows.wrappers.Crypt;
import org.sol.util.Area;

import java.util.LinkedList;
import java.util.List;

public class Trip {
    private List<Crypt> crypts;

    public Trip() {
        crypts = new LinkedList<Crypt>();
        Crypt verac = new Crypt(new Brother(),
                new Area(
                        new Tile(3555, 3295, 0),
                        new Tile(3560, 3300, 0)
                )
        );
        Crypt dharok = new Crypt(new Brother(),
                new Area(
                        new Tile(3572, 3300, 0),
                        new Tile(3577, 3295, 0)
                )
        );
        Crypt ahrim = new Crypt(new Brother(),
                new Area(
                        new Tile(3563, 3291, 0),
                        new Tile(3568, 3286, 0)
                )
        );
        Crypt guthan = new Crypt(new Brother(),
                new Area(
                        new Tile(3573, 3283, 0),
                        new Tile(3578, 33278, 0)
                )
        );
        Crypt karil = new Crypt(new Brother(),
                new Area(
                        new Tile(3567, 3275, 0),
                        new Tile(3562, 3280, 0)
                )
        );
        Crypt torag = new Crypt(new Brother(),
                new Area(
                        new Tile(3557, 3285, 0),
                        new Tile(3552, 3280, 0)
                )
        );
        crypts.add(verac);
        crypts.add(dharok);
        crypts.add(ahrim);
        crypts.add(guthan);
        crypts.add(karil);
        crypts.add(torag);
    }

    public boolean isComplete() {
        return killedBrothers() && completedCrypt() && retrievedTreasure();
    }

    private boolean retrievedTreasure() {
        return false;
    }

    private boolean completedCrypt() {
        return false;
    }

    private boolean killedBrothers() {
        return false;
    }
}