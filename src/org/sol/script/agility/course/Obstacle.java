package org.sol.script.agility.course;

import org.powerbot.script.rt6.GameObject;
import org.sol.core.script.Methods;
import org.sol.util.ContextCondition;
import org.sol.util.SimpleExecute;

public class Obstacle {
    private Methods methods;
    private int failCount;

    private String interaction;
    private int[] ids;
    private Courses course;
    private int ordinal;
    private String objectName;

    private ContextCondition isInFailArea;
    private ContextCondition isInArea;


    private boolean failable;
    private boolean bankObstacle;
    private boolean usingId;

    private SimpleExecute goToBank = null;
    private SimpleExecute bank = null;
    private SimpleExecute returnToCourse = null;

    public State state = State.NOT_STARTED;

    public Obstacle(Obstacle other) {
        this.ordinal = other.ordinal;
        this.course = other.course;
        this.ids = other.ids;
        this.objectName = other.objectName;
        this.interaction = other.interaction;
        this.isInArea = other.isInArea;
        this.isInFailArea = other.isInFailArea;

        this.failable = other.failable;

        this.goToBank = other.goToBank;
        this.bank = other.bank;
        this.returnToCourse = other.returnToCourse;

        this.bankObstacle = other.bankObstacle;
        this.usingId = other.usingId;
    }

    public Obstacle(int ordinal, Courses course, int[] ids, String objectName, String interaction,
                    ContextCondition isInArea, ContextCondition isInFailArea, SimpleExecute goToBank,
                    SimpleExecute bank, SimpleExecute returnToCourse) {
        this.ordinal = ordinal;
        this.course = course;
        this.ids = ids;
        this.objectName = objectName;
        this.interaction = interaction;
        this.isInArea = isInArea;
        this.isInFailArea = isInFailArea;

        this.failable = this.isInFailArea != null;

        this.goToBank = goToBank;
        this.bank = bank;
        this.returnToCourse = returnToCourse;

        this.bankObstacle = this.goToBank != null;
        this.usingId = (ids != null);
    }

    public static class Builder {
        private String interaction = "";
        private int[] ids = null;
        private Courses course = null;
        private int ordinal = -1;
        private String objectName = "";

        private ContextCondition isInfailArea = null;
        private ContextCondition isInArea = null;

        private SimpleExecute goToBank = null;
        private SimpleExecute bank = null;
        private SimpleExecute returnToCourse = null;

        public Builder setInteraction(String interaction) {
            this.interaction = interaction;
            return this;
        }

        public Builder setIds(int... ids) {
            this.ids = ids;
            return this;
        }

        public Builder setCourse(Courses course) {
            this.course = course;
            return this;
        }

        public Builder setOrdinal(int ordinal) {
            this.ordinal = ordinal;
            return this;
        }

        public Builder setObjectName(String objectName) {
            this.objectName = objectName;
            return this;
        }

        public Builder setFailArea(ContextCondition isInfailArea) {
            this.isInfailArea = isInfailArea;
            return this;
        }

        public Builder setArea(ContextCondition isInArea) {
            this.isInArea = isInArea;
            return this;
        }

        public Builder setGoToBank(SimpleExecute goToBank) {
            this.goToBank = goToBank;
            return this;
        }

        public Builder setBank(SimpleExecute bank) {
            this.bank = bank;
            return this;
        }

        public Builder setReturnToCourse(SimpleExecute returnToCourse) {
            this.returnToCourse = returnToCourse;
            return this;
        }

        public Obstacle build() {
            return new Obstacle(this.ordinal, this.course, this.ids, this.objectName, this.interaction, this.isInArea,
                    this.isInfailArea, this.goToBank, this.bank, this.returnToCourse);
        }
    }

    public enum State {
        NOT_STARTED, IN_PROGRESS, FAILED, COMPLETED;
    }

    public void incrementFailCount() {
        failCount++;
    }

    // todo
    public GameObject getObject(Methods methods) {
        if(usingId && !methods.context.objects.select(20).id(this.ids).nearest().first().isEmpty()) {
            return methods.context.objects.poll();
        } else if (!methods.context.objects.select(20).name(this.objectName).nearest().first().isEmpty()) {
            return methods.context.objects.poll();
        } else {
            return null;
        }
    }

    public boolean isUsingId() {
        return usingId;
    }

    public boolean isBankObstacle() {
        return bankObstacle;
    }

    public boolean isFailable() {
        return failable;
    }

    public Courses getCourse() {
        return course;
    }

    public String getInteraction() {
        return interaction;
    }

    public int getFailCount() {
        return failCount;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public boolean isInArea(Methods methods) {
        return isInArea.accept(methods);
    }

    public boolean isInFailArea(Methods methods) {
        return isInFailArea.accept(methods);
    }
}