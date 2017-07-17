package org.sol.script.barrows.wrappers;

import org.sol.script.barrows.combat.Brother;
import org.sol.util.Area;

public class Crypt {
    private Brother brother;
    public Area moundArea;


    private static final int[] SPADE_IDS = new int[]{66115, 66116},
            SARCOPHAGUS_IDS = new int[]{66055, 66056},
            STAIR_IDS = new int[]{6702, 6704, 6705, 6706, 6707};

    public Crypt(final Brother brother, final Area moundArea) {
        this.brother = brother;
        this.moundArea = moundArea;
    }
    //area for mound
    //spade
    ////spade action
    //brother inside
    //crypt id
    //if find nothing, crypt complete

    //if "you've found a hidden tunnel
    ////widget = 1186
    ////component 2 for text
    ////component 5 to click
    ////component


    ////6716, 6735, 6722, 6741,
    //locked doors//6723, 6742

    //sarcophagus ids = 66055
}