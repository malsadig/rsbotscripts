package org.sol.core.ui;

import org.sol.core.script.Methods;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SkillRectangle extends MovableRectangle {
    private final int skill;

    public SkillRectangle(final Methods methods, final int y, final Color color, final String text, final int skill) {
        super(methods, 7, y, color);
        this.skill = skill;
        texts.add(text);
    }

    @Override
    public void draw(Graphics2D graphics, final Methods methods) {
        super.draw(graphics, methods);

        final double ratio = methods.omniPaint.SKILL_LIST[skill].getRatio();

        final RoundRectangle2D rect = new RoundRectangle2D.Float(x, y, (float) ((width + 10) * ratio), height, 2, 2);
        fillRect(graphics, rect);

        graphics.setColor(Color.WHITE);
        graphics.drawString(texts.get(0), x + 5, y + height - 3);
    }
}
