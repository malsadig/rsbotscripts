package org.sol.script.agility.course;

import org.sol.util.Area;

public enum Courses {
    BURTHORPE(new Area(), null),
    GNOME_BASIC(null, null),
    BARBARIAN_BASIC(null, null),
    APE_ATOLL(null, null),
    GNOME_ADVANCED(null, null),
    BARBARIAN_ADVANCED(null, null);

    private final Area bankArea;
    private final Area courseArea;

    Courses(final Area courseArea, final Area bankArea) {
        this.bankArea = bankArea;
        this.courseArea = courseArea;
    }

    public Area getBankArea() {
        return bankArea;
    }

    public Area getCourseArea() {
        return courseArea;
    }
}