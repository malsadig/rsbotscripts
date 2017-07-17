package org.sol.core.ui;

import org.sol.core.script.Methods;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MovableRectangle extends Movable {
    int width, height;

    private final Color color;

    public MovableRectangle(Methods methods, final int x, final int y, final Color color) {
        super(methods);

        this.x = x;
        this.y = y;
        this.xMod = x;
        this.yMod = y;
        this.color = color;
    }

    @Override
    public void draw(final Graphics2D graphics, final Methods methods) {
        height = graphics.getFontMetrics().getHeight() * texts.size() + 6;
        width = getWidth(graphics) + 10;

        this.shape = new RoundRectangle2D.Float(x, y, width, height, 2, 2);

        drawRect(graphics, shape);

        graphics.setColor(Color.WHITE);

        int yOffSet = 0;
        for (final String text : texts) {
            graphics.drawString(text, x + 5, y + yOffSet + height / texts.size() - 6);
            yOffSet += graphics.getFontMetrics().getHeight();
        }
    }

    void drawRect(final Graphics2D graphics, final Shape shape) {
        graphics.setColor(Color.BLACK);
        graphics.fill(shape);

        graphics.setStroke(Paint.STROKE_2);
        graphics.setColor(color);
        graphics.draw(shape);
    }

    void fillRect(final Graphics2D graphics, final Shape shape) {
        graphics.setStroke(Paint.STROKE_2);
        graphics.setColor(color);
        graphics.fill(shape);
    }
}