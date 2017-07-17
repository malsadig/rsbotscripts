package org.sol.core.ui;

import org.powerbot.script.rt6.Constants;
import org.sol.core.script.Contextable;
import org.sol.core.script.Methods;
import org.sol.util.Timer;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Paint extends Contextable {
    public final Timer RUN_TIME = new Timer(0);

    private final MouseTrail trail = new MouseTrail();

    private static final String[] SKILL_NAMES = {"attack", "defence", "strength", "constitution", "range", "prayer",
            "magic", "cooking", "woodcutting", "fletching", "fishing", "firemaking", "crafting", "smithing", "mining",
            "herblore", "agility", "thieving", "slayer", "farming", "runecrafting", "hunter", "construction",
            "summoning", "dungeoneering"};

    private static final Color[] COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE};

    public final Skill[] SKILL_LIST = new Skill[SKILL_NAMES.length];

    static final BasicStroke STROKE_2 = new BasicStroke(2);

    public final Map<String, Movable> movables;

    private final String title;
    private final Color color;

    private int startY = 45;

    public Paint(final Methods methods, final String title) {
        super(methods);

        this.title = title;

        final int rand = (int) (Math.random() * (COLORS.length));
        this.color = COLORS[rand];

        for (int i = 0; i < SKILL_NAMES.length; i++) {
            SKILL_LIST[i] = new Skill(i);
        }

        this.movables = new HashMap<String, Movable>();
    }

    public class Skill {
        private final int startXp;
        private final int startLvl;
        private final int index;
        private final String name;

        public int expPerHour;

        public Skill(final int index) {
            this.index = index;
            this.name = SKILL_NAMES[index].substring(0, 1).toUpperCase() + SKILL_NAMES[index].substring(1);
            this.startLvl = methods.context.skills.realLevel(index);//todo
            this.startXp = methods.context.skills.experience(index);
        }

        public final boolean isValid() {
            int experience = methods.context.skills.experience(index);
            return (experience - startXp) != 0;
        }

        public double getRatio() {
            final double currExp = methods.context.skills.experience(index);
            final int currLvl = methods.context.skills.realLevel(index);
            if (currLvl > 98) {
                return 100;
            }

            double currLvlExp = Constants.SKILLS_XP[currLvl],
                    nextLvlExp = Constants.SKILLS_XP[currLvl + 1];

            return ((currExp - currLvlExp) / (nextLvlExp - currLvlExp));
        }

        public int getPercent() {
            return (int) (getRatio() * 100);
        }

        public String getPaintLineInfo() {
            final int currExp = methods.context.skills.experience(index);
            final int expGained = currExp - startXp;
            final int currLevel = methods.context.skills.realLevel(index);
            final int levelsGained = currLevel - startLvl;

            expPerHour = (int) (3600000.0 / (double) (RUN_TIME.getElapsed()) * expGained);
            long ttl = 0;

            if (currLevel != 99) {
                long nextLvlExp = Constants.SKILLS_XP[currLevel + 1];

                ttl = (RUN_TIME.getElapsed() * (nextLvlExp - currExp)) / expGained;
            }
            return name + ": Exp Gained: " + expGained + " (" + expPerHour + " /hr) | " +
                    getPercent() + "% to lvl " + (currLevel + 1) + " (+" + levelsGained + ") | TTL: " + format(ttl);
        }
    }

    public void draw(final Graphics2D graphics, final List<String> skillsTrained) {
        String text = title + " | Run Time: " + format(RUN_TIME.getElapsed()) + " | " + methods.status;

        if (movables.containsKey(title)) {
            movables.get(title).texts.set(0, text);
        } else {
            Movable m = new MovableRectangle(methods, 7, startY, color);
            m.texts.add(text);
            movables.put(title, m);
            startY += 40;
        }

        for (Skill skill : SKILL_LIST) {
            if (skillsTrained.isEmpty() || skillsTrained.contains(skill.name)) {
                int realLevel = methods.context.skills.realLevel(skill.index);
                if (skill.isValid() && realLevel < 99) {
                    if (!movables.containsKey(skill.name)) {
                        Movable m = new SkillRectangle(methods, startY, color, skill.getPaintLineInfo(), skill.index);
                        movables.put(skill.name, m);
                        startY += 40;
                    } else {
                        movables.get(skill.name).texts.set(0, skill.getPaintLineInfo());
                    }
                }
            }
        }

        if (!methods.loot.isEmpty()) {
            if (movables.containsKey("Loot")) {
                ((LootRectangle) movables.get("Loot")).update(false);
            } else {
                LootRectangle loot = new LootRectangle(methods, 7, startY, color);
                loot.update(true);
                movables.put("Loot", loot);
            }
        }

        for (final Movable movable : movables.values()) {
            movable.draw(graphics, methods);
        }
        Point location = methods.context.input.getLocation();
        trail.add(location);
        graphics.setColor(color);
        trail.draw(graphics);
    }

    public static String format(final long time) {
        final StringBuilder t = new StringBuilder();
        final long total_secs = time / 1000;
        final long total_minutes = total_secs / 60;
        final long total_hrs = total_minutes / 60;
        final int secs = (int) total_secs % 60;
        final int minutes = (int) total_minutes % 60;
        final int hrs = (int) total_hrs % 60;
        if (hrs < 10) {
            t.append("0");
        }
        t.append(hrs);
        t.append(":");
        if (minutes < 10) {
            t.append("0");
        }
        t.append(minutes);
        t.append(":");
        if (secs < 10) {
            t.append("0");
        }
        t.append(secs);
        return t.toString();
    }
}